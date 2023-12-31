package com.bersihin.mobileapp.repository

import com.bersihin.mobileapp.api.ApiConfig
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.api.services.AuthService
import com.bersihin.mobileapp.api.services.LoginRequest
import com.bersihin.mobileapp.api.services.RegisterRequest
import com.bersihin.mobileapp.models.LoginData
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val service: AuthService
) {
    companion object {
        @Volatile
        private var INSTANCE: AuthRepository? = null

        fun getInstance(): AuthRepository {
            return INSTANCE ?: synchronized(this) {
                val service = ApiConfig.getService(AuthService::class.java)
                AuthRepository(service).also {
                    INSTANCE = it
                }
            }
        }
    }

    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        role: String,
    ): Response<String> {
        return try {
            val response = service.register(
                RegisterRequest(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password,
                    role = role
                )
            )

            Response.Success(response)
        } catch (e: HttpException) {
            val errorMessage =
                JSONObject(e.response()?.errorBody()?.string()!!).getString("message")
            Response.Error(errorMessage)
        } catch (e: IOException) {
            Response.NetworkError
        }
    }


    suspend fun login(email: String, password: String): Response<LoginData> {
        return try {
            val response = service.login(LoginRequest(email, password))
            Response.Success(response)
        } catch (e: HttpException) {
            val errorMessage =
                JSONObject(e.response()?.errorBody()?.string()!!).getString("message")
            Response.Error(errorMessage)
        } catch (e: IOException) {
            Response.NetworkError
        }
    }

    suspend fun checkAuthToken(): Response<LoginData> {
        return try {
            val response = service.credentialInfo()
            Response.Success(response)
        } catch (e: HttpException) {
            val errorMessage =
                JSONObject(e.response()?.errorBody()?.string()!!).getString("message")
            Response.Error(errorMessage)
        } catch (e: IOException) {
            Response.NetworkError
        }
    }
}