package com.bersihin.mobileapp.repository

import android.util.Log
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.api.services.ReportsRequest
import com.bersihin.mobileapp.api.services.UpdateRequest
import com.bersihin.mobileapp.api.services.WorkerService
import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.models.ReportStatus
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
        sortByDate: Boolean = false,
        latitude: Double = 0.0,
        longitude: Double = 0.0,
        distanceLimit: Int = 10,
        statusName: String = ReportStatus.VERIFIED.name,
        bySelf: Boolean = false
    ): Response<List<Report>> {
//        return flowOf(reportList)

        reportList.clear()

        return try {
            val response = service.getReports(
                ReportsRequest(
                    sortByDate = sortByDate,
                    lat = latitude,
                    lon = longitude,
                    distanceLimit = distanceLimit,
                    status = statusName,
                    bySelf = bySelf
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

    suspend fun getReportDetails(reportId: String): Response<Report> {
//        Log.i("WorkerRepository", "getReportDetails: ${reportList}")
//        return reportList.first {
//            it.id == reportId
//        }

        return try {
            val response = service.getReportDetails(reportId)

            Response.Success(response)
        } catch (e: HttpException) {
            val errorMessage =
                JSONObject(e.response()?.errorBody()?.string()!!).getString("message")
            Response.Error(errorMessage)
        } catch (e: IOException) {
            Response.NetworkError
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