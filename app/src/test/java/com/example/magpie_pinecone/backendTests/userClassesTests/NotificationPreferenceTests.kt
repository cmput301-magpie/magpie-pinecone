package com.example.magpie_pinecone.backendTests.userClassesTests

import com.example.magpie_pinecone.backend.UserClasses.allowsNotifications
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotificationPreferenceTests {

    @Test
    fun optedInEntrant_canReceiveNotifications() {
        assertTrue(allowsNotifications("Entrant", false))
    }

    @Test
    fun optedOutEntrant_cannotReceiveNotifications() {
        assertFalse(allowsNotifications("Entrant", true))
    }

    @Test
    fun missingPreference_defaultsToEnabled() {
        // Existing entrants may not have saved a preference yet
        assertTrue(allowsNotifications("Entrant", null))
    }

    @Test
    fun owner_cannotReceiveEntrantNotifications() {
        assertFalse(allowsNotifications("Owner", false))
    }

    @Test
    fun admin_cannotReceiveEntrantNotifications() {
        assertFalse(allowsNotifications("Admin", false))
    }

    @Test
    fun missingAccountType_cannotReceiveNotifications() {
        assertFalse(allowsNotifications(null, false))
    }

    @Test
    fun invalidPreference_cannotReceiveNotifications() {
        assertFalse(allowsNotifications("Entrant", "false"))
    }
}