package com.bersihin.mobileapp.api.services

import com.bersihin.mobileapp.api.responses.AuthResponse
import retrofit2.http.Body
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


interface AuthService {
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): AuthResponse

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse
}