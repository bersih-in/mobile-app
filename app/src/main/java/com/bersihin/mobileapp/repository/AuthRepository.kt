package com.bersihin.mobileapp.repository

import com.bersihin.mobileapp.api.services.AuthService
import com.bersihin.mobileapp.api.services.LoginRequest
import com.bersihin.mobileapp.api.services.RegisterRequest

class AuthRepository(
    private val service: AuthService
) {
    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        role: String,
    ) = service.register(
        RegisterRequest(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            role = role
        )
    )

    suspend fun login(
        email: String,
        password: String,
    ) = service.login(
        LoginRequest(
            email = email,
            password = password
        )
    )
}