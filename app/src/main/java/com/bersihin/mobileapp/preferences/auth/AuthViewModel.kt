package com.bersihin.mobileapp.preferences.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bersihin.mobileapp.api.services.LoginResponse
import com.bersihin.mobileapp.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AuthViewModel(
    private val pref: AuthPreferences,
    private val repository: AuthRepository
) : ViewModel() {

    private val _authToken = MutableStateFlow<String?>(null)
    private val _userRole = MutableStateFlow<String?>(null)

    val authToken: StateFlow<String?> = _authToken.asStateFlow()

    //    val userRole: StateFlow<String?> = _userRole.asStateFlow()
    var userRole = ""

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

    suspend fun waitForTokenLoad() {
        Log.i("AuthViewModel", "Waiting for token load")
        authToken.first { it != null }
    }

    fun getAuthToken() = pref.getAuthToken()
    fun getUserRole() = pref.getUserRole()

    fun saveAuthInfo(
        authToken: String,
        userRole: String,
        firstName: String,
        lastName: String,
        email: String
    ) {
        viewModelScope.launch {
            pref.saveAuthInfo(
                authToken = authToken,
                userRole = userRole,
                firstName = firstName,
                lastName = lastName,
                email = email
            )
        }
    }

    fun clearAuthInfo() {
        viewModelScope.launch {
            pref.clearAuthInfo()
        }
    }


    suspend fun checkAuthToken(): Boolean {
        return withContext(Dispatchers.IO) {
            when (val response = repository.checkAuthToken()) {
                is LoginResponse.Success -> {
                    userRole = response.response.data.role

                    pref.saveAuthInfo(
                        authToken = response.response.data.token,
                        userRole = response.response.data.role,
                        firstName = response.response.data.firstName,
                        lastName = response.response.data.lastName,
                        email = response.response.data.email
                    )
                    true
                }

                else -> false
            }
        }
    }
}