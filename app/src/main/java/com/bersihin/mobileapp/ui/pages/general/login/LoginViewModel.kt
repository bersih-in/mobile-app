package com.bersihin.mobileapp.ui.pages.general.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bersihin.mobileapp.api.services.LoginResponse
import com.bersihin.mobileapp.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    lateinit var authToken: String
    lateinit var userRole: String

    var errorMessage: String? = null

    suspend fun login(email: String, password: String): Boolean {
        _isLoading.value = true
        var response: LoginResponse

        return withContext(Dispatchers.IO) {
            response = repository.login(email, password)
            Log.i("LoginViewModel", "login: ${response}")

            _isLoading.value = false
            
            when (response) {
                is LoginResponse.Success -> {
                    authToken = (response as LoginResponse.Success).response.data.token
                    userRole = (response as LoginResponse.Success).response.data.role
                    true
                }

                is LoginResponse.Error -> {
                    errorMessage = (response as LoginResponse.Error).errorMessage
                    false
                }

                is LoginResponse.NetworkError -> {
                    errorMessage = "Network Error"
                    false
                }
            }
        }
    }


}
