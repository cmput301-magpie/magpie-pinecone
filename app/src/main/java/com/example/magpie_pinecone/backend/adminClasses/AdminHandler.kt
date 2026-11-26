package com.example.magpie_pinecone.backend.adminClasses

import android.app.Notification
import android.util.Log
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.backend.UserClasses.UserDB
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Provides actions for admins when interacting with the app.
 * @constructor Creates an AdminHandler.
 */
class AdminHandler {
    private val db = FirebaseFirestore.getInstance()
    private val campsitesCollection = db.collection("campsites")
    private val usersCollection = db.collection("users")
    private val userDB = UserDB()
    private val campsiteDB = CampsiteDB()

    /**
     * Gets a list of all users in the database
     */
    fun browseProfiles(
        onResult: (List<User>) -> Unit
    ) {
        usersCollection.get()
            .addOnSuccessListener { snapshot ->

                val users = snapshot.documents.mapNotNull { document ->
                    document.toObject(User::class.java)
                }

                onResult(users)
            }
    }

    /**
     * Remove a user from the database
     */
    fun removeProfile(
        user: User,
        onDeleted: () -> Unit = {}
    ) {
        userDB.deleteUser(user, onDeleted)
    }

    /**
     * Gets a list of all campsites in the database
     */
    fun browseCampsites(
        onResult: (List<Campsite>) -> Unit
    ) {
        campsiteDB.getAllCampsites(onResult)
    }

    /**
     * Remove a campsite from the database
     */
    fun removeCampsite(campsite: Campsite) {
        campsiteDB.deleteCampsite(campsite)
    }

    fun browseNotifications(): List<Notification> {
        TODO("Get a list of all notifications in the database")
    }

    fun browseImages(): List<String> {
        TODO("Get a list of all images in the database")
    }

    fun removeImage(imageUrl: String) {
        TODO("Remove an image from the database")
    }
}