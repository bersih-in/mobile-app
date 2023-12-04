package com.bersihin.mobileapp.repository

import android.util.Log
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.api.services.ReportsRequest
import com.bersihin.mobileapp.api.services.UpdateRequest
import com.bersihin.mobileapp.api.services.WorkerService
import com.bersihin.mobileapp.models.Report
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class WorkerRepository(
    private val service: WorkerService
) {
    private val reportList = mutableListOf<Report>()


    init {
        Log.i("WorkerRepository", "initializing worker repository")
        if (reportList.isEmpty()) {
            ReportDataSource.reports.forEach {
                reportList.add(it)
            }
        }
    }

    suspend fun getReports(
        sortByDate: Boolean,
        latitude: Double,
        longitude: Double
    ): Response<List<Report>> {
//        return flowOf(reportList)

        reportList.clear()

        return try {
            val response = service.getReports(
                ReportsRequest(
                    sortByDate = sortByDate,
                    lat = latitude,
                    lon = longitude,
                    distanceLimit = 10
                )
            )

            response.data.forEach {
                reportList.add(it)
            }

            Response.Success(response)
        } catch (e: HttpException) {
            val errorMessage =
                JSONObject(e.response()?.errorBody()?.string()!!).getString("message")
            Response.Error(errorMessage)
        } catch (e: IOException) {
            Response.NetworkError
        }
    }

    fun getReportDetails(reportId: String): Report {
        Log.i("WorkerRepository", "getReportDetails: ${reportList}")
        return reportList.first {
            it.id == reportId
        }
    }

    suspend fun getHistory(): Response<List<Report>> {
        reportList.clear()

        return try {
            val response = service.getHistory()

            response.data.forEach {
                reportList.add(it)
            }

            Log.i("WorkerRepository", "getHistory: ${reportList}")

            Response.Success(response)
        } catch (e: HttpException) {
            val errorMessage =
                JSONObject(e.response()?.errorBody()?.string()!!).getString("message")
            Response.Error(errorMessage)
        } catch (e: IOException) {
            Response.NetworkError
        }
    }

    suspend fun updateReport(
        reportId: String,
        status: String,
        statusReason: String
    ) =
        service.updateReport(
            UpdateRequest(
                reportId = reportId,
                status = status,
                statusReason = statusReason
            )
        )
    
}