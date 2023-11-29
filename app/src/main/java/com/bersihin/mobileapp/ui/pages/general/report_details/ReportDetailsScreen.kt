package com.bersihin.mobileapp.ui.pages.general.report_details

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.models.ReportStatus
import com.bersihin.mobileapp.ui.common.UiState
import com.bersihin.mobileapp.ui.components.FullscreenLoadingIndicator
import com.bersihin.mobileapp.ui.components.StatusBox
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import com.bersihin.mobileapp.utils.ViewModelFactory

@Composable
fun ReportDetailsScreen(
    reportId: String, viewModel: ReportDetailsViewModel = viewModel(
        factory = ViewModelFactory(context = LocalContext.current)
    ),
    navController: NavController?
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
                ),
                navController = navController
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
    navController: NavController?,
    props: ReportDetailsContentProps
) {
    Column(modifier = modifier) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { navController?.navigateUp() }
                        .testTag("backButton")
                )
                Text(
                    text = "Go Back"
                )
            }

            Spacer(modifier = modifier.height(24.dp))

            AsyncImage(
                model = props.report.pictureUrl,
                contentDescription = null,
                modifier = modifier
                    .height(300.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
            )

            Spacer(modifier = modifier.height(24.dp))

            Column(
                modifier = modifier
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = props.report.title,
                    style = MaterialTheme.typography.titleLarge,
                )

                Row(
                    modifier = Modifier.padding(vertical = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status: ",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 24.sp
                        )
                    )

                    StatusBox(
                        status = props.report.status
                    )
                }

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

                Spacer(modifier = modifier.height(32.dp))


            }

        }
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
                address = "Jalan Jalan",

                ),
            navController = null
        )
    }
}