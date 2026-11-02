package com.example.magpie_pinecone.backend.campsiteClasses

/**
 * Provides actions for interfacing with the campsite database
 *
 * @property campsiteRepository List of campsites
 */

class CampsiteDB (
    val campsiteRepository: ListOf<Campsite>
) {
    /**
     * Adds a new [campsite] to the database
     */
    fun addCampsite(campsite: Campsite): Unit {}

    /**
     * Deletes a [campsite] from the database
     */
    fun deleteCampsite(campsite: Campsite): Unit {}

    /**
     * Updates a [campsite] in the database with the details in the given [Campsite]
     */
    fun updateCampsite(campsite: Campsite): Unit {}

    /**
     * Fetch a [campsite] from the database
     */
    fun readCampsite(campsite: Campsite): Unit {}
}