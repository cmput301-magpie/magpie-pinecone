package com.example.magpie_pinecone.backend.entrantClasses

import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.backend.UserClasses.UserDB
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.lotteryClasses.LotteryDB
import com.example.magpie_pinecone.backend.reservationClasses.Reservation
import com.example.magpie_pinecone.backend.reservationClasses.ReservationDB
import com.example.magpie_pinecone.backend.reviewClasses.Review
import com.example.magpie_pinecone.backend.reviewClasses.ReviewDB
import java.util.Calendar
import java.util.Date


/**
 * Provides actions for entrants when interacting with the app
 */
class EntrantHandler {

    /**
     * Takes in parameters lottery, lotteryDB, and entrant to allow entrant to join lottery waitlist
     */
    fun joinWaitlist(lottery: Lottery, lotteryDB: LotteryDB, entrant: Entrant): Unit{
        lotteryDB.addEntrant(lottery, entrant)
    }

    /**
     * Takes in parameters lottery, lotteryDB, and entrant to allow entrant to leave lottery waitlist
     */
    fun leaveWaitlist(lottery: Lottery, lotteryDB: LotteryDB, entrant: Entrant): Unit{
        lotteryDB.removeEntrant(lottery, entrant)
    }

    /**
     * Returns the string of policy and guidelines
     * (is this needed?)
     */
    fun displayGuidelines(): String{
        return "Policy and Guidelines"
    }

    /**
     * Takes in parameters [reservationDB], [reviewDB], [campsite], [entrant], [rating], [comment],
     * and [onResult] to post/edit [entrant]'s review of [campsite].
     *
     * Requires rating between 1 and 5 (inclusive), non-blank text, and a reservation for the campsite.
     * [onResult] is called with true if the review was saved, false if any requirement listed above
     * failed or the write failed.
     */
    fun postReview(
        reservationDB: ReservationDB,
        reviewDB: ReviewDB,
        campsite: Campsite,
        entrant: Entrant,
        rating: Int,
        comment: String,
        onResult: (Boolean) -> Unit
    ) {
        val text = comment.trim()

        // Fail if rating is invalid (not between 1 and 5 inclusive)
        if (rating !in 1..5 || text.isEmpty()) {
            onResult(false)
            return
        }

        reservationDB.hasReservedCampsite(entrant.id, campsite.ID) { reserved ->
            if (!reserved) {
                onResult(false)
                return@hasReservedCampsite
            }

            reviewDB.submitReview(
                Review(
                    campsiteID = campsite.ID,
                    entrantID = entrant.id,
                    entrantName = entrant.name.ifBlank { "Anonymous" },
                    rating = rating,
                    comment = text
                ),
                onResult
            )
        }
    }

    /**
     * Takes in parameters userDB and updatedUser to allow entrant to update their profile
     */
    fun updateProfile(userDB: UserDB, updatedUser: User): Unit{
        userDB.updateUser(updatedUser)
    }

    /**
     * Takes in parameters userDB and user to allow entrant to delete their profile
     */
    fun deleteProfile(userDB: UserDB, user: User): Unit{
        userDB.deleteUser(user)
    }

    /**
     * Reserves [campsite] for [entrantId] from [startDate] to [endDate] (both days included).
     *
     * The reservation is only saved if the dates are valid and do not overlap an existing
     * active reservation for the campsite. [onResult] is called with true if the reservation
     * was saved, or false if it was rejected or the write failed.
     */
    fun reserveCampsite(
        reservationDB: ReservationDB,
        campsite: Campsite,
        entrantId: String,
        startDate: Date,
        endDate: Date,
        numGuests: Int = 1,
        onResult: (Boolean) -> Unit
    ) {
        val start = startOfDay(startDate)
        val end = startOfDay(endDate)

        if (campsite.ID.isEmpty() || entrantId.isEmpty() || end.before(start)) {
            onResult(false)
            return
        }

        // Check if there were two simultaneous bookings of the campsite by chance, fail to reserve if there is.
        reservationDB.getActiveReservationsByCampsite(campsite.ID) { existing ->
            val overlaps = existing.any {
                !start.after(startOfDay(it.endDate)) && !startOfDay(it.startDate).after(end)
            }

            if (overlaps) {
                onResult(false)
                return@getActiveReservationsByCampsite
            }

            reservationDB.addReservation(
                Reservation(
                    entrantID = entrantId,
                    ownerID = campsite.ownerId,
                    campsiteID = campsite.ID,
                    startDate = start,
                    endDate = endOfDay(end),  // Last day stays "active" until it is over
                    numGuests = numGuests,
                    termsAndConditions = true,  // The Reserve button is only enabled after the rules are accepted
                    isCancelled = false
                ),
                onResult
            )
        }
    }

    // Returns midnight (device time zone) at the start of the day of the given date
    private fun startOfDay(date: Date): Date =
        Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

    // Returns the last millisecond of the day of the given date
    private fun endOfDay(date: Date): Date =
        Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.time

    /**
     * Takes in parameters reservation, reservationDB, and entrant to allow entrant to cancel their reservation
     */
    fun cancelReservation(reservation: Reservation, reservationDB: ReservationDB, entrant: Entrant): Unit{
        // TODO: Needs to work with cancel reservations, currently reservation db only has delete reservation
        reservationDB.deleteReservation(reservation)
        entrant.activeReservations.remove(reservation)
    }

    /**
     * Takes in parameters userDB and user to allow entrant to delete their profile
     */
    fun searchCampsites(campsiteDB: CampsiteDB, name: String, location: String, date: Date, guests: Int, onResult: (List<Campsite>) -> Unit): Unit{
        campsiteDB.searchCampsites(name, location, date, guests, onResult)
    }


    fun uploadProfileImage(): Unit{
        // TODO: Make upload image work with userdb or imagedb most likely 
    }

    fun scanQrCode(): Unit{
        // TODO: Make scanning qr code work with qrcode db and classes most likely 
    }
}