package com.example.magpie_pinecone.backend.campsiteClasses

import android.media.Image

/**
 * Stores the data associated with a campsite.
 *
 * @property ID The ID of this campsite.
 * @property name The name of this campsite.
 * @property location The location of this campsite.
 * @property maxOccupancy The maximum number of guests that can occupy this campsite.
 * @property price The price of renting this campsite for one day.
 * @property rating The rating of this campsite.
 * @property images The images associated with this campsite.
 * @property rulesAndPolicy The rules and policy of this campsite.
 * @property availableDates The dates that are unbooked and are currently available to reserve.
 * @property bookedDates The dates that are booked and are currently unavailable to reserve.
 * @property owner The owner that owns this campsite.
 * @property QRCode The QRCode that links to the campsite details screen associated with this campsite.
 * @property description The description of this campsite
 * @property amenities The amenities of this campsite
 * @property mapLocation The map location of this campsite.
 * @property tags The tags associated with this campsite
 * @constructor Creates a campsite class
 */


class Campsite(
    val ID: Int,
    var name: String,
    var location: Location,
    var maxOccupancy: Int,
    var price: Float,
    var rating: Float,
    var images: Image,
    var rulesAndPolicy: TBD,
    var availableDates: ListOf<Dates>,
    var bookedDates: ListOf<Date>,
    var owner: String,
    var QRCode: QRCode,
    var description: String,
    var amenities: String,
    var mapLocation: TBD,
    var tags: TBD
) {
}