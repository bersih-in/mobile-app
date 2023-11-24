package com.bersihin.mobileapp.preferences.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(
    private val pref: AuthPreferences
) : ViewModel() {
    fun getAuthToken() = pref.getAuthToken()

    fun saveAuthInfo(authToken: String, isWorker: Boolean) {
        viewModelScope.launch {
            pref.saveAuthInfo(authToken, isWorker)
        }
    }

    fun clearAuthInfo() {
        viewModelScope.launch {
            pref.clearAuthInfo()
        }
    }
}