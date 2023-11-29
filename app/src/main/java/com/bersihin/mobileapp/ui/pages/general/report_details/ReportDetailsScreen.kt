package com.bersihin.mobileapp.ui.pages.general.report_details

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.models.ReportStatus
import com.bersihin.mobileapp.ui.common.UiState
import com.bersihin.mobileapp.ui.components.FullscreenLoadingIndicator
import com.bersihin.mobileapp.ui.theme.BersihinTheme
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

            Log.i("ReportDetailsScreen", "Report: ${data.pictureUrl}")

            ReportDetailsContent(
                props = ReportDetailsContentProps(
                    report = data,
                    address = viewModel.address
                )
            )
        }

        is UiState.Error -> {

        }
    }
}


data class ReportDetailsContentProps(
    val report: Report,
    val address: String,
)

@Composable
fun ReportDetailsContent(
    modifier: Modifier = Modifier,
    props: ReportDetailsContentProps
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = props.report.title,
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = modifier.height(24.dp))

        Text(
            text = props.report.description,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = modifier.height(32.dp))

        Text(
            text = "Location: ${props.address}",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                lineHeight = 24.sp
            )
        )

        AsyncImage(model = props.report.pictureUrl, contentDescription = null)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReportDetailsContentPreview() {
    BersihinTheme {
        ReportDetailsContent(
            props = ReportDetailsContentProps(
                report = Report(
                    id = "1",
                    title = "Spotted Trash Pile at Bandung",
                    description = "Report 1 description",
                    latitude = 0.0,
                    longitude = 0.0,
                    status = ReportStatus.PENDING,
                    pictureUrl = "https://i1.sndcdn.com/artworks-5ApRolfwUlCRJ2v4-bcG9jg-t500x500.jpg"
                ),
                address = "Jalan Jalan"
            )
        )
    }
}