package com.bersihin.mobileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bersihin.mobileapp.api.ApiConfig
import com.bersihin.mobileapp.models.UserRole
import com.bersihin.mobileapp.preferences.auth.AuthViewModel
import com.bersihin.mobileapp.ui.components.BottomBar
import com.bersihin.mobileapp.ui.navigation.Screen
import com.bersihin.mobileapp.ui.pages.general.login.LoginScreen
import com.bersihin.mobileapp.ui.pages.general.register.RegisterScreen
import com.bersihin.mobileapp.ui.pages.general.report_details.ReportDetailsScreen
import com.bersihin.mobileapp.ui.pages.general.settings.SettingsScreen
import com.bersihin.mobileapp.ui.pages.user.home.UserHomeScreen
import com.bersihin.mobileapp.ui.pages.user.report_upload.ReportUploadScreen
import com.bersihin.mobileapp.ui.pages.worker.history.HistoryScreen
import com.bersihin.mobileapp.ui.pages.worker.home.WorkerHomeScreen
import com.bersihin.mobileapp.ui.pages.worker.progress.ProgressScreen
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import com.bersihin.mobileapp.utils.ViewModelFactory

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
    authViewModel: AuthViewModel = viewModel(
        factory = ViewModelFactory(LocalContext.current)
    ),
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val snackbarHostState = remember { SnackbarHostState() }
    val isLoading = remember { mutableStateOf(true) }

    val screenWithBottomBar = listOf(
        Screen.UserHome.route,
        Screen.WorkerHome.route,
        Screen.Progress.route,
        Screen.History.route,
        Screen.Settings.route
    )

    val authToken = authViewModel.authToken.collectAsState()

    LaunchedEffect(authToken.value) {
        if (authToken.value == null) {
            navController.navigate(Screen.Login.route)
        } else {
            ApiConfig.setAuthToken(authToken.value as String)
            val isExpired = !authViewModel.checkAuthToken()

            if (isExpired) {
                navController.navigate(Screen.Login.route)
            } else {
                val userRole = authViewModel.userRole
                if (UserRole.valueOf(userRole) == UserRole.USER) {
                    navController.navigate(Screen.UserHome.route)
                } else if (UserRole.valueOf(userRole) == UserRole.WORKER) {
                    navController.navigate(Screen.WorkerHome.route)
                }
            }
        }

        isLoading.value = false
    }

    Scaffold(
        bottomBar = {
            if (screenWithBottomBar.contains(currentRoute)) {
                BottomBar(
                    navController = navController
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = modifier
        ) {
            // general pages
            composable(Screen.Login.route) {
                LoginScreen(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    isAuthLoading = isLoading.value
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    snackbarHostState = snackbarHostState
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(
                route = Screen.ReportDetails.route,
                arguments = listOf(navArgument("reportId") { type = NavType.StringType })
            ) {
                val reportId = it.arguments?.getString("reportId") ?: ""
                ReportDetailsScreen(reportId = reportId, navController = navController)
            }

            // user pages
            composable(Screen.UserHome.route) {
                UserHomeScreen()
            }

            composable(Screen.ReportUpload.route) {
                ReportUploadScreen()
            }

            // worker pages
            composable(Screen.WorkerHome.route) {
                WorkerHomeScreen(
                    navController = navController
                )
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
