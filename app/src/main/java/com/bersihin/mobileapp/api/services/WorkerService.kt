package com.bersihin.mobileapp.api.services

import com.bersihin.mobileapp.api.MessageResponse
import com.bersihin.mobileapp.api.SuccessResponse
import com.bersihin.mobileapp.models.Report
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class ReportsRequest(
    val sortByDate: Boolean,
    val lat: Double,
    val lon: Double,
    val distanceLimit: Int = 10
)

data class UpdateRequest(
    val reportId: String,
    val status: String,
    val statusReason: String
)

interface WorkerService {
    companion object {
        const val PATH = "worker"
    }

    @POST("${PATH}/reports")
    suspend fun getReports(
        @Body request: ReportsRequest
    ): SuccessResponse<List<Report>>

    @GET("${PATH}/history")
    suspend fun getHistory(): SuccessResponse<List<Report>>

    // TODO: update jadi PUT kalo udah up
    @POST("${PATH}/report-update")
    suspend fun updateReport(
        @Body request: UpdateRequest
    ): MessageResponse
}