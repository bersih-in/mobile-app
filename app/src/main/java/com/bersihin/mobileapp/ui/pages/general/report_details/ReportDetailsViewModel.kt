package com.bersihin.mobileapp.ui.pages.general.report_details

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.repository.WorkerRepository
import com.bersihin.mobileapp.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class ReportDetailsViewModel(
    private val repository: WorkerRepository,
    private val context: Context
) : ViewModel() {
    private val _reportInfo: MutableStateFlow<UiState<Report>> = MutableStateFlow(UiState.Loading)
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val reportInfo: StateFlow<UiState<Report>> get() = _reportInfo
    val isLoading: StateFlow<Boolean> get() = _isLoading

    var address: String = "Unknown Address"

    suspend fun getReportDetails(reportId: String) {
        return withContext(Dispatchers.IO) {
            when (val response = repository.getReportDetails(reportId)) {
                is Response.Success -> {
                    _reportInfo.value = UiState.Success(response.response.data)

                    val geocoder = Geocoder(context)
                    val addressArray = geocoder.getFromLocation(
                        (_reportInfo.value as UiState.Success<Report>).data.latitude,
                        (_reportInfo.value as UiState.Success<Report>).data.longitude,
                        1
                    )

                    if (addressArray != null) {
                        if (addressArray.isNotEmpty()) {
                            address = addressArray[0].getAddressLine(0)
                        }
                    }
                }

                is Response.Error -> {
                    _reportInfo.value = UiState.Error(response.errorMessage)
                }

                is Response.NetworkError -> {
                    _reportInfo.value = UiState.Error("Network Error")
                }
            }
        }
    }

    suspend fun updateReport(reportId: String, status: String, statusReason: String): Boolean {
        _isLoading.value = true

        return try {
            withContext(Dispatchers.IO) {
                val response = repository.updateReport(reportId, status, statusReason)
                response.success
            }
        } catch (e: Exception) {
            false
        } finally {
            _isLoading.value = false
        }
    }
}