package com.bersihin.mobileapp.utils

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.io.InputStream

fun uploadImage(
    imageUri: Uri,
    context: Context,
    onSuccess: (String) -> Unit,
    onError: () -> Unit
) {
    val storageRef = FirebaseStorage.getInstance().reference
    val imageRef = storageRef.child("images/${imageUri.lastPathSegment}")

    val uploadTask = imageUri.let { uri ->
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        inputStream?.let {
            imageRef.putStream(it)
        }
    }

    uploadTask?.addOnSuccessListener {
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            onSuccess(uri.toString())
        }
    }?.addOnFailureListener { _ ->
        onError()
    }
}