package com.bersihin.mobileapp.ui.pages.worker.history

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
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = viewModel(
        factory = ViewModelFactory(context = LocalContext.current)
    ),
    navController: NavController = NavController(LocalContext.current)
) {
    val reports = viewModel.reports.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.getHistory()
        }
    }

    SwipeRefresh(
        modifier = modifier,
        state = rememberSwipeRefreshState(isRefreshing = reports.value is UiState.Loading),
        onRefresh = {
            scope.launch {
                viewModel.getHistory()
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
                    title = stringResource(id = R.string.your_history),
                    description = stringResource(id = R.string.your_history_desc)
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