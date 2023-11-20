package com.bersihin.mobileapp.ui.common

sealed class State<out T : Any?> {
    object Loading : State<Nothing>()
    data class Success<out T : Any>(val data: T) : State<T>()
    data class Error(val errorMessage: String) : State<Nothing>()
}