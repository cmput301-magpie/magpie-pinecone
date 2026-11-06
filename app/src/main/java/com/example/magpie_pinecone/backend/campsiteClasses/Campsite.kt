package com.example.magpie_pinecone.backend.campsiteClasses
import java.util.Date
import com.google.firebase.firestore.GeoPoint

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
    var ID: String = "",
    var name: String = "",
    var location: String = "",
    var maxOccupancy: Int = 0,
    var price: Double = 0.0,
    var rating: Double = 0.0,
    var imageUrl: String? = null,
    var rulesAndPolicy: String = "",
    var availableDates: List<Date> = emptyList(),
    var bookedDates: List<Date> = emptyList(),
    var ownerId: String = "",
//    var QRCode: QRCode,
    var description: String = "",
//    var amenities: String = "",
    var mapLocation: GeoPoint? = null,
    var tags: List<String> = emptyList()
)