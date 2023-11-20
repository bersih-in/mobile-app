package com.bersihin.mobileapp.navigation

sealed class Screen(val route: String) {
    object Register : Screen("register")
    object Login : Screen("login")
}