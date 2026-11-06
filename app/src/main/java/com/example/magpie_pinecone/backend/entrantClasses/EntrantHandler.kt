package com.example.magpie_pinecone.backend.entrantClasses

import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.backend.UserClasses.UserDB
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.lotteryClasses.LotteryDB
import com.example.magpie_pinecone.backend.reservationClasses.Reservation
import com.example.magpie_pinecone.backend.reservationClasses.ReservationDB
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
     * Takes in parameters campsite, campsiteDB, and rating to allow entrant to rate a campsite
     */
    fun giveCampsiteRating(campsite: Campsite, campsiteDB: CampsiteDB, rating: Int): Unit{
        // TODO: Add campsite ratings
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