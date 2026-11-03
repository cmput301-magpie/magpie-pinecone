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

//enum class AppDestinations(  // Destinations remain unfinished -Albert
//    val route: String,
//    val label: String,
//    @DrawableRes val icon: Int,  // Unsure if we should implement icons like this -Albert
//) {
//    HOME("home", "Home", R.drawable.ic_home),  // All icons are temporary right now -Albert
//    NEW_USER("newUser", "New User", R.drawable.ic_home),
//    VIEW_CAMPSITE("viewCampsite", "View Campsite", R.drawable.ic_home),
//    SEARCH_CAMPSITE("searchCampsite", "Search Campsite", R.drawable.ic_home),
//}

enum class StartState {
    LOADING,
    NONE,
    ENTRANT,
    OWNER,
    ERROR
}

@Composable
fun PineconeApp(androidId: String) {
    var state by rememberSaveable { mutableStateOf(StartState.LOADING) }

    LaunchedEffect(androidId, state) {
        if (state == StartState.LOADING) {
            state = try {
                val doc = Firebase.firestore.collection("users").document(androidId).get().await()
                when (doc.getString("accountType")) {
                    "owner" -> StartState.OWNER
                    "entrant" -> StartState.ENTRANT
                    else -> StartState.NONE
                }
            } catch (e: Exception) {
                StartState.ERROR
            }
        }
    }

    when (state) {
        StartState.LOADING -> Box(Modifier.fillMaxSize())
        StartState.NONE -> TODO("Must implement welcome page and new profile page.")
        StartState.ENTRANT -> EntrantApp()
        StartState.OWNER -> OwnerApp()
        StartState.ERROR -> Column(
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

//    val navController = rememberNavController()
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = backStackEntry?.destination
//
//    NavigationSuiteScaffold(
//        navigationSuiteItems = {
//            AppDestinations.entries.forEach { destination ->
//                item(
//                    icon = {
//                        Icon(
//                            painter = painterResource(destination.icon),
//                            contentDescription = null // label already describes it -Albert
//                        )
//                    },
//                    label = { Text(destination.label) },
//                    selected = currentDestination?.hierarchy
//                        ?.any { it.route == destination.route } == true,
//                    onClick = {
//                        navController.navigate(destination.route) {
//                            // Debatable if these options are really necessary -Albert
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    }
//                )
//            }
//        }
//    ) {
//        NavHost(
//            navController = navController,
//            startDestination = AppDestinations.HOME.route
//        ) {
//            composable(AppDestinations.HOME.route) { HomeScreen(navController) }
//            composable(AppDestinations.NEW_USER.route) { NewUserScreen(navController) }
//            composable(AppDestinations.VIEW_CAMPSITE.route) { ViewCampsiteScreen(navController) }
//            composable(AppDestinations.SEARCH_CAMPSITE.route) { SearchCampsiteScreen(navController) }
//        }
//    }
}