package com.bersihin.mobileapp.api.responses

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class LoginSuccessResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("data")
    val data: LoginData
)

data class LoginData(
    @field:SerializedName("firstName")
    val firstName: String,

    @field:SerializedName("lastName")
    val lastName: String,

    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("token")
    val token: String
)
