package com.bersihin.mobileapp.api

import com.bersihin.mobileapp.BuildConfig
import com.bersihin.mobileapp.utils.ReportStatus
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


class ReportStatusDeserializer : JsonDeserializer<ReportStatus> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ReportStatus {
        val statusAsString = json?.asString
        return ReportStatus.entries.find { it.status == statusAsString }
            ?: throw JsonParseException("Invalid Report Status")
    }
}

class ApiConfig {
    companion object {
        @Volatile
        private var INSTANCE: Retrofit? = null
        private var authToken: String? = null

        private fun getClient(): Retrofit {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildRetrofit().also { INSTANCE = it }
            }
        }

        private fun buildRetrofit(): Retrofit {
            val gson = GsonBuilder()
                .registerTypeAdapter(ReportStatus::class.java, ReportStatusDeserializer())
                .create()

            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = if (authToken != null) {
                    req.newBuilder()
                        .addHeader("Authorization", "Bearer $authToken")
                        .build()
                } else {
                    req
                }
                chain.proceed(requestHeaders)
            }


            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }

        fun setAuthToken(token: String) {
            authToken = token
            synchronized(this) {
                buildRetrofit().also { INSTANCE = it }
            }
        }

        fun <T> getService(serviceClass: Class<T>): T {
            return getClient().create(serviceClass)
        }
    }
}