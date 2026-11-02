package com.example.magpie_pinecone.backend.lotteryClasses

import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import java.sql.Time


/**
 * Stores the data associated with a lottery.
 *
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

class Lottery (
    val campsites: List<Campsite>,
    val activeStartDate: Time,
    val activeEndDate: Time,
    val registrationStartDate: Time,
    val registrationEndDate: Time,
    val bookingStartDate: Time,
    val bookingEndDate: Time,
    val numberOfWinners: Int,
    val waitlistCapacity: Int? = null
) {
    val waitlist = listOf<Entrant>()
    val winners = listOf<Entrant>()
}