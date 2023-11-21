package com.bersihin.mobileapp.di

import com.bersihin.mobileapp.api.ApiConfig
import com.bersihin.mobileapp.api.services.AuthService
import com.bersihin.mobileapp.repository.AuthRepository

object Injection {
    fun provideAuthRepository(): AuthRepository {
        val service = ApiConfig.getService(AuthService::class.java)
        return AuthRepository(service)
    }
}