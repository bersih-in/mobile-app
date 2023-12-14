package com.bersihin.mobileapp.preferences.auth

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.preferences.auth.AuthPreferences.Companion.AUTH_TOKEN
import com.bersihin.mobileapp.preferences.auth.AuthPreferences.Companion.USER_ROLE
import com.bersihin.mobileapp.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    //    val userRole: StateFlow<String?> = _userRole.asStateFlow()
    var userRole = ""

    init {
        viewModelScope.launch {
            getPrefValue(AUTH_TOKEN).collect { token ->
                _authToken.value = token
            }

            getPrefValue(USER_ROLE).collect { role ->
                _userRole.value = role
            }
        }
    }

    fun getPrefValue(key: Preferences.Key<String>): Flow<String> {
        return pref.getPrefValue(key)
    }
    

    fun clearAuthInfo() {
        viewModelScope.launch {
            pref.clearAuthInfo()
        }
    }


    suspend fun checkAuthToken(): Boolean {
        return withContext(Dispatchers.IO) {
            when (val response = repository.checkAuthToken()) {
                is Response.Success -> {
                    userRole = response.response.data.role

                    pref.saveAuthInfo(
                        authToken = response.response.data.token,
                        userRole = response.response.data.role,
                        firstName = response.response.data.firstName,
                        lastName = response.response.data.lastName,
                        email = response.response.data.email,
                        userId = response.response.data.id,
                    )
                    true
                }

                else -> false
            }
        }
    }
}