package com.bersihin.mobileapp.api.services

import com.bersihin.mobileapp.api.SuccessResponse
import com.bersihin.mobileapp.models.LoginData
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


interface AuthService {
    companion object {
        const val PATH = "auth"
    }

    @POST("${PATH}/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): SuccessResponse<String>

    @POST("${PATH}/login")
    suspend fun login(
        @Body request: LoginRequest
    ): SuccessResponse<LoginData>

    @GET("${PATH}/credential-info")
    suspend fun credentialInfo(): SuccessResponse<LoginData>
}