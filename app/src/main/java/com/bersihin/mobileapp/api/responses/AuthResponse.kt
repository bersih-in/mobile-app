package com.bersihin.mobileapp.api.responses

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
)

