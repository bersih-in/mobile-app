package com.bersihin.mobileapp.repository

import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.api.services.ReportsRequest
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
        if (reportList.isEmpty()) {
            ReportDataSource.reports.forEach {
                reportList.add(it)
            }
        }
    }

    suspend fun getReports(sortByDate: Boolean): Response<List<Report>> {
//        return flowOf(reportList)
        return try {
            val response = service.getReports(
                ReportsRequest(
                    sortByDate = sortByDate,
                    lat = -6.2,
                    lon = 106.2,
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
        return reportList.first {
            it.id == reportId
        }
    }
}