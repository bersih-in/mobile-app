package com.bersihin.mobileapp.preferences.settings

import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.preferences.settings.SettingsPreferences.Companion.AUTH_TOKEN
import com.bersihin.mobileapp.preferences.settings.SettingsPreferences.Companion.COLOR_MODE
import com.bersihin.mobileapp.preferences.settings.SettingsPreferences.Companion.USER_ROLE
import com.bersihin.mobileapp.repository.AuthRepository
import com.bersihin.mobileapp.utils.ColorMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SettingsViewModel(
    private val pref: SettingsPreferences,
    private val repository: AuthRepository
) : ViewModel() {

    private val _authToken = MutableStateFlow<String?>(null)
    private val _userRole = MutableStateFlow<String?>(null)
    private val _colorMode = MutableStateFlow<String?>(null)

    val authToken: StateFlow<String?> = _authToken.asStateFlow()
    val colorMode: StateFlow<String?> = _colorMode.asStateFlow()

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

            getPrefValue(COLOR_MODE).collect { mode ->
                _colorMode.value = mode
            }
        }
    }

    fun getPrefValue(key: Preferences.Key<String>): Flow<String?> {
        return pref.getPrefValue(key)
    }

    fun setColorMode(colorMode: ColorMode) {
        viewModelScope.launch {
            pref.setColorMode(colorMode)
        }

        _colorMode.value = colorMode.mode
        Log.i("SettingsViewModel", "setColorMode: ${colorMode.mode}")
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