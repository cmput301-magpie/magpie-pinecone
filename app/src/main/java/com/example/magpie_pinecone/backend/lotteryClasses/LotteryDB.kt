package com.example.magpie_pinecone.backend.lotteryClasses


/**
 * Provides actions for interfacing with the lottery database.
 * @property lotteries The list of lotteries
 */
class LotteryDB {
    /**
     * Adds a [lottery] to this lottery database.
     */
    fun addLottery(lottery: Lottery): Unit{}

    /**
     * Deletes the specified [lottery] from this lottery database. Only attempts deletion if the specified lottery currently exists in this lottery database.
     */
    fun deleteLottery(lottery: Lottery): Unit{}

    /**
     * Replaces the specified [lottery]’s details in this lottery database. Only attempts replacement if the specified lottery currently exists in this lottery database.
     */
    fun updateLottery(lottery: Lottery): Unit{}

    /**
     * Returns the details of the specified [lottery]
     * @return The lottery instance.
     */
     fun readLottery(lottery: Lottery): String{
         return ""
     }

}