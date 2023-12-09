package com.bersihin.mobileapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bersihin.mobileapp.preferences.auth.AuthPreferences
import com.bersihin.mobileapp.preferences.auth.AuthViewModel
import com.bersihin.mobileapp.preferences.auth.dataStore
import com.bersihin.mobileapp.repository.AuthRepository
import com.bersihin.mobileapp.repository.UserRepository
import com.bersihin.mobileapp.repository.WorkerRepository
import com.bersihin.mobileapp.ui.pages.general.login.LoginViewModel
import com.bersihin.mobileapp.ui.pages.general.register.RegisterViewModel
import com.bersihin.mobileapp.ui.pages.general.report_details.ReportDetailsViewModel
import com.bersihin.mobileapp.ui.pages.user.home.UserHomeViewModel
import com.bersihin.mobileapp.ui.pages.user.report_upload.ReportUploadViewModel
import com.bersihin.mobileapp.ui.pages.worker.history.HistoryViewModel
import com.bersihin.mobileapp.ui.pages.worker.home.WorkerHomeViewModel
import com.bersihin.mobileapp.ui.pages.worker.progress.ProgressViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val authRepository = AuthRepository.getInstance()
        val workerRepository = WorkerRepository.getInstance()
        val userRepository = UserRepository.getInstance()
        val preferences = AuthPreferences.getInstance(context.applicationContext.dataStore)

        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                pref = preferences,
                repository = authRepository
            ) as T

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(
                repository = authRepository
            ) as T

            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(
                pref = preferences,
                repository = authRepository
            ) as T

            modelClass.isAssignableFrom(WorkerHomeViewModel::class.java) -> WorkerHomeViewModel(
                repository = workerRepository
            ) as T

            modelClass.isAssignableFrom(ReportDetailsViewModel::class.java) -> ReportDetailsViewModel(
                repository = workerRepository,
                context = context
            ) as T

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> HistoryViewModel(
                repository = workerRepository
            ) as T

            modelClass.isAssignableFrom(ProgressViewModel::class.java) -> ProgressViewModel(
                repository = workerRepository
            ) as T

            modelClass.isAssignableFrom(UserHomeViewModel::class.java) -> UserHomeViewModel(
                repository = userRepository
            ) as T

            modelClass.isAssignableFrom(ReportUploadViewModel::class.java) -> ReportUploadViewModel(
                repository = userRepository
            ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}