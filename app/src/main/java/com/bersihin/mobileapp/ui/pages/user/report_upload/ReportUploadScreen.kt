package com.bersihin.mobileapp.ui.pages.user.report_upload

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.ui.components.common.FormField
import com.bersihin.mobileapp.ui.components.common.FormFieldProps
import com.bersihin.mobileapp.ui.components.common.ImageUploadButton
import com.bersihin.mobileapp.ui.components.common.PageHeader
import com.bersihin.mobileapp.ui.components.common.PageHeaderProps
import com.bersihin.mobileapp.ui.navigation.Screen
import com.bersihin.mobileapp.ui.theme.BersihinTheme
import com.bersihin.mobileapp.utils.ViewModelFactory
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ReportUploadScreen(
    modifier: Modifier = Modifier,
    viewModel: ReportUploadViewModel = viewModel(
        factory = ViewModelFactory(context = LocalContext.current)
    ),
    navController: NavController? = null,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var imageUrl by rememberSaveable { mutableStateOf("") }

    var isUploading by rememberSaveable { mutableStateOf(false) }
    var isAllValid by rememberSaveable { mutableStateOf(false) }

    val isLoading = viewModel.isLoading.collectAsState()

    val context = LocalContext.current

    val submitSuccess = stringResource(id = R.string.submit_success)
    val submitFailed = stringResource(id = R.string.submit_failed)

    fun updateValid() {
        isAllValid = title.isNotEmpty() && description.isNotEmpty() && imageUrl.isNotEmpty()
    }

    val props: List<FormFieldProps> = listOf(
        FormFieldProps(
            labelId = R.string.title,
            value = title,
            placeholderId = R.string.title_placeholder,
            validator = { it.toString().isNotEmpty() },
            onValueChanged = {
                title = it as String
                updateValid()
            },
            errorMessageId = R.string.title_invalid,
        ),
        FormFieldProps(
            labelId = R.string.description,
            value = description,
            placeholderId = R.string.description_placeholder,
            validator = { it.toString().isNotEmpty() },
            onValueChanged = {
                description = it as String
                updateValid()
            },
            errorMessageId = R.string.description_invalid,
            singleLine = false,
            textFieldHeight = 120,
        ),
    )
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                viewModel.latitude = location.latitude
                viewModel.longitude = location.longitude
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            PageHeader(
                props = PageHeaderProps(
                    title = stringResource(id = R.string.submit_report_here),
                    description = stringResource(id = R.string.submit_report_desc)
                )
            )

            props.forEach { prop ->
                FormField(props = prop)
            }

            if (isUploading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Uploading image...")
            } else {
                ImageUploadButton(
                    modifier = Modifier.padding(top = 16.dp),
                    onUploading = {
                        isUploading = true
                    },
                    onSuccess = {
                        imageUrl = it
                        isUploading = false
                        updateValid()
                    },
                    onError = {
                        Toast.makeText(
                            context,
                            "Failed to upload image; please try again later!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )

                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(if (imageUrl.isEmpty()) 0.dp else 400.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

            }

            ElevatedButton(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .height(50.dp),

                enabled = isAllValid,
                onClick = {
                    scope.launch {
                        val response = viewModel.submitReport(
                            title = title,
                            description = description,
                            imageUrl = imageUrl
                        )

                        if (response is Response.Success) {
                            Toast.makeText(
                                context,
                                submitSuccess,
                                Toast.LENGTH_SHORT
                            ).show()
                            navController?.navigate(Screen.UserHome.route)
                        } else {
                            Toast.makeText(
                                context,
                                submitFailed,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }) {
                if (isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(24.dp)
                            .width(24.dp)
                    )
                } else {
                    Icon(imageVector = Icons.Default.UploadFile, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Submit Report",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportUploadScreenPreview() {
    BersihinTheme {
        ReportUploadScreen()
    }
}