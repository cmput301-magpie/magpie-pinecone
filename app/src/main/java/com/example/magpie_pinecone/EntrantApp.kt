package com.example.magpie_pinecone

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.magpie_pinecone.frontend.entrantScreens.AppPolicyGuidelinesScreen
import com.example.magpie_pinecone.frontend.entrantScreens.CampsiteDetailsScreen
import com.example.magpie_pinecone.frontend.entrantScreens.CampsiteReserveScreen
import com.example.magpie_pinecone.frontend.entrantScreens.CampsiteResultScreen
import com.example.magpie_pinecone.frontend.entrantScreens.EntrantHomeScreen
import com.example.magpie_pinecone.frontend.entrantScreens.EntrantViewReservationsScreen
import com.example.magpie_pinecone.frontend.entrantScreens.ProfileScreen
import com.example.magpie_pinecone.frontend.entrantScreens.SearchCampsiteScreen
import com.example.magpie_pinecone.frontend.entrantScreens.EditProfileScreen
import kotlinx.serialization.Serializable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.magpie_pinecone.backend.UserClasses.UserDB
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import com.example.magpie_pinecone.backend.entrantClasses.EntrantHandler
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.lotteryClasses.LotteryDB
import com.example.magpie_pinecone.backend.ownerClasses.OwnerHandler
import com.example.magpie_pinecone.backend.entrantClasses.Entrant


/**
 * Provide destinations for the navigation bar at the bottom of the app.
 */
enum class EntrantDestination(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int,
) {
    HOME("entrantHome", "Home", R.drawable.ic_home),  // icons are temporary -Albert
    SEARCH("searchCampsite", "Search", R.drawable.ic_home),
    PROFILE("viewProfile", "Profile", R.drawable.ic_favorite)
}

// Objects to represent routes
@Serializable
object AppPolicyGuidelines
@Serializable
object CampsiteDetails
@Serializable
object CampsiteReserve
@Serializable
object CampsiteResult
@Serializable
object Profile
@Serializable
object EntrantViewReservations

@Serializable
object EditProfile

@Composable
fun EntrantApp(androidId: String, onProfileDeleted: () -> Unit) {
    val navController = rememberNavController()
    var selectedCampsite by remember {
        mutableStateOf<Campsite?>(null)
    }

    val userDB = remember { UserDB() }
    val campsiteDB = remember { CampsiteDB() }
    val lotteryDB = remember { LotteryDB() }
    val entrantHandler = remember { EntrantHandler() }

    var lotteries by remember { mutableStateOf<List<Lottery>>(emptyList())}

    // Tracking the current screen on top of the back stack, if the user navigates Compose redraws and updates the screen.
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    // NavigationSuiteScaffold picks an appropriate nav layout for the screen size.
    // Screen is vertical (i.e. phone screen) -> bottom navbar
    // Screen is horizontal (i.e. tablet screen) -> sidebar
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            EntrantDestination.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(destination.icon),
                            contentDescription = null // label already describes it
                        )
                    },
                    label = { Text(destination.label) },
                    // Highlight the tab if the current screen belongs to it
                    selected = currentDestination?.hierarchy
                        ?.any { it.route == destination.route } == true,
                    onClick = {
                        // Navigate to the correct screen
                        navController.navigate(destination.route) {
                            // Removes screens above Home from the back stack to prevent long back stack
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true  // Saves state of the tab while leaving it.
                            }
                            // Prevents duplicates of the same tab in the backstack when already on it.
                            launchSingleTop = true
                            // Restore the state of the tab (i.e. scroll position)
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        // Area where the screen of the current route is displayed.
        NavHost(
            navController = navController,
            startDestination = EntrantDestination.HOME.route  // The first screen is set to HOME.
        ) {
            composable(EntrantDestination.HOME.route) {
                // Runs every time the home screen is shown
                LaunchedEffect(Unit) {
                    lotteryDB.getLotteriesByEntrant(entrantId = androidId) { lotteries = it }
                }

                EntrantHomeScreen(
                    lotteries = lotteries,
                    onMyReservations = { navController.navigate(route = EntrantViewReservations) } ,
                )
            }

            composable<AppPolicyGuidelines> { AppPolicyGuidelinesScreen({ navController.popBackStack() }) }
            composable(EntrantDestination.SEARCH.route) {
                SearchCampsiteScreen(
                    navController = navController,
                    onCampsiteClick = { campsite ->
                        selectedCampsite = campsite
                        navController.navigate(CampsiteDetails)
                    }
                )
            }
            composable<CampsiteDetails> {
                val campsite = selectedCampsite

                if (campsite == null) {
                    LaunchedEffect(Unit) { navController.popBackStack() }
                } else {
                    var lottery by remember { mutableStateOf<Lottery?>(null) }

                    LaunchedEffect(campsite) {
                        lotteryDB.getLotteriesByOwner(ownerId = campsite.ownerId) {
                            lottery = it.firstOrNull()
                        }
                    }

                    CampsiteDetailsScreen(
                        campsite = campsite,
                        onBack = {
                            if (!navController.popBackStack()) {
                                navController.navigate(EntrantDestination.SEARCH.route)
                            }
                        },
                        onSubmit = {
                            val currentLottery = lottery
                            if (currentLottery != null) {
                                userDB.readEntrant(androidId) { entrant ->
                                    if (entrant != null) {
                                        entrantHandler.joinWaitlist(
                                            currentLottery,
                                            lotteryDB,
                                            entrant
                                        )
                                        navController.popBackStack(
                                            EntrantDestination.HOME.route,
                                            inclusive = false
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
            composable<CampsiteReserve> { CampsiteReserveScreen({ navController.popBackStack() }) }
            composable<CampsiteResult> { CampsiteResultScreen({ navController.popBackStack() }) }
            composable<EntrantViewReservations> { EntrantViewReservationsScreen() }
            composable(EntrantDestination.PROFILE.route) { ProfileScreen(navController, onProfileDeleted) }
            composable<EditProfile> { EditProfileScreen(navController) }
        }
    }
}