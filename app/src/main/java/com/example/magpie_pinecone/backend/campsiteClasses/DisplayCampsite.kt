package com.example.magpie_pinecone.backend.campsiteClasses

/**
 * Displays campsite details
 *
 * @property campsite Campsite to be displayed
 */

class DisplayCampsite(
    val campsite: Campsite
) {
    /**
     * Displays details of the campsite
     */
    fun displayDetails(): Unit {}

    /**
     * Displays map location of the campsite
     */
    fun displayMapLocation(): Unit {}

    /**
     * Displays available dates of the campsite
     */
    fun displayAvailableDates(): Unit {}

    /**
     * Displays QR Code of the campsite
     */
    fun displayQRCode(): Unit {}

    /**
     * Joins the campsite’s active lottery
     */
    fun joinActiveLottery(): Unit {}
}