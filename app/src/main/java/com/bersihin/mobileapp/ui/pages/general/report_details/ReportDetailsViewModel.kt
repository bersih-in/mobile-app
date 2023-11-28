package com.bersihin.mobileapp.ui.pages.general.report_details

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.repository.WorkerRepository
import com.bersihin.mobileapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportDetailsViewModel(
    private val repository: WorkerRepository,
    private val context: Context
) : ViewModel() {
    private val _reportInfo: MutableStateFlow<UiState<Report>> = MutableStateFlow(UiState.Loading)

    val reportInfo: StateFlow<UiState<Report>> get() = _reportInfo

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
}