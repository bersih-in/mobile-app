package com.bersihin.mobileapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.UploadFile

object NavigationItems {
    val userItems = listOf(
        NavigationItem(
            title = "Home",
            screen = Screen.UserHome,
            icon = Icons.Default.Home
        ),
        NavigationItem(
            title = "Report",
            screen = Screen.Report,
            icon = Icons.Default.UploadFile
        ),
        NavigationItem(
            title = "Settings",
            screen = Screen.Settings,
            icon = Icons.Default.Settings
        ),
    )

    val workerItems = listOf(
        NavigationItem(
            title = "Home",
            screen = Screen.WorkerHome,
            icon = Icons.Default.Home
        ),
        NavigationItem(
            title = "Progress",
            screen = Screen.Progress,
            icon = Icons.Default.Timelapse
        ),
        NavigationItem(
            title = "History",
            screen = Screen.History,
            icon = Icons.Default.History
        ),
        NavigationItem(
            title = "Settings",
            screen = Screen.Settings,
            icon = Icons.Default.Settings
        ),
    )
}