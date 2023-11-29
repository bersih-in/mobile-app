package com.bersihin.mobileapp.ui.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.models.ReportStatus

@Composable
fun StatusReasonDialog(
    message: String,
    status: ReportStatus,
    onDismissRequest: () -> Unit = {}
) {
    val statusMsg = when (status) {
        ReportStatus.PENDING -> stringResource(id = R.string.pending_desc)
        ReportStatus.VERIFIED -> stringResource(id = R.string.verified_desc)
        ReportStatus.REJECTED_BY_ADMIN -> stringResource(id = R.string.rejected_by_admin_desc)
        ReportStatus.REJECTED_BY_WORKER -> stringResource(id = R.string.rejected_by_worker_desc)
        ReportStatus.REJECTED_BY_SYSTEM -> stringResource(id = R.string.rejected_by_ml_desc)
        ReportStatus.IN_PROGRESS -> stringResource(id = R.string.inprogress_desc)
        ReportStatus.FINISHED -> stringResource(id = R.string.finished_desc)
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
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
                StatusBox(status = status)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = statusMsg, style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 15.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatusReasonDialogPreview() {
    StatusReasonDialog(
        message = "This is a message",
        status = ReportStatus.PENDING
    )
}