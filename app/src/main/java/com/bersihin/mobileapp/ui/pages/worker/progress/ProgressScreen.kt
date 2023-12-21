package com.bersihin.mobileapp.ui.pages.worker.progress

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.ui.common.UiState
import com.bersihin.mobileapp.ui.components.common.FullscreenLoadingIndicator
import com.bersihin.mobileapp.ui.components.common.InfoItem
import com.bersihin.mobileapp.ui.components.common.PageHeader
import com.bersihin.mobileapp.ui.components.common.PageHeaderProps
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
fun ProgressScreen(
    modifier: Modifier = Modifier,
    viewModel: ProgressViewModel = viewModel(
        factory = ViewModelFactory(context = LocalContext.current)
    ),
    navController: NavController = NavController(LocalContext.current),
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val reports = viewModel.reports.collectAsState()
    val context = LocalContext.current
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
                    viewModel.getProgress()
                }
            }
        }
    }

    SwipeRefresh(
        modifier = modifier,
        state = rememberSwipeRefreshState(isRefreshing = reports.value is UiState.Loading),
        onRefresh = {
            scope.launch {
                viewModel.getProgress()
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
                    title = "Your Progress 📈",
                    description = stringResource(id = R.string.progress_header)
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
//                            .padding(bottom = 80.dp)
                    ) {
                        if (uiState.data.isEmpty()) {
                            item {
                                InfoItem(
                                    emoji = "😢",
                                    title = stringResource(id = R.string.no_progress),
                                    description = stringResource(id = R.string.no_progress_desc)
                                )
                            }
                        } else {
                            items(uiState.data) {
                                ReportItem(
                                    props = ReportItemProps(
                                        id = it.id,
                                        title = it.title,
                                        description = it.description,
                                        status = it.status,
                                        urgent = it.urgent
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
                        emoji = "🤔",
                        title = stringResource(id = R.string.couldnt_get_reports),
                        description = stringResource(id = R.string.couldnt_get_reports_desc)
                    )
                }
            }
        }
    }
}