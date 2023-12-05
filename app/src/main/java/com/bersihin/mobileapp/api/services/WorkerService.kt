package com.bersihin.mobileapp.api.services

import com.bersihin.mobileapp.api.MessageResponse
import com.bersihin.mobileapp.api.SuccessResponse
import com.bersihin.mobileapp.models.Report
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class ReportsRequest(
    val sortByDate: Boolean,
    val lat: Double? = 0.0,
    val lon: Double? = 0.0,
    val distanceLimit: Int = 10,
    val status: String = "PENDING",
    val bySelf: Boolean = false
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

    @PUT("${PATH}/report-update")
    suspend fun updateReport(
        @Body request: UpdateRequest
    ): MessageResponse

    @GET("${PATH}/report/{id}")
    suspend fun getReportDetails(
        @Path("id") reportId: String
    ): SuccessResponse<Report>
}