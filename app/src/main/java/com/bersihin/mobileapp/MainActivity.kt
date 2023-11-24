package com.bersihin.mobileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bersihin.mobileapp.ui.components.BottomBar
import com.bersihin.mobileapp.ui.navigation.Screen
import com.bersihin.mobileapp.ui.pages.general.login.LoginScreen
import com.bersihin.mobileapp.ui.pages.general.register.RegisterScreen
import com.bersihin.mobileapp.ui.pages.general.settings.SettingsScreen
import com.bersihin.mobileapp.ui.pages.user.home.UserHomeScreen
import com.bersihin.mobileapp.ui.pages.user.report_details.ReportDetailsScreen
import com.bersihin.mobileapp.ui.pages.worker.history.HistoryScreen
import com.bersihin.mobileapp.ui.pages.worker.progress.ProgressScreen
import com.bersihin.mobileapp.ui.theme.BersihinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BersihinTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val screenWithNoBottomBar = listOf(
        Screen.Login.route,
        Screen.Register.route
    )

    Scaffold(
        bottomBar = {
            if (!screenWithNoBottomBar.contains(currentRoute)) {
                BottomBar(
                    navController = navController
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = modifier
        ) {
            // general pages
            composable(Screen.Login.route) {
                LoginScreen(
                    modifier = Modifier.padding(innerPadding)
                ) { navController.navigate(Screen.Register.route) }
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    modifier = Modifier.padding(innerPadding),
                    navigateToLogin = { navController.navigate(Screen.Login.route) }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }

            // user pages
            composable(Screen.UserHome.route) {
                UserHomeScreen()
            }
            composable(Screen.ReportDetails.route) {
                ReportDetailsScreen()
            }
            composable(Screen.ReportUpload.route) {
                ReportDetailsScreen()
            }

            // worker pages
            composable(Screen.WorkerHome.route) {
                UserHomeScreen()
            }
            composable(Screen.Progress.route) {
                ProgressScreen()
            }
            composable(Screen.History.route) {
                HistoryScreen()
            }
        }
    }
}