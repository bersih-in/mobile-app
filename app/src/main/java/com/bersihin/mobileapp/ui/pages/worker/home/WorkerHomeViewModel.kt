package com.bersihin.mobileapp.ui.pages.worker.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.repository.WorkerRepository
import com.bersihin.mobileapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WorkerHomeViewModel(
    private val repository: WorkerRepository
) : ViewModel() {
    private val _reports: MutableStateFlow<UiState<List<Report>>> =
        MutableStateFlow(UiState.Loading)
    private val _sortType: MutableStateFlow<String> = MutableStateFlow("")

    val reports: MutableStateFlow<UiState<List<Report>>> get() = _reports
    val sortType: MutableStateFlow<String> get() = _sortType

    fun getReports() {
        viewModelScope.launch {
            repository.getReports()
                .catch {
                    _reports.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _reports.value = UiState.Success(it)
                }
        }
    }
}