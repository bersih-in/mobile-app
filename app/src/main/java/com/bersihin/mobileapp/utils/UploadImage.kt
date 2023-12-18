package com.bersihin.mobileapp.utils

import android.content.Context
import android.net.Uri
import com.bersihin.mobileapp.preferences.settings.SettingsPreferences.Companion.USER_ID
import com.bersihin.mobileapp.preferences.settings.SettingsViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun uploadImage(
    imageUri: Uri,
    context: Context,
    onSuccess: (String) -> Unit,
    onError: () -> Unit
) {
    val storageRef = FirebaseStorage.getInstance().reference
    val imageRef = storageRef.child("images/${imageUri.lastPathSegment}")

    val settingsViewModel = ViewModelFactory(context).create(SettingsViewModel::class.java)

    withContext(Dispatchers.IO) {
        settingsViewModel.getPrefValue(USER_ID).collect {
            val metadata = StorageMetadata.Builder()
                .setCustomMetadata("userID", it)
                .build()

            val uploadTask = imageRef.putFile(imageUri, metadata)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }
            }.addOnFailureListener { _ ->
                onError()
            }
        }
    }
//
//    val uploadTask = imageUri.let { uri ->
//        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
//        inputStream?.let {
//            imageRef.putStream(it)
//        }
//    }
}