package com.example.magpie_pinecone.backend.lotteryClasses

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


/**
 * Provides actions for interfacing with the lottery database and the lottery winners.
 * @property lotteries The list of lotteries
 */
class LotteryDB {


    private val db = Firebase.firestore     // db is the reference to the database
    private val lotteriesRef = db.collection("lotteries")       // lotteriesRef is the collection of lotteries

    private val lotteries = mutableStateListOf<Lottery>()       // lotteries is the local list of lotteries

    private val lotteryWinnersRef = db.collection("lotteryWinners")
    private val lotteryWinners = mutableStateListOf<LotteryWinner>()

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

                // assign the fire store id to the lottery and add it to the local list
                if (lottery != null){
                    lottery.id = document.id
                    lotteries.add(lottery)
                }
            }
        }

        lotteryWinnersRef.addSnapshotListener { snapshot, error ->
            if (error != null){
                return@addSnapshotListener
            }

            // empty local list of lottery winners
            lotteryWinners.clear()

            snapshot?.documents?.forEach { document ->
                // get the lottery class
                val lotteryWinner = document.toObject(LotteryWinner::class.java)

                // assign the fire store id to the lottery and add it to the local list
                if (lotteryWinner != null){
                    lotteryWinner.id = document.id
                    lotteryWinners.add(lotteryWinner)
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
     * Deletes the specified [lottery] based on Firestore id from this lottery database. Only attempts deletion if the specified lottery currently exists in this lottery database.
     */
    fun deleteLottery(lottery: Lottery): Unit{
        lotteriesRef.document(lottery.id).delete()
    }

    /**
     * Replaces the specified [lottery]’s details in this lottery database. Only attempts replacement if the specified lottery currently exists in this lottery database.
     */
    fun updateLottery(oldLottery: Lottery, updatedLottery: Lottery): Unit{
        lotteriesRef.document(oldLottery.id).set(updatedLottery)
    }

    fun readLottery(lotteryId: String): Lottery {
//        code from https://firebase.google.com/docs/firestore/query-data/get-data

        var lottery = Lottery()

//        .get() doesn't directly give the object or the item, so kotlin has a .toObject to convert back for classes
        lotteriesRef.document(lotteryId).get()
            .addOnSuccessListener { documentSnapshot ->

//                This line turns it back into an actual object
                val result = documentSnapshot.toObject<Lottery>()

//                Making sure return is not null
                if (result != null){
                    lottery = result
                }

            }

        return lottery
    }

    fun addEntrant(lottery: Lottery, entrant: Entrant){
//        Makes a waitlist of entrants subcollection then stores (sets) an entrant
        lotteriesRef.document(lottery.id).collection("waitlistEntrants").document(entrant.id).set(entrant)
    }

    fun removeEntrant(lottery: Lottery, entrant: Entrant){
        lotteriesRef.document(lottery.id).collection("waitlistEntrants").document(entrant.id).delete()
    }

    fun viewEntrants(lotteryId: String): List<Entrant>{
        val lottery = readLottery(lotteryId)

        return lottery.waitlist
    }
}