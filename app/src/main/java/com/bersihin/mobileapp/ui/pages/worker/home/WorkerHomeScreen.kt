package com.bersihin.mobileapp.ui.pages.worker.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.bersihin.mobileapp.preferences.auth.AuthViewModel
import com.bersihin.mobileapp.ui.common.UiState
import com.bersihin.mobileapp.ui.components.common.FullscreenLoadingIndicator
import com.bersihin.mobileapp.ui.components.common.InfoItem
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
import kotlinx.coroutines.launch

@Composable
fun WorkerHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: WorkerHomeViewModel = viewModel(
        factory = ViewModelFactory(context = LocalContext.current)
    ),
    authViewModel: AuthViewModel = viewModel(
        factory = ViewModelFactory(context = LocalContext.current)
    ),
    navController: NavController = NavController(LocalContext.current)
) {
    val reports = viewModel.reports.collectAsState()
    val firstName = authViewModel.getFirstName().collectAsState(initial = null)
    val lastName = authViewModel.getLastName().collectAsState(initial = null)
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

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

    SwipeRefresh(
        modifier = modifier,
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
                .padding(16.dp)
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
                            .padding(bottom = 80.dp)
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
                            item {
                                ElevatedButton(
                                    onClick = { isFilterDialogShown = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp)
                                        .height(50.dp)
                                        .padding(horizontal = 16.dp)

                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Adjust Filters",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
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