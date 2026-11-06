package com.example.magpie_pinecone.backend.lotteryClasses

import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.entrantClasses.Entrant

import java.util.Date


/**
 * Stores the data associated with a lottery.
 *
 * @property id The unique id of this lottery
 * @property campsites The campsites associated with this lottery.
 * @property waitlist The waitlist associated with this lottery.
 * @property activeStartDate The date that marks the start of the active period of this lottery.
 * @property activeEndDate The date that marks the end of the active period of this lottery.
 * @property registrationStartDate The date that marks the start of the registration period of this lottery.
 * @property registrationEndDate The date that marks the end of the registration period of this lottery.
 * @property bookingStartDate The date that marks the start of the booking period of this lottery.
 * @property bookingEndDate The date that marks the end of the booking period of this lottery.
 * @property numberOfWinners The number of winners that can be selected by this lottery.
 * @property winners The list of winners selected by the lottery.
 * @property waitlistCapacity The capacity of the waitlist associated with this lottery.
 * @constructor Creates a Lottery class
 */

data class Lottery (
    var id: String = "",
    val campsites: MutableList<Campsite> = mutableListOf<Campsite>(),
    val activeStartDate: Date? = null,
    val activeEndDate: Date? = null,
    val registrationStartDate: Date? = null,
    val registrationEndDate: Date? = null,
    val bookingStartDate: Date? = null,
    val bookingEndDate: Date? = null,
    val numberOfWinners: Int = 1,
    val waitlistCapacity: Int = 100,
    var waitlist: MutableList<Entrant> = mutableListOf<Entrant>(),
    var winners: List<Entrant> = listOf<Entrant>(),
) {

    /**
     * Uses the waitlist attribute to generate the random order of winners, then select the first n (n is the number of winners)
     * Updates the winners property to reflect the selection
     */
    fun generateWinners(): List<Entrant>{

        val randomChoice = waitlist.shuffled().take(numberOfWinners)
        winners = randomChoice
        return randomChoice

    }
}