package com.bersihin.mobileapp.repository

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
    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        role: String,
    ) = service.register(
        RegisterRequest(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            role = role
        )
    )


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