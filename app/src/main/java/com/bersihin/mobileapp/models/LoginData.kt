package com.bersihin.mobileapp.models

import com.google.gson.annotations.SerializedName

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
