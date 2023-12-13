package com.bersihin.mobileapp.ui.navigation

sealed class Screen(val route: String) {
    // general pages
    object Register : Screen("register")
    object Login : Screen("login")
    object ReportDetails : Screen("report/{reportId}") {
        fun createRoute(reportId: String) = "report/$reportId"
    }

    object LoadingScreen : Screen("loading")

    object Settings : Screen("settings")

    // user pages
    object UserHome : Screen("user-home")
    object ReportUpload : Screen("report-upload")


    // worker pages
    object WorkerHome : Screen("worker-home")
    object Progress : Screen("progress")
    object History : Screen("history")

}