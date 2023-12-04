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
import com.bersihin.mobileapp.ui.pages.general.report_details.ReportDetailsViewModel
import com.bersihin.mobileapp.ui.pages.worker.history.HistoryViewModel
import com.bersihin.mobileapp.ui.pages.worker.home.WorkerHomeViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        
        val authRepository = Injection.provideAuthRepository()
        val workerRepository = Injection.provideWorkerRepository()
        val preferences = AuthPreferences.getInstance(context.applicationContext.dataStore)

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                pref = preferences,
                repository = authRepository
            ) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(
                repository = authRepository
            ) as T
        } else if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(
                pref = preferences,
                repository = authRepository
            ) as T
        } else if (modelClass.isAssignableFrom(WorkerHomeViewModel::class.java)) {
            return WorkerHomeViewModel(
                repository = workerRepository
            ) as T
        } else if (modelClass.isAssignableFrom(ReportDetailsViewModel::class.java)) {
            return ReportDetailsViewModel(
                repository = workerRepository,
                context = context
            ) as T
        } else if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(
                repository = Injection.provideWorkerRepository()
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}