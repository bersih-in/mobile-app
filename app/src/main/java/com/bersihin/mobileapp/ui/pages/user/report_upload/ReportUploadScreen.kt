package com.bersihin.mobileapp.ui.pages.user.report_upload

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.api.Response
import com.bersihin.mobileapp.ui.components.common.CameraUploadButton
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
    snackbarHostState: SnackbarHostState? = null,
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
    val uploadImageFailed = stringResource(id = R.string.upload_image_failed)

    val address = viewModel.address.collectAsState()

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
                if (it.toString().length <= 255)
                    description = it.toString()

                updateValid()
            },
            errorMessageId = R.string.description_invalid,
            singleLine = false,
            textFieldHeight = 180,
            maxLength = 255
        ),
    )
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    LaunchedEffect(Unit) {
        if (viewModel.latitude != 0.0 && viewModel.longitude != 0.0) {
            return@LaunchedEffect
        }
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
                viewModel.getAddress(context)
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

            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    "Location: ${address.value}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ElevatedButton(
                    onClick = {
                        navController?.navigate(Screen.LocationPickerScreen.route)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.LocationCity, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.change_location),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))



            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (imageUrl.isNotEmpty()) 400.dp else 10.dp)
                    .padding(vertical = 16.dp)
            )

            if (isUploading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.uploading_image),
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            } else {
                Text(
                    text = if (imageUrl.isNotEmpty()) {
                        "Image successfully uploaded!"
                    } else
                        "Upload image for report proof!",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    CameraUploadButton(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        onUploading = {
                            isUploading = true
                        },
                        onSuccess = {
                            imageUrl = it
                            isUploading = false
                            updateValid()
                        },
                        onError = {
                            scope.launch {
                                snackbarHostState?.showSnackbar(
                                    uploadImageFailed,
                                )
                            }
                        }
                    )


                    ImageUploadButton(
                        modifier = Modifier.fillMaxWidth(0.95f),
                        onUploading = {
                            isUploading = true
                        },
                        onSuccess = {
                            imageUrl = it
                            isUploading = false
                            updateValid()
                        },
                        onError = {
                            scope.launch {
                                snackbarHostState?.showSnackbar(
                                    uploadImageFailed
                                )
                            }
                        }
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
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
                            scope.launch {
                                snackbarHostState?.showSnackbar(
                                    submitSuccess
                                )
                            }
                            Log.i("ReportUploadScreen", "response: $response")
                            navController?.navigate(Screen.UserHome.route) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }

                        } else {
                            scope.launch {
                                snackbarHostState?.showSnackbar(
                                    submitFailed
                                )
                            }
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
                        text = stringResource(id = R.string.submit_report),
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