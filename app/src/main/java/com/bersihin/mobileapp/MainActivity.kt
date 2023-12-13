package com.bersihin.mobileapp

import android.Manifest
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.bersihin.mobileapp.preferences.auth.AuthViewModel
import com.bersihin.mobileapp.ui.components.common.BottomBar
import com.bersihin.mobileapp.ui.navigation.Screen
import com.bersihin.mobileapp.ui.pages.general.loading.LoadingScreen
import com.bersihin.mobileapp.ui.pages.general.location_picker.LocationPickerScreen
import com.bersihin.mobileapp.ui.pages.general.login.LoginScreen
import com.bersihin.mobileapp.ui.pages.general.register.RegisterScreen
import com.bersihin.mobileapp.ui.pages.general.report_details.ReportDetailsScreen
import com.bersihin.mobileapp.ui.pages.general.settings.SettingsScreen
import com.bersihin.mobileapp.ui.pages.general.splash.SplashScreen
import com.bersihin.mobileapp.ui.pages.user.home.UserHomeScreen
import com.bersihin.mobileapp.ui.pages.user.report_upload.ReportUploadScreen
import com.bersihin.mobileapp.ui.pages.worker.history.HistoryScreen
import com.bersihin.mobileapp.ui.pages.worker.home.WorkerHomeScreen
import com.bersihin.mobileapp.ui.pages.worker.progress.ProgressScreen
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import com.bersihin.mobileapp.utils.UserRole
import com.bersihin.mobileapp.utils.ViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

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

@OptIn(ExperimentalPermissionsApi::class)
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
    val scope = rememberCoroutineScope()

    val screenWithBottomBar = listOf(
        Screen.UserHome.route,
        Screen.WorkerHome.route,
        Screen.Progress.route,
        Screen.History.route,
        Screen.Settings.route,
        Screen.ReportUpload.route,
    )

    val authToken = authViewModel.authToken.collectAsState()

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    )

    LaunchedEffect(authToken.value) {
        Log.i("MainActivity", "authToken.value: ${authToken.value}")

        permissionState.launchMultiplePermissionRequest()

        if (authToken.value != null) {
            if (authToken.value == "") {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.LoadingScreen.route) {
                        inclusive = true
                    }
                }
            } else {
                ApiConfig.setAuthToken(authToken.value as String)
                val isExpired = !authViewModel.checkAuthToken()
                val userRole = authViewModel.userRole

                navController.navigate(
                    if (isExpired) {
                        Screen.Login.route
                    } else {
                        if (UserRole.valueOf(userRole) == UserRole.USER) {
                            Screen.UserHome.route
                        } else {
                            Screen.WorkerHome.route
                        }
                    }
                ) {
                    popUpTo(Screen.Login.route) {
                        inclusive = true
                    }
                }
            }
        }

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
            startDestination = Screen.LoadingScreen.route,
            modifier = modifier
        ) {
            // general pages
            composable(Screen.SplashScreen.route) {
                SplashScreen()
            }

            composable(Screen.LoadingScreen.route) {
                LoadingScreen()
            }

            composable(Screen.LocationPickerScreen.route) {
                LocationPickerScreen(
                    navController = navController
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    scope = scope
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    scope = scope
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                    navController = navController,
                    scope = scope
                )
            }
            composable(
                route = Screen.ReportDetails.route,
                arguments = listOf(navArgument("reportId") { type = NavType.StringType })
            ) {
                val reportId = it.arguments?.getString("reportId") ?: ""
                ReportDetailsScreen(
                    reportId = reportId,
                    navController = navController,
                    scope = scope
                )
            }

            // user pages
            composable(Screen.UserHome.route) {
                UserHomeScreen(
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                    navController = navController,
                    scope = scope
                )
            }

            composable(Screen.ReportUpload.route) {
                ReportUploadScreen(
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                    navController = navController,
                    scope = scope
                )
            }

            // worker pages
            composable(Screen.WorkerHome.route) {
                WorkerHomeScreen(
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                    navController = navController,
                    scope = scope
                )
            }
            composable(Screen.Progress.route) {
                ProgressScreen(
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                    navController = navController,
                    scope = scope
                )
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                    navController = navController,
                    scope = scope
                )
            }
        }
    }
}

