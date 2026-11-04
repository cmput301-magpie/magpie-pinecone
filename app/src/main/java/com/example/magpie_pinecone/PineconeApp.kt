package com.example.magpie_pinecone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

/**
 * Indicates the state of the app at startup.
 */
enum class StartState {
    LOADING,
    NONE,
    ENTRANT,
    OWNER,
    ERROR
}

@Composable
fun PineconeApp(androidId: String) {
    var state by rememberSaveable { mutableStateOf(StartState.LOADING) }  // Remember the state of the app

    LaunchedEffect(androidId, state) {
        if (state == StartState.LOADING) {
            state = try {  // Try finding an Android ID match in the Firestore database
                val doc = Firebase.firestore.collection("users").document(androidId).get().await()
                when (doc.getString("accountType")) {
                    "owner" -> StartState.OWNER
                    "entrant" -> StartState.ENTRANT
                    else -> StartState.NONE
                }
            } catch (e: Exception) {  // An error occurred during searching
                StartState.ERROR
            }
        }
    }

    when (state) {
        StartState.LOADING -> Box(Modifier.fillMaxSize())  // Empty screen while awaiting Firestore search
        StartState.NONE -> TODO("Must implement welcome page and new profile page.")
        StartState.ENTRANT -> EntrantApp()
        StartState.OWNER -> OwnerApp()
        StartState.ERROR -> Column(  // Display simple page notifying user of error, with a button to try searching again.
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Couldn't reach the server")
            Button(
                onClick = { state = StartState.LOADING }
            ) {
                Text("Retry")
            }
        }
    }
}