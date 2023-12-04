package com.bersihin.mobileapp.ui.pages.worker.home

import androidx.lifecycle.ViewModel
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.models.Report
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
    private val _sortByDate: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val reports: MutableStateFlow<UiState<List<Report>>> get() = _reports
    val sortByDate: MutableStateFlow<Boolean> get() = _sortByDate

    
    var errorMessage: String? = null

    suspend fun getReports() {
        return withContext(Dispatchers.IO) {
            when (val response = repository.getReports(_sortByDate.value)) {
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