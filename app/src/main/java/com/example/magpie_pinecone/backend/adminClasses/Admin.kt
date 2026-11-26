package com.example.magpie_pinecone.backend.adminClasses

import com.example.magpie_pinecone.backend.UserClasses.AccountType
import com.example.magpie_pinecone.backend.UserClasses.User


/**
 * Stores the data associated with an admin user of the application.
 *
 * @constructor Creates an Admin class
 */
class Admin(
    id: String = "",
    name: String = "",
    email: String = "",
    phoneNumber: String = "",
): User(id, name, email, phoneNumber, accountType = AccountType.Admin)