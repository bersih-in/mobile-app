package com.bersihin.mobileapp.ui.components.dialog

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.ui.components.common.ImageUploadButton
import com.bersihin.mobileapp.ui.pages.general.report_details.ReportDetailsViewModel
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import kotlinx.coroutines.launch

@Composable
fun FinishReportDialog(
    reportId: String = "",
    viewModel: ReportDetailsViewModel? = null,
    onSuccess: () -> Unit = {},
    onFailure: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    var isUploading by rememberSaveable { mutableStateOf(false) }
    var imageUrl by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    val scope = rememberCoroutineScope()


    Dialog(onDismissRequest = { onDismissRequest() }) {

        Card(
            modifier = Modifier
                .fillMaxWidth(),
//                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.finish_report_prompt))

                Spacer(modifier = Modifier.height(16.dp))

                if (isUploading) {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Uploading image...")
                } else {
                    ImageUploadButton(
                        onUploading = { isUploading = true },
                        onSuccess = {
                            imageUrl = it
                            isUploading = false
                        },
                        onError = {
                            isUploading = false
                            Toast.makeText(
                                context,
                                "Failed to upload image, please try again later!",
                                Toast.LENGTH_SHORT
                            ).show(
                            )
                        }
                    )
                }

                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .height(if (imageUrl.isEmpty()) 0.dp else 300.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        Log.i("FinishReportDialog", "imageUrl: $imageUrl")
                        scope.launch {
                            val isSuccess = viewModel?.updateReport(
                                reportId = reportId,
                                status = "FINISHED",
                                statusReason = imageUrl,
                            )

                            if (isSuccess == true) {
                                onSuccess()
                            } else {
                                onDismissRequest()
                                onFailure()
                            }
                        }

                    },
                    enabled = imageUrl.isNotEmpty()
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.mark_as_finished),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinishReportDialogPreview() {
    BersihinTheme {
        FinishReportDialog()
    }
}