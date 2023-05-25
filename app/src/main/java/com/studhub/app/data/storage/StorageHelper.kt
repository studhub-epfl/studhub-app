package com.studhub.app.data.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.*

class StorageHelper {
    private val storage = FirebaseStorage.getInstance()

    /**
     * Takes a URI, store it and returns the URL of a pic
     */
    suspend fun storePicture(uri: Uri, parentFolder: String = "misc"): String {
        val randomId = UUID.randomUUID().toString()
        val imageRef = storage.reference.child("pictures/$parentFolder/$randomId")

        return try {
            imageRef.putFile(uri).await()
            imageRef.downloadUrl.await().toString()
        } catch (_: Exception) {
            ""
        }
    }
}
