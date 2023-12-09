package com.bersihin.mobileapp.ui.pages.worker.progress

import androidx.lifecycle.ViewModel
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.repository.WorkerRepository
import com.bersihin.mobileapp.ui.common.UiState
import com.bersihin.mobileapp.utils.ReportStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class ProgressViewModel(
    private val repository: WorkerRepository
) : ViewModel() {
    private val _reports: MutableStateFlow<UiState<List<Report>>> =
        MutableStateFlow(UiState.Loading)

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    val reports: MutableStateFlow<UiState<List<Report>>> get() = _reports
    var errorMessage: String? = null


    suspend fun getProgress() {
        _reports.value = UiState.Loading

        return withContext(Dispatchers.IO) {
            when (val response =
                repository.getReports(
                    latitude = latitude,
                    longitude = longitude,
                    statusName = ReportStatus.IN_PROGRESS.name,
                    bySelf = true
                )) {
                is Response.Success -> {
                    _reports.value = UiState.Success(response.response.data)
                }

                is Response.Error -> {
                    errorMessage = response.errorMessage
                }

                is Response.NetworkError -> {
                    errorMessage = "Network Error"
                }
            }
        }
    }
}