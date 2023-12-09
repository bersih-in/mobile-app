package com.bersihin.mobileapp.ui.pages.user.report_upload

import androidx.lifecycle.ViewModel
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.api.services.SubmitRequest
import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class ReportUploadViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    suspend fun submitReport(
        title: String,
        description: String,
        imageUrl: String,
    ): Response<Report> {
        _isLoading.value = true

        return try {
            withContext(Dispatchers.IO) {
                val response = repository.submitReport(
                    SubmitRequest(
                        title = title,
                        description = description,
                        imageUrl = imageUrl,
                        lat = latitude,
                        lon = longitude
                    )
                )

                response
            }
        } catch (e: Exception) {
            Response.Error(e.message ?: "Unknown Error")
        } finally {
            _isLoading.value = false
        }
    }
}