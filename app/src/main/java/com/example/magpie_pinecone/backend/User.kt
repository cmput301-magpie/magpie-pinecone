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
 * @property name The name of this user.
 * @property email The email address of this user.
 * @property phoneNumber The phone number of this user.
 * @property accountType The type of this user (entrant, owner, or admin).
 * @property rulesAccepted Whether this user has confirmed the campsite rules and policies.
 * @constructor Creates a user class.
 */
class User(
    var id: String = "",                                // CHANGE 1: String, not Int
    var name: String = "",                              // CHANGE 2: every field has a default value
    var email: String = "",
    var phoneNumber: String = "",
    var accountType: AccountType = AccountType.Entrant,
    var rulesAccepted: Boolean = false                  // CHANGE 3: new field for US 02.11.01
)