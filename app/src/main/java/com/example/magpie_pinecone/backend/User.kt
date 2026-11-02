package com.example.magpie_pinecone.backend

enum class AccountType {
    Entrant,
    Owner,
    Admin
}

/**
 * Stores the data associated with a user of the application. Base class for entrants, owners and admins.
 *
 * @property id The ID of this user. The ID is automatically generated using the device ID of the device the user accesses the application through.
 * @property email The email address of this user.
 * @property phoneNumber The phone number of this user.
 * @property accountType The type of this user (entrant, owner, or admin).
 * @constructor Creates a user class.
 */
class User (var id: Int, var email: String, var phoneNumber: String, var accountType: AccountType) {

}