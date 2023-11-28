package com.bersihin.mobileapp.repository

import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.models.ReportDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WorkerRepository {
    private val reportList = mutableListOf<Report>()

    init {
        if (reportList.isEmpty()) {
            ReportDataSource.reports.forEach {
                reportList.add(it)
            }
        }
    }

    fun getReports(): Flow<List<Report>> {
        return flowOf(reportList)
    }

    fun getReportDetails(reportId: String): Report {
        return reportList.first {
            it.id == reportId
        }
    }
}