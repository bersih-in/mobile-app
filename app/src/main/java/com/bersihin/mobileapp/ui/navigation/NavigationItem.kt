package com.bersihin.mobileapp.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val screen: Screen,
    val icon: ImageVector
)