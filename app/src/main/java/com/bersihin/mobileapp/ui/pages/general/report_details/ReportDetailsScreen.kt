package com.bersihin.mobileapp.ui.pages.general.report_details

import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.models.Report
import com.bersihin.mobileapp.preferences.settings.SettingsPreferences.Companion.USER_ROLE
import com.bersihin.mobileapp.preferences.settings.SettingsViewModel
import com.bersihin.mobileapp.ui.common.UiState
import com.bersihin.mobileapp.ui.components.actions.WorkerPickupActions
import com.bersihin.mobileapp.ui.components.actions.WorkerUpdateActions
import com.bersihin.mobileapp.ui.components.common.FullscreenLoadingIndicator
import com.bersihin.mobileapp.ui.components.common.InfoItem
import com.bersihin.mobileapp.ui.components.common.ListMapView
import com.bersihin.mobileapp.ui.components.dialog.FakeReportDialog
import com.bersihin.mobileapp.ui.components.dialog.FinishReportDialog
import com.bersihin.mobileapp.ui.components.dialog.StatusReasonDialog
import com.bersihin.mobileapp.ui.components.report.StatusBox
import com.bersihin.mobileapp.ui.navigation.Screen
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import com.bersihin.mobileapp.utils.ReportStatus
import com.bersihin.mobileapp.utils.UserRole
import com.bersihin.mobileapp.utils.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ReportDetailsScreen(
    reportId: String,
    viewModel: ReportDetailsViewModel = viewModel(
        factory = ViewModelFactory(context = LocalContext.current)
    ),
    settingsViewModel: SettingsViewModel = viewModel(
        factory = ViewModelFactory(context = LocalContext.current)
    ),
    navController: NavController = NavController(LocalContext.current),
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val reportInfo = viewModel.reportInfo.collectAsState(initial = UiState.Loading)
    val userRole = settingsViewModel.getPrefValue(USER_ROLE).collectAsState(initial = null)

    LaunchedEffect(userRole.value) {
        Log.i("ReportDetailsScreen", "User role: ${userRole.value}")
        scope.launch {
            viewModel.getReportDetails(reportId)
        }
    }

    when (val uiState = reportInfo.value) {
        is UiState.Loading -> {
            FullscreenLoadingIndicator()
        }

        is UiState.Success -> {
            val data = uiState.data

            Log.i("ReportDetailsScreen", "Report: ${data.imageUrl}")

            ReportDetailsContent(
                props = ReportDetailsContentProps(
                    report = data,
                    address = viewModel.address
                ),
                viewModel = viewModel,
                navController = navController,
                userRole = UserRole.valueOf(userRole.value ?: "USER")
            )
        }

        is UiState.Error -> {
            InfoItem(
                emoji = "🙏",
                title = stringResource(id = R.string.couldnt_get_reports),
                description = stringResource(id = R.string.couldnt_get_reports_desc)
            )
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
    viewModel: ReportDetailsViewModel?,
    navController: NavController?,
    props: ReportDetailsContentProps,
    userRole: UserRole,
) {
    var showStatusDialog by rememberSaveable { mutableStateOf(false) }
    var showFakeReportDialog by rememberSaveable { mutableStateOf(false) }
    var showFinishedReportDialog by rememberSaveable { mutableStateOf(false) }

    val pickupSuccess = stringResource(id = R.string.pickup_success)
    val pickupFailed = stringResource(id = R.string.pickup_failed)

    val context = LocalContext.current

    if (showStatusDialog) {
        StatusReasonDialog(
            reason = props.report.statusReason ?: "",
            status = props.report.status,
            onDismissRequest = { showStatusDialog = false },
            userRole = userRole
        )
    }

    if (showFakeReportDialog) {
        FakeReportDialog(
            reportId = props.report.id,
            onDismissRequest = { showFakeReportDialog = false },
            onSuccess = {
                navController?.navigateUp()
                Toast.makeText(
                    context,
                    "Successfully updated the report status!",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onFailure = {
                Toast.makeText(
                    context,
                    "Failed to update the report status!",
                    Toast.LENGTH_SHORT
                ).show()
            },
            viewModel = viewModel
        )
    }

    if (showFinishedReportDialog) {
        FinishReportDialog(
            reportId = props.report.id,
            onDismissRequest = { showFinishedReportDialog = false },
            onSuccess = {
                navController?.navigateUp()
                Toast.makeText(
                    context,
                    "Successfully updated the report status!",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onFailure = {
                Toast.makeText(
                    context,
                    "Failed to update the report status!",
                    Toast.LENGTH_SHORT
                ).show()
            },
            viewModel = viewModel
        )
    }

    Column(modifier = modifier) {
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

        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = modifier.height(24.dp))

            AsyncImage(
                model = props.report.imageUrl,
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
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .clickable {
                            showStatusDialog = true
                        },
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
                    text = "Location: ${props.address}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 24.sp
                    )
                )

                Spacer(modifier = modifier.height(8.dp))

                ListMapView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),

                    reports = listOf(props.report)
                )

                Spacer(modifier = modifier.height(32.dp))

                Text(
                    text = props.report.description,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = modifier.height(32.dp))

                if (userRole == UserRole.WORKER) {
                    if (props.report.status == ReportStatus.VERIFIED) {
                        WorkerPickupActions(
                            reportId = props.report.id,
                            viewModel = viewModel,
                            onSuccess = {
                                navController?.navigate(Screen.Progress.route)
                                Toast.makeText(
                                    context,
                                    pickupSuccess,
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    pickupFailed,
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                        )
                    } else if (props.report.status == ReportStatus.IN_PROGRESS) {
                        WorkerUpdateActions(
                            onFakeReportClick = {
                                showFakeReportDialog = true
                            },
                            onFinishedClick = {
                                showFinishedReportDialog = true
                            }
                        )
                    }
                }

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
                    imageUrl = "https://i1.sndcdn.com/artworks-5ApRolfwUlCRJ2v4-bcG9jg-t500x500.jpg"
                ),
                address = "Jalan Jalan",

                ),
            navController = null,
            viewModel = null,
            userRole = UserRole.WORKER
        )
    }
}