package com.bersihin.mobileapp.preferences.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bersihin.mobileapp.api.services.LoginResponse
import com.bersihin.mobileapp.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AuthViewModel(
    private val pref: AuthPreferences,
    private val repository: AuthRepository
) : ViewModel() {

    private val _authToken = MutableStateFlow<String?>(null)
    private val _userRole = MutableStateFlow<String?>(null)

    val authToken: StateFlow<String?> = _authToken.asStateFlow()
    val userRole: StateFlow<String?> = _userRole.asStateFlow()

    init {
        viewModelScope.launch {
            getAuthToken().collect { token ->
                _authToken.value = token
            }

            getUserRole().collect { role ->
                _userRole.value = role
            }
        }
    }

    private fun getAuthToken() = pref.getAuthToken()
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

    suspend fun checkAuthToken(): Boolean {
        return withContext(Dispatchers.IO) {
            val response = repository.checkAuthToken()

            when (response) {
                is LoginResponse.Success -> true
                else -> false
            }
        }
    }
}