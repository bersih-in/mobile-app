package com.bersihin.mobileapp.ui.components.common

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bersihin.mobileapp.preferences.settings.SettingsPreferences.Companion.USER_ROLE
import com.bersihin.mobileapp.preferences.settings.SettingsViewModel
import com.bersihin.mobileapp.ui.navigation.NavigationItems
import com.bersihin.mobileapp.utils.ViewModelFactory

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = viewModel(
        factory = ViewModelFactory(LocalContext.current)
    )
) {
    val userRole = settingsViewModel.getPrefValue(USER_ROLE).collectAsState(initial = "")

    NavigationBar(modifier = modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val items =
            if (userRole.value == "WORKER") {
                NavigationItems.workerItems
            } else {
                NavigationItems.userItems
            }

        items.map { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}