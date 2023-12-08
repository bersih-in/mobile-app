package com.bersihin.mobileapp.ui.pages.user.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.repository.UserRepository
import com.bersihin.mobileapp.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class UserHomeViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private val _reports: MutableStateFlow<UiState<List<Report>>> =
        MutableStateFlow(UiState.Loading)

    val reports: MutableStateFlow<UiState<List<Report>>> get() = _reports

    suspend fun getSelfSubmissions() {
        _reports.value = UiState.Loading

        return withContext(Dispatchers.IO) {
            val response = repository.getSelfSubmissions()

            Log.i("UserHomeViewModel", "getSelfSubmissions: $response")

            when (response) {
                is Response.Success -> {
                    _reports.value = UiState.Success(response.response.data)
                }

                is Response.Error -> {
                    _reports.value = UiState.Error(response.errorMessage)
                }

                is Response.NetworkError -> {
                    _reports.value = UiState.Error("Network Error")
                }
            }
        }
    }
}