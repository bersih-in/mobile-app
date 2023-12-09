package com.bersihin.mobileapp.api.services

import com.bersihin.mobileapp.api.SuccessResponse
import com.bersihin.mobileapp.models.Report
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class SubmitRequest(
    val title: String,
    val description: String,
    val imageUrl: String,
    val lat: Double,
    val lon: Double
)

interface UserService {
    companion object {
        const val PATH = "submission"
    }

    @GET("${PATH}/self")
    suspend fun getSelfSubmission(): SuccessResponse<List<Report>>

    @POST("${PATH}/submit")
    suspend fun submitReport(
        @Body request: SubmitRequest
    ): SuccessResponse<Report>
}