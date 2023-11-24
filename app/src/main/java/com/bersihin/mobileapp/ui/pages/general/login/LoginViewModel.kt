package com.bersihin.mobileapp.ui.pages.general.login

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import com.bersihin.mobileapp.api.responses.AuthResponse
import com.bersihin.mobileapp.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun login(email: String, password: String): String {
        _isLoading.value = true
        var response: AuthResponse

        return try {
            withContext(Dispatchers.IO) {
                response = repository.login(email, password)
                Log.i("LoginViewModel", "login: ${response}")

                if (response.success) "" else response.message
            }
        } catch (e: HttpException) {
            JSONObject(e.response()?.errorBody()?.string()!!).getString("message")
        } finally {
            _isLoading.value = false
        }
    }
}
