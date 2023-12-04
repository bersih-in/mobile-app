package com.bersihin.mobileapp.ui.pages.worker.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.preferences.auth.AuthViewModel
import com.bersihin.mobileapp.ui.common.UiState
import com.bersihin.mobileapp.ui.components.common.FullscreenLoadingIndicator
import com.bersihin.mobileapp.ui.components.common.HomeHeader
import com.bersihin.mobileapp.ui.components.common.HomeHeaderProps
import com.bersihin.mobileapp.ui.components.report.ReportItem
import com.bersihin.mobileapp.ui.components.report.ReportItemProps
import com.bersihin.mobileapp.ui.navigation.Screen
import com.bersihin.mobileapp.utils.ViewModelFactory

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


    when (val uiState = reports.value) {
        is UiState.Loading -> {
            LaunchedEffect(Unit) {
                viewModel.getReports()
            }
            FullscreenLoadingIndicator()
        }

        is UiState.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 80.dp)
            ) {
                item {
                    HomeHeader(
                        props = HomeHeaderProps(
                            firstName = firstName.value ?: "",
                            lastName = lastName.value ?: "",
                            description = stringResource(id = R.string.worker_home_header)
                        )
                    )
                }

                if (uiState.data.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 96.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "😔",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 64.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = stringResource(id = R.string.no_reports_found),
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = stringResource(id = R.string.worker_home_no_reports),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(horizontal = 24.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(uiState.data) {
                        ReportItem(
                            modifier = Modifier.padding(bottom = 16.dp),
                            props = ReportItemProps(
                                id = it.id,
                                title = it.title,
                                description = it.description,
                                status = it.status
                            ),
                            onClick = { navController.navigate(Screen.ReportDetails.createRoute(it.id)) }
                        )

                    }
                }

            }
        }

        is UiState.Error -> {

        }
    }
}