package com.example.magpie_pinecone.backend.UserClasses

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Provides actions for interfacing with the user database
 *
 */

class UserDB {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    /**
     * Adds a new [user] to the database.
     * The user's ID is the device ID, so it is used as the document ID.
     */
    fun addUser(user: User) {

        if (user.id.isEmpty()) {
            Log.e("UserDB", "Cannot add user with empty ID")
            return
        }

        usersCollection.document(user.id)
            .set(user)
            .addOnSuccessListener {
                Log.d("UserDB", "User added with ID: ${user.id}")
            }
            .addOnFailureListener { exception ->
                Log.e("UserDB", "Failed to add user", exception)
            }
    }
    /**
     * Deletes a [user] from the database.
     */
    fun deleteUser(user: User) {

        if (user.id.isEmpty()) {
            Log.e("UserDB", "Cannot delete user with empty ID")
            return
        }

        usersCollection.document(user.id)
            .delete()
            .addOnSuccessListener {
                Log.d("UserDB", "User deleted: ${user.id}")
            }
            .addOnFailureListener { exception ->
                Log.e("UserDB", "Failed to delete user", exception)
            }
    }

    /**
     * Updates a [user] in the database with the details in the given [User]
     */
    fun updateUser(user: User) {

        if (user.id.isEmpty()) {
            Log.e("UserDB", "Cannot update user with empty ID")
            return
        }

        usersCollection.document(user.id)
            .set(user)
            .addOnSuccessListener {
                Log.d("UserDB", "User updated: ${user.id}")
            }
            .addOnFailureListener { exception ->
                Log.e("UserDB", "Failed to update user", exception)
            }
    }

    /**
     * Sets the rulesAccepted field of a [user] to the given [accepted] value
     */
    fun setRulesAccepted(userID: String, accepted: Boolean) {

        if (userID.isEmpty()) {
            Log.e("UserDB", "Cannot set rulesAccepted with empty ID")
            return
        }

        usersCollection.document(userID)
            .update("rulesAccepted", accepted)
            .addOnSuccessListener {
                Log.d("UserDB", "rulesAccepted set to $accepted for: $userID")
            }
            .addOnFailureListener { exception ->
                Log.e("UserDB", "Failed to set rulesAccepted", exception)
            }
    }

    /**
     * Fetch a [user] from the database
     */
    fun readUser(
        userID: String,
        onResult: (User?) -> Unit
    ) {
        val document = usersCollection.document(userID)

        document.get()
            .addOnSuccessListener { snapshot ->

                if (snapshot.exists()) {
                    val user = snapshot.toObject(User::class.java)
                    onResult(user)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("UserDB", "Failed to read user", exception)
                onResult(null)
            }
    }
}