package com.bersihin.mobileapp.ui.pages.worker.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.preferences.settings.SettingsPreferences.Companion.FIRST_NAME
import com.bersihin.mobileapp.preferences.settings.SettingsPreferences.Companion.LAST_NAME
import com.bersihin.mobileapp.preferences.settings.SettingsViewModel
import com.bersihin.mobileapp.ui.common.UiState
import com.bersihin.mobileapp.ui.components.common.FullscreenLoadingIndicator
import com.bersihin.mobileapp.ui.components.common.InfoItem
import com.bersihin.mobileapp.ui.components.common.ListMapView
import com.bersihin.mobileapp.ui.components.common.PageHeader
import com.bersihin.mobileapp.ui.components.common.PageHeaderProps
import com.bersihin.mobileapp.ui.components.dialog.FilterDialog
import com.bersihin.mobileapp.ui.components.report.ReportItem
import com.bersihin.mobileapp.ui.components.report.ReportItemProps
import com.bersihin.mobileapp.ui.navigation.Screen
import com.bersihin.mobileapp.utils.ViewModelFactory
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WorkerHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: WorkerHomeViewModel = viewModel(
        factory = ViewModelFactory(context = LocalContext.current)
    ),
    settingsViewModel: SettingsViewModel = viewModel(
        factory = ViewModelFactory(context = LocalContext.current)
    ),
    navController: NavController = NavController(LocalContext.current),
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val reports = viewModel.reports.collectAsState()
    val firstName = settingsViewModel.getPrefValue(FIRST_NAME).collectAsState(initial = null)
    val lastName = settingsViewModel.getPrefValue(LAST_NAME).collectAsState(initial = null)
    val context = LocalContext.current


    var isFilterDialogShown by rememberSaveable { mutableStateOf(false) }
    var sortByDate by rememberSaveable { mutableStateOf(false) }
    var distanceLimit by rememberSaveable { mutableIntStateOf(10) }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                viewModel.latitude = location.latitude
                viewModel.longitude = location.longitude

                scope.launch {
                    viewModel.getReports()
                }
            }
        }
    }

    if (isFilterDialogShown) {
        FilterDialog(
            onDismissRequest = { isFilterDialogShown = false },
            onDropdownClick = {
                sortByDate = it == "Date"
            },
            onSliderChange = {
                distanceLimit = it
            },
            onApply = {
                scope.launch {
                    viewModel.getReports(
                        sortByDate = sortByDate,
                        distanceLimit = distanceLimit
                    )
                }

                isFilterDialogShown = false
            }
        )
    }
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { isFilterDialogShown = true },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.adjust_filters),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (val uiState = reports.value) {
                is UiState.Success -> {
                    if (uiState.data.isNotEmpty()) {
                        ListMapView(reports = uiState.data, modifier = Modifier.fillMaxHeight(0.4f))
                    }
                }

                else -> {}
            }

            SwipeRefresh(
//                modifier = modifier,
                state = rememberSwipeRefreshState(isRefreshing = reports.value is UiState.Loading),
                onRefresh = {
                    scope.launch {
                        viewModel.getReports()
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    PageHeader(
                        props = PageHeaderProps(
                            title = "Hello, ${firstName.value ?: ""} ${lastName.value ?: ""} 👋!",
                            description = stringResource(id = R.string.worker_home_header)
                        )
                    )

                    when (val uiState = reports.value) {
                        is UiState.Loading -> {
                            FullscreenLoadingIndicator()
                        }

                        is UiState.Success -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                if (uiState.data.isEmpty()) {
                                    item {
                                        InfoItem(
                                            emoji = "😔",
                                            title = stringResource(id = R.string.no_reports_found),
                                            description = stringResource(id = R.string.no_reports_found_desc)
                                        )
                                    }
                                } else {
                                    items(uiState.data) {
                                        ReportItem(
                                            props = ReportItemProps(
                                                id = it.id,
                                                title = it.title,
                                                description = it.description,
                                                status = it.status
                                            ),
                                            onClick = {
                                                navController.navigate(
                                                    Screen.ReportDetails.createRoute(
                                                        it.id
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        is UiState.Error -> {
                            InfoItem(
                                emoji = "😔",
                                title = stringResource(id = R.string.couldnt_get_reports),
                                description = stringResource(id = R.string.couldnt_get_reports_desc)
                            )
                        }
                    }
                }
            }
        }
    }
}