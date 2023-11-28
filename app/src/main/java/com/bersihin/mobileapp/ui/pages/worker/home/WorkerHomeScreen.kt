package com.bersihin.mobileapp.ui.pages.worker.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bersihin.mobileapp.preferences.auth.AuthViewModel
import com.bersihin.mobileapp.ui.common.UiState
import com.bersihin.mobileapp.ui.components.FullscreenLoadingIndicator
import com.bersihin.mobileapp.ui.components.HomeHeader
import com.bersihin.mobileapp.ui.components.HomeHeaderProps
import com.bersihin.mobileapp.ui.components.ReportItem
import com.bersihin.mobileapp.ui.components.ReportItemProps
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
            viewModel.getReports()
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
                            description = "Here are the reports submitted nearby you: "
                        )
                    )
                }
                items(uiState.data) {
                    ReportItem(
                        modifier = Modifier.padding(bottom = 16.dp),
                        props = ReportItemProps(
                            id = it.id,
                            title = it.title,
                            description = it.description,
//                            location = geocoder.getFromLocation(
//                                it.latitude,
//                                it.longitude,
//                                1
//                            )?.get(0)?.getAddressLine(0) ?: "",
                            status = it.status
                        ),
                        onClick = { navController.navigate(Screen.ReportDetails.createRoute(it.id)) }
                    )

                }
            }
        }

        is UiState.Error -> {

        }
    }
}