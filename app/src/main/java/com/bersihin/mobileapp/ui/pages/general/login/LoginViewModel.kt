package com.bersihin.mobileapp.ui.pages.general.login

import androidx.lifecycle.ViewModel
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.preferences.auth.AuthPreferences
import com.bersihin.mobileapp.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val pref: AuthPreferences,
    private val repository: AuthRepository
) : ViewModel() {
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    lateinit var authToken: String
    lateinit var userRole: String
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var userEmail: String
    lateinit var userId: String

    var errorMessage: String? = null

    suspend fun login(email: String, password: String): Boolean {
        _isLoading.value = true

        return withContext(Dispatchers.IO) {
            val response = repository.login(email, password)
            _isLoading.value = false

            when (response) {
                is Response.Success -> {
                    authToken = response.response.data.token
                    userRole = response.response.data.role
                    firstName = response.response.data.firstName
                    lastName = response.response.data.lastName
                    userEmail = response.response.data.email
                    userId = response.response.data.id



                    pref.saveAuthInfo(
                        authToken = authToken,
                        userRole = userRole,
                        firstName = firstName,
                        lastName = lastName,
                        email = userEmail,
                        userId = userId
                    )
                    true
                }

                is Response.Error -> {
                    errorMessage = response.errorMessage
                    false
                }

                is Response.NetworkError -> {
                    errorMessage = "Network Error"
                    false
                }
            }
        }


    }


}
