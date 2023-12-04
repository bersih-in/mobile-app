package com.bersihin.mobileapp.ui.pages.general.report_details

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.repository.WorkerRepository
import com.bersihin.mobileapp.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportDetailsViewModel(
    private val repository: WorkerRepository,
    private val context: Context
) : ViewModel() {
    private val _reportInfo: MutableStateFlow<UiState<Report>> = MutableStateFlow(UiState.Loading)
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val reportInfo: StateFlow<UiState<Report>> get() = _reportInfo
    val isLoading: StateFlow<Boolean> get() = _isLoading

    var address: String = ""

    fun getReportDetails(reportId: String) {
        viewModelScope.launch {
            _reportInfo.value = UiState.Loading
            _reportInfo.value = UiState.Success(repository.getReportDetails(reportId))

            val geocoder = Geocoder(context)
            address = geocoder.getFromLocation(
                (_reportInfo.value as UiState.Success<Report>).data.latitude,
                (_reportInfo.value as UiState.Success<Report>).data.longitude,
                1
            )?.get(0)?.getAddressLine(0) ?: ""
        }
    }

    suspend fun updateReport(reportId: String, status: String, statusReason: String): Boolean {
        _isLoading.value = true

        return try {
            withContext(Dispatchers.IO) {
                val response = repository.updateReport(reportId, status, statusReason)
                response.success
            }
        } catch (E: Exception) {
            false
        } finally {
            _isLoading.value = false
        }
    }
}