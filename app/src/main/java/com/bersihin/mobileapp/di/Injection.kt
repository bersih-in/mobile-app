package com.bersihin.mobileapp.di

import com.bersihin.mobileapp.api.ApiConfig
import com.bersihin.mobileapp.api.services.AuthService
import com.bersihin.mobileapp.api.services.WorkerService
import com.bersihin.mobileapp.repository.AuthRepository
import com.bersihin.mobileapp.repository.WorkerRepository

object Injection {
    fun provideAuthRepository(): AuthRepository {
        val service = ApiConfig.getService(AuthService::class.java)
        return AuthRepository(service)
    }

    fun provideWorkerRepository(): WorkerRepository {
        val service = ApiConfig.getService(WorkerService::class.java)
        return WorkerRepository(service)
    }
}