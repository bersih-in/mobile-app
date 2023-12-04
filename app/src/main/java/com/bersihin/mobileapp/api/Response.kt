package com.bersihin.mobileapp.api

import com.google.gson.annotations.SerializedName

sealed class Response<out T> {
    data class Success<out T>(val response: SuccessResponse<T>) : Response<T>()
    data class Error(val errorMessage: String) : Response<Nothing>()
    data object NetworkError : Response<Nothing>()
}

data class SuccessResponse<out T>(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("data")
    val data: T
)

data class MessageResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
)