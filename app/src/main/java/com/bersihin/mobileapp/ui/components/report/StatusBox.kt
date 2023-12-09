package com.bersihin.mobileapp.ui.components.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import com.bersihin.mobileapp.utils.ReportStatus


@Composable
fun StatusBox(
    status: ReportStatus
) {
    var bgColor =
        when (status) {
            ReportStatus.PENDING -> Color(0xFF949494)
            ReportStatus.VERIFIED -> Color(0xFF00acf7)
            ReportStatus.IN_PROGRESS -> Color(0xFFe8cc00)
            ReportStatus.FINISHED -> Color(0xFF00b803)
            else -> Color(0Xffdb0000)
        }

    Row(
        modifier = Modifier
            .padding(4.dp)
            .clip(
                shape = RoundedCornerShape(18.dp)
            )
            .width(150.dp)
            .height(30.dp)
            .background(color = bgColor),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = when (status) {
                ReportStatus.PENDING -> {
                    stringResource(id = R.string.pending).uppercase()
                }

                ReportStatus.VERIFIED -> {
                    stringResource(id = R.string.verified).uppercase()
                }

                ReportStatus.IN_PROGRESS -> {
                    stringResource(id = R.string.inprogress).uppercase()
                }

                ReportStatus.FINISHED -> {
                    stringResource(id = R.string.finished).uppercase()
                }

                else -> {
                    stringResource(id = R.string.rejected).uppercase()
                }
            },
            style = MaterialTheme.typography.labelLarge.copy(
                color = Color.White,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatusBoxPreview() {
    BersihinTheme {
        Column {
            StatusBox(
                status = ReportStatus.PENDING
            )
            StatusBox(
                status = ReportStatus.FINISHED
            )
            StatusBox(
                status = ReportStatus.VERIFIED
            )
            StatusBox(
                status = ReportStatus.REJECTED_BY_ADMIN
            )
            StatusBox(
                status = ReportStatus.IN_PROGRESS
            )
        }

    }
}