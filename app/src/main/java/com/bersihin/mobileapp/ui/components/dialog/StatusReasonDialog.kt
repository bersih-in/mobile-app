package com.bersihin.mobileapp.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.models.ReportStatus
import com.bersihin.mobileapp.models.UserRole
import com.bersihin.mobileapp.ui.components.report.StatusBox

@Composable
fun StatusReasonDialog(
    reason: String,
    status: ReportStatus,
    onDismissRequest: () -> Unit = {},
    userRole: UserRole = UserRole.WORKER
) {

    val statusMsg = when (status) {
        ReportStatus.PENDING -> stringResource(id = R.string.pending_desc)
        ReportStatus.VERIFIED -> "${stringResource(id = R.string.verified_desc)} \n\n ${
            stringResource(
                id = if (userRole == UserRole.WORKER)
                    R.string.verified_desc_worker
                else R.string.verified_desc_user
            )
        }"

        ReportStatus.REJECTED_BY_ADMIN -> stringResource(id = R.string.rejected_by_admin_desc)
        ReportStatus.REJECTED_BY_WORKER -> stringResource(id = R.string.rejected_by_worker_desc)
        ReportStatus.REJECTED_BY_SYSTEM -> stringResource(id = R.string.rejected_by_ml_desc)
        ReportStatus.IN_PROGRESS -> stringResource(id = R.string.inprogress_desc)
        ReportStatus.FINISHED -> stringResource(id = R.string.finished_desc)
    }

    val rejectedStatus = listOf(
        ReportStatus.REJECTED_BY_ADMIN,
        ReportStatus.REJECTED_BY_WORKER,
        ReportStatus.REJECTED_BY_SYSTEM
    )

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (status == ReportStatus.FINISHED) 600.dp else 400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.your_report_status),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 15.sp
                    ),
                    textAlign = TextAlign.Center
                )
                StatusBox(status = status)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = statusMsg,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (status == ReportStatus.FINISHED) {
                    Text(
                        "Proof of clearance: ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 15.sp
                        ),
                    )
                    AsyncImage(
                        model = reason,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .height(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                } else if (rejectedStatus.contains(status)) {
                    Text(
                        "Reason for rejection: ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 15.sp
                        ),
                    )

                    Text(
                        text = reason, style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 18.sp,
                            lineHeight = 15.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatusReasonDialogPreview() {
    StatusReasonDialog(
        reason = "This is a message",
        status = ReportStatus.PENDING
    )
}