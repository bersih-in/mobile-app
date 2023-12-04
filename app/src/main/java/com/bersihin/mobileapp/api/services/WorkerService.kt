package com.bersihin.mobileapp.api.services

import com.bersihin.mobileapp.api.SuccessResponse
import com.bersihin.mobileapp.models.Report
import retrofit2.http.Body
import retrofit2.http.POST

data class ReportsRequest(
    val sortByDate: Boolean,
    val lat: Double,
    val lon: Double,
    val distanceLimit: Int = 10
)

interface WorkerService {
    companion object {
        const val PATH = "worker"
    }

    @POST("${PATH}/reports")
    suspend fun getReports(
        @Body request: ReportsRequest
    ): SuccessResponse<List<Report>>
}