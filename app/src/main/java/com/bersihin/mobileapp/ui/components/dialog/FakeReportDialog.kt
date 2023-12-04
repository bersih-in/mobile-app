package com.bersihin.mobileapp.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.ui.components.common.FormField
import com.bersihin.mobileapp.ui.components.common.FormFieldProps
import com.bersihin.mobileapp.ui.pages.general.report_details.ReportDetailsViewModel
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import kotlinx.coroutines.launch

@Composable
fun FakeReportDialog(
    reportId: String = "",
    viewModel: ReportDetailsViewModel? = null,
    onSuccess: () -> Unit = {},
    onFailure: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    val reason = rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
//                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(text = stringResource(id = R.string.fake_report_prompt))

                FormField(
                    props = FormFieldProps(
                        labelId = R.string.reason,
                        placeholderId = R.string.reason_placeholder,
                        value = reason.value,
                        onValueChanged = { reason.value = it.toString() },
                        validator = { it.toString().isNotEmpty() },
                        errorMessageId = R.string.reason_invalid,
                        textFieldHeight = 300,
                        singleLine = false
                    )
                )

                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        scope.launch {
                            val isSuccess = viewModel?.updateReport(
                                reportId = reportId,
                                status = "REJECTED_BY_WORKER",
                                statusReason = reason.value
                            )

                            if (isSuccess == true) {
                                onSuccess()
                            } else {
                                onDismissRequest()
                                onFailure()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onErrorContainer)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.mark_as_fake_report),
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.error
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FakeReportDialogPreview() {
    BersihinTheme {
        FakeReportDialog()
    }
}