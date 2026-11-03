package com.example.magpie_pinecone.backend.lotteryClasses

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


/**
 * Provides actions for interfacing with the lottery database.
 * @property lotteries The list of lotteries
 */
class LotteryDB {

    /**
     *
     *
     *
     *
     *
     *
     *
     * DOUBLE CHECK IF LOTTERY NEEDS ID
     */

    private val db = Firebase.firestore     // db is the reference to the database
    private val lotteriesRef = db.collection("lotteries")       // lotteriesRef is the collection of lotteries

    private val lotteries = mutableStateListOf<Lottery>()       // lotteries is the local list of lotteries
    /**
     * init block creates a listener to watch the collection, and update the local list of lotteries
     */
    init{
        lotteriesRef.addSnapshotListener { snapshot, error ->
            if (error != null){
                return@addSnapshotListener
            }

            // empty local list of lotteries
            lotteries.clear()

            snapshot?.documents?.forEach { document ->
                // get the lottery class
                val lottery = document.toObject(Lottery::class.java)

                // asssign the fire store id to the lottery and add it to the local list
                if (lottery != null){
                    lottery.id = document.id
                    lotteries.add(lottery)
                }
            }
        }
    }


    /**
     * Adds a [lottery] to this lottery database.
     */
    fun addLottery(lottery: Lottery): Unit{
        lotteriesRef.add(lottery)
    }

    /**
     * Deletes the specified [lottery] based on firestore id from this lottery database. Only attempts deletion if the specified lottery currently exists in this lottery database.
     */
    fun deleteLottery(lottery: Lottery): Unit{
        lotteriesRef.document(lottery.id).delete()
    }

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