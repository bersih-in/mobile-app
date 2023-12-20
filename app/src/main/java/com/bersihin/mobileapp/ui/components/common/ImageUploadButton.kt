package com.bersihin.mobileapp.ui.components.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bersihin.mobileapp.R
import com.bersihin.mobileapp.utils.uploadImage
import kotlinx.coroutines.launch


@Composable
fun ImageUploadButton(
    modifier: Modifier = Modifier,
    onUploading: () -> Unit = {},
    onSuccess: (String) -> Unit = {},
    onError: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var imageUri by rememberSaveable {
        mutableStateOf<Uri?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                uri?.let {
                    imageUri = it
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
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            launcher.launch(intent)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.upload_image),
            style = MaterialTheme.typography.labelLarge
        )
    }

}