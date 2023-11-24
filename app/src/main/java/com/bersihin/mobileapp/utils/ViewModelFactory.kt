package com.bersihin.mobileapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bersihin.mobileapp.di.Injection
import com.bersihin.mobileapp.preferences.auth.AuthPreferences
import com.bersihin.mobileapp.preferences.auth.AuthViewModel
import com.bersihin.mobileapp.preferences.auth.dataStore
import com.bersihin.mobileapp.ui.pages.general.login.LoginViewModel
import com.bersihin.mobileapp.ui.pages.general.register.RegisterViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(Injection.provideAuthRepository()) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(Injection.provideAuthRepository()) as T
        } else if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(AuthPreferences.getInstance(context.applicationContext.dataStore)) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}