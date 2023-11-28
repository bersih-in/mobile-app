package com.bersihin.mobileapp.ui.pages.general.report_details

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bersihin.mobileapp.ui.common.UiState
import com.bersihin.mobileapp.ui.components.FullscreenLoadingIndicator
import com.bersihin.mobileapp.utils.ViewModelFactory

@Composable
fun ReportDetailsScreen(
    reportId: String, viewModel: ReportDetailsViewModel = viewModel(
        factory = ViewModelFactory(context = LocalContext.current)
    )
) {
    val reportInfo = viewModel.reportInfo.collectAsState(initial = UiState.Loading)

    when (val uiState = reportInfo.value) {
        is UiState.Loading -> {
            viewModel.getReportDetails(reportId)
            FullscreenLoadingIndicator()
        }

        is UiState.Success -> {
            val data = uiState.data

            Column {
                Text(text = data.title)
                Text(text = data.description)
                Text(text = viewModel.address)
            }
        }

        is UiState.Error -> {

        }
    }
}