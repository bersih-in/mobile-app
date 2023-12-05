package com.bersihin.mobileapp.ui.pages.worker.home

import androidx.lifecycle.ViewModel
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.models.ReportStatus
import com.bersihin.mobileapp.repository.WorkerRepository
import com.bersihin.mobileapp.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class WorkerHomeViewModel(
    private val repository: WorkerRepository
) : ViewModel() {
    private val _reports: MutableStateFlow<UiState<List<Report>>> =
        MutableStateFlow(UiState.Loading)
    val reports: MutableStateFlow<UiState<List<Report>>> get() = _reports


    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var sortByDate: Boolean = false
    var distanceLimit: Int = 10
    var errorMessage: String? = null

    suspend fun getReports(
        sortByDate: Boolean = false,
        distanceLimit: Int = 10,
    ) {
        _reports.value = UiState.Loading
        return withContext(Dispatchers.IO) {
            when (val response =
                repository.getReports(
                    sortByDate = sortByDate,
                    latitude = latitude,
                    longitude = longitude,
                    distanceLimit = distanceLimit,
                    statusName = ReportStatus.VERIFIED.name,
                )) {
                is Response.Success -> {
                    _reports.value = UiState.Success(response.response.data)
                }

                is Response.Error -> {
                    errorMessage = response.errorMessage
                    _reports.value = UiState.Error(response.errorMessage)
                }

                is Response.NetworkError -> {
                    errorMessage = "Network Error"
                }
            }
        }
    }
}