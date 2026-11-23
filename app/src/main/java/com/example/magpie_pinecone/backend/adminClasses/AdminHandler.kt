package com.example.magpie_pinecone.backend.adminClasses

import android.app.Notification
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.backend.UserClasses.UserDB
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB

/**
 * Provides actions for admins when interacting with the app.
 * @constructor Creates an AdminHandler.
 */
class AdminHandler {
    fun browseProfiles(userDB: UserDB): List<User> {
        TODO("Get a list of all users in the database")
    }

    fun removeProfile(userDB: UserDB, user: User) {
        TODO("Remove a user from the database")
    }

    fun browseCampsite(campsiteDB: CampsiteDB): List<Campsite> {
        TODO("Get a list of all campsites in the database")
    }

    fun removeCampsite(campsiteDB: CampsiteDB, campsite: Campsite) {
        TODO("Remove a campsite from the database")
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