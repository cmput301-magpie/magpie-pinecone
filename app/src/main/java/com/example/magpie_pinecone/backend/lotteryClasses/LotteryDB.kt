package com.example.magpie_pinecone.backend.lotteryClasses


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
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
        val document = lotteriesRef.document()
        lottery.id = document.id

        document.set(lottery)
            .addOnSuccessListener {
                Log.d("LotteryDB", "Lottery added with ID: ${lottery.id}")
            }
            .addOnFailureListener { exception ->
                Log.e("LotteryDB", "Failed to add lottery", exception)
            }
    }

    /**
     * Deletes the specified [lottery] based on Firestore id from this lottery database. Only attempts deletion if the specified lottery currently exists in this lottery database.
     */
    fun deleteLottery(lottery: Lottery): Unit{
        if (lottery.id.isEmpty()) {
            Log.e("LotteryDB", "Cannot delete lottery with empty ID")
            return
        }

        lotteriesRef.document(lottery.id).delete()
            .addOnSuccessListener {
                Log.d("LotteryDB", "Lottery deleted: ${lottery.id}")
            }
            .addOnFailureListener { exception ->
                Log.e("LotteryDB", "Failed to delete lottery", exception)
            }
    }

    /**
     * Replaces the specified old details with [lottery] in this lottery database. Only attempts replacement if the specified lottery currently exists in this lottery database.
     */
    fun updateLottery(lottery: Lottery): Unit{
        if (lottery.id.isEmpty()) {
            Log.e("LotteryDB", "Cannot update lottery with empty ID")
            return
        }

        lotteriesRef.document(lottery.id)
            .set(lottery)
            .addOnSuccessListener {
                Log.d("LotteryDB", "Lottery updated: ${lottery.id}")
            }
    }

    fun readLottery(lotteryId: String, onResult: (Lottery?) -> Unit): Unit {

        lotteriesRef.document(lotteryId).get()
            .addOnSuccessListener { snapshot ->

                // This line turns it back into an actual object
                val lottery = snapshot.toObject<Lottery>()
                lottery?.id = snapshot.id
                onResult(lottery)


            }

            .addOnFailureListener { onResult(null) }
    }

    /**
     * Get all lotteries in the Firestore.
     */
    fun getAllLotteries(onResult: (List<Lottery>) -> Unit) {
        lotteriesRef.get()
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Lottery::class.java)?.also { it.id = doc.id }
                })
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    /**
     * Get all lotteries associated with an owner in the Firestore.
     */
    fun getLotteriesByOwner(ownerId: String, onResult: (List<Lottery>) -> Unit) {
        lotteriesRef
            .whereEqualTo("ownerId", ownerId)
            .get()
            .addOnSuccessListener { snapshot ->
                val ownerLotteries = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Lottery::class.java)?.also { it.id = doc.id }
                }
                onResult(ownerLotteries)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    /**
     * Get all lotteries associated with an entrant in the Firestore.
     */
    fun getLotteriesByEntrant(entrantId: String, onResult: (List<Lottery>) -> Unit) {
        // made the waitlist as a subcollection so getting by entrant id is more annoying, need to check every waitlist
        db.collectionGroup("waitlistEntrants")
            .whereEqualTo("id", entrantId)
            .get()
            .addOnSuccessListener { snapshot ->

                // ids of the lotteries that have this entrantid in the waitlist
                val lotteryIds = snapshot.documents.mapNotNull { it.reference.parent.parent?.id }

                if (lotteryIds.isEmpty()) {
                    onResult(emptyList())
                    return@addOnSuccessListener
                }
                lotteriesRef
                    // gets the lottery ids (capped at 30 at one time)
                    .whereIn(FieldPath.documentId(), lotteryIds.take(30))
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val entrantLotteries = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(Lottery::class.java)?.also { it.id = doc.id }
                        }
                        onResult(entrantLotteries)
                    }
                    .addOnFailureListener { onResult(emptyList()) }
            }

            .addOnFailureListener { onResult(emptyList()) }
    }

    fun addEntrant(lottery: Lottery, entrant: Entrant){
//        Makes a waitlist of entrants subcollection then stores (sets) an entrant
        lotteriesRef.document(lottery.id).collection("waitlistEntrants").document(entrant.id).set(entrant)
    }

    fun removeEntrant(lottery: Lottery, entrant: Entrant){
        lotteriesRef.document(lottery.id).collection("waitlistEntrants").document(entrant.id).delete()
    }

    fun viewEntrants(lotteryId: String, onResult: (List<Entrant>) -> Unit): Unit{
        lotteriesRef.document(lotteryId).collection("waitlistEntrants").get()

            .addOnSuccessListener {snapshot ->
                val entrants = snapshot.documents.mapNotNull { document -> document.toObject(Entrant::class.java) }
                onResult(entrants)
            }

            .addOnFailureListener {exception ->
                Log.e("LotteryDB", "Failed to read entrants", exception)
                onResult(emptyList())}

    }

    /**
     * Adds a [lotteryWinner] to the lotteryWinners database.
     */
    fun addLotteryWinner(lotteryWinner: LotteryWinner): Unit{
        lotteryWinnersRef.add(lotteryWinner)
    }

    /**
     * Deletes the specified [lotteryWinner] based on Firestore id from this lottery database. Only attempts deletion if the specified lottery currently exists in this lottery database.
     */
    fun deleteLotteryWinner(lotteryWinner: LotteryWinner): Unit{
        lotteryWinnersRef.document(lotteryWinner.id).delete()
    }
}