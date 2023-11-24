package com.bersihin.mobileapp.api.services

import com.bersihin.mobileapp.api.responses.AuthResponse
import com.bersihin.mobileapp.api.responses.LoginSuccessResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val role: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

sealed class LoginResponse {
    data class Success(val response: LoginSuccessResponse) : LoginResponse()
    data class Error(val errorMessage: String) : LoginResponse()
    object NetworkError : LoginResponse()
}


interface AuthService {
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): AuthResponse

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginSuccessResponse

    @GET("auth/credential-info")
    suspend fun credentialInfo(): LoginSuccessResponse
}