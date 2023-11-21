package com.bersihin.mobileapp.api

import com.bersihin.mobileapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        fun setAuthToken(token: String) {
            authToken = token
            INSTANCE = null
        }

        fun <T> getService(serviceClass: Class<T>): T {
            return getClient().create(serviceClass)
        }
    }
}