package com.bersihin.mobileapp.ui.pages.user.report_upload

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.api.services.SubmitRequest
import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.repository.UserRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class ReportUploadViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    companion object {
        @Volatile
        private var INSTANCE: ReportUploadViewModel? = null

        fun getInstance(): ReportUploadViewModel {
            return INSTANCE ?: synchronized(this) {
                val repository = UserRepository.getInstance()

                ReportUploadViewModel(repository).also {
                    INSTANCE = it
                }
            }
        }
    }

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _address: MutableStateFlow<String> = MutableStateFlow("Unknown Address")
    val isLoading: StateFlow<Boolean> get() = _isLoading
    val address: StateFlow<String> get() = _address

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    fun getAddress(context: Context) {
        val geocoder = Geocoder(context)
        val addressArray = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        )

        if (addressArray != null) {
            if (addressArray.isNotEmpty()) {
                _address.value = addressArray[0].getAddressLine(0)
            }
        }
    }

    fun updateLatLng(latLng: LatLng, context: Context) {
        latitude = latLng.latitude
        longitude = latLng.longitude

        getAddress(context)
    }

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