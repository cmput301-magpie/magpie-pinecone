package com.example.magpie_pinecone.backend

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.util.UUID

/**
 * Uploads an image to firebase and returns a url to that image
 */
suspend fun uploadImageToFirebase(imageUri: Uri): Result<String> {
    return try {
        val storageRef = Firebase.storage.reference

        val fileName = "${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child("images/$fileName")

        imageRef.putFile(imageUri).await()

        val downloadUrl = imageRef.downloadUrl.await().toString()

        Result.success(downloadUrl)
    } catch (e: Exception) {
        Result.failure(e)
    }
}