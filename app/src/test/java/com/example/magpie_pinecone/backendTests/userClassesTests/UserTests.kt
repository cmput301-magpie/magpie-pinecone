package com.example.magpie_pinecone.backendTests.userClassesTests

import com.example.magpie_pinecone.backend.UserClasses.AccountType
import com.example.magpie_pinecone.backend.UserClasses.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserTests {

    @Test
    fun baseConstructors() {
        val user = User()   // Firestore needs every class object to have base constructors

        // Every field should start with its default value
        assertEquals("", user.id)
        assertEquals("", user.name)
        assertEquals("", user.email)
        assertEquals("", user.phoneNumber)
        assertEquals(AccountType.Entrant, user.accountType)   // default type is Entrant
        assertFalse(user.rulesAccepted)                       // rules not accepted yet
    }

    @Test
    fun valuedConstructors() {
        val user = User(
            id = "abc123",
            name = "Rizzler McRizz",
            email = "Rizz@gmail.com",
            phoneNumber = "780-123-4567",
            accountType = AccountType.Owner,
            rulesAccepted = true
        )

        // Every field should hold the value we passed in
        assertEquals("abc123", user.id)
        assertEquals("Rizzler McRizz", user.name)
        assertEquals("Rizz@gmail.com", user.email)
        assertEquals("780-123-4567", user.phoneNumber)
        assertEquals(AccountType.Owner, user.accountType)
        assertTrue(user.rulesAccepted)
    }

    @Test
    fun user_canBeAdmin() {
        val admin = User(id = "admin1", accountType = AccountType.Admin)

        assertEquals(AccountType.Admin, admin.accountType)
    }

    @Test
    fun user_fieldsCanBeUpdated() {
        val user = User()

        // All fields are "var", so we should be able to change them after creating the user
        user.id = "xyz789"
        user.name = "Drunkard Drunk"
        user.email = "livvy@gmail.com"
        user.phoneNumber = "587-987-6543"
        user.accountType = AccountType.Owner

        assertEquals("xyz789", user.id)
        assertEquals("Drunkard Drunk", user.name)
        assertEquals("livvy@gmail.com", user.email)
        assertEquals("587-987-6543", user.phoneNumber)
        assertEquals(AccountType.Owner, user.accountType)
    }

    @Test
    @Suppress("KotlinConstantConditions")
    fun rulesAccepted_canBeToggled() {
        val user = User()

        // Starts as false
        assertFalse(user.rulesAccepted)

        // User confirms the rules
        user.rulesAccepted = true
        assertTrue(user.rulesAccepted)

        // Set back to false
        user.rulesAccepted = false
        assertFalse(user.rulesAccepted)
    }

    @Test
    fun twoUsers_doNotShareData() {
        val a = User(id = "a")
        val b = User(id = "b")

        // Changing a should not change b
        a.name = "Changed Name"
        a.rulesAccepted = true

        assertEquals("", b.name)
        assertFalse(b.rulesAccepted)
        assertNotEquals(a.id, b.id)
    }

    @Test
    fun accountType_hasThreeTypes() {
        val types = AccountType.entries
        // There should be exactly Entrant, Owner and Admin, in that order
        assertEquals(3, types.size)
        assertEquals(AccountType.Entrant, types[0])
        assertEquals(AccountType.Owner, types[1])
        assertEquals(AccountType.Admin, types[2])
    }
}