package com.bersihin.mobileapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bersihin.mobileapp.di.Injection
import com.bersihin.mobileapp.ui.pages.login.LoginViewModel
import com.bersihin.mobileapp.ui.pages.register.RegisterViewModel

class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(Injection.provideAuthRepository()) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(Injection.provideAuthRepository()) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}