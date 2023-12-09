package com.bersihin.mobileapp.ui.pages.general.register

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bersihin.mobileapp.api.MessageResponse
import com.bersihin.mobileapp.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class RegisterViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    
    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        role: String
    ): Boolean {
        _isLoading.value = true

        return try {
            withContext(Dispatchers.IO) {
                val response: MessageResponse = repository.register(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password,
                    role = role
                )

                Log.i("RegisterViewModel", "register: ${response}")
                response.success
            }
        } catch (e: Exception) {
            Log.e("RegisterViewModel", "register: ${e.message}")
            false
        } finally {
            _isLoading.value = false
        }

    }
}
