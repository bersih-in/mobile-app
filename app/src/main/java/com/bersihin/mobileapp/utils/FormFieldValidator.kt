package com.bersihin.mobileapp.utils

import android.util.Patterns

object FormFieldValidator {
    fun validateName(name: String): Boolean = name.isNotEmpty()

    fun validateEmail(email: String): Boolean =
        email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun validatePassword(password: String): Boolean {
        val minLength = 8
        val hasAlphanumeric = password.any { it.isLetterOrDigit() }

        return password.length >= minLength && hasAlphanumeric
    }

    fun validateConfirmPassword(confirmPassword: String, password: String): Boolean =
        password == confirmPassword
}