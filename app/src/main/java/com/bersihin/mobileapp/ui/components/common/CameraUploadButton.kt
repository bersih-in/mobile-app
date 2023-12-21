package com.bersihin.mobileapp.ui.components.common

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.utils.uploadImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraUploadButton(
    modifier: Modifier = Modifier,
    onUploading: () -> Unit = {},
    onSuccess: (String) -> Unit = {},
    onError: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val permissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )


    var imageUri by rememberSaveable {
        mutableStateOf<Uri?>(null)
    }

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = context.getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            imageUri = Uri.fromFile(this)
        }
    }

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let {
                    onUploading()
                    scope.launch {
                        uploadImage(
                            imageUri = it,
                            context = context,
                            onSuccess = onSuccess,
                            onError = onError
                        )
                    }
                }
            }
        }

    ElevatedButton(
        onClick = {
            val photoFile: File = createImageFile()
            takePictureLauncher.launch(
                FileProvider.getUriForFile(
                    context,
                    context.packageName + ".provider",
                    photoFile
                )
            )
        },
        modifier = modifier
//            .fillMaxWidth()
            .height(50.dp)
    ) {
        Icon(imageVector = Icons.Default.Camera, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.take_picture),
            style = MaterialTheme.typography.labelLarge
        )
    }
}