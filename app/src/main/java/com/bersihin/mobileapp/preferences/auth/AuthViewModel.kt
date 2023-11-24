package com.bersihin.mobileapp.preferences.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(
    private val pref: AuthPreferences
) : ViewModel() {
    fun getAuthToken() = pref.getAuthToken()
    fun getUserRole() = pref.getUserRole()

    fun saveAuthInfo(authToken: String, userRole: String) {
        viewModelScope.launch {
            pref.saveAuthInfo(authToken, userRole)
        }
    }

    fun clearAuthInfo() {
        viewModelScope.launch {
            pref.clearAuthInfo()
        }
    }
}