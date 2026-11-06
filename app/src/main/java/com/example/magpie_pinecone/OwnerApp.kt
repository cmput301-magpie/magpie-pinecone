package com.example.magpie_pinecone

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.lotteryClasses.LotteryDB
import com.example.magpie_pinecone.backend.ownerClasses.OwnerHandler
import com.example.magpie_pinecone.frontend.ownerScreens.DatesBookedScreen
import com.example.magpie_pinecone.frontend.ownerScreens.EditCampsiteScreen
import com.example.magpie_pinecone.frontend.ownerScreens.EditLotteryScreen
import com.example.magpie_pinecone.frontend.ownerScreens.OwnerHomeScreen
import com.example.magpie_pinecone.frontend.ownerScreens.ViewCampsiteScreen
import com.example.magpie_pinecone.frontend.ownerScreens.ViewReservationsScreen
import com.example.magpie_pinecone.frontend.ownerScreens.WaitingListScreen
import kotlinx.serialization.Serializable

/**
 * Provide destinations for the navigation bar at the bottom of the app.
 */
enum class OwnerDestination(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int,
) {
    HOME("ownerHome", "Home", R.drawable.ic_home),  // icons are temporary -Albert
}

// Objects to represent routes
@Serializable
object DatesBooked
@Serializable
object EditCampsite
@Serializable
object EditLottery
@Serializable
object ViewCampsite
@Serializable
object ViewReservations
@Serializable
object WaitingList

@Composable
fun OwnerApp(androidId: String) {
    val navController = rememberNavController()
    val campsiteDB = remember { CampsiteDB() }
    val lotteryDB = remember { LotteryDB() }
    val ownerHandler = remember { OwnerHandler() }

    // null = creating a new campsite, not null = editing existing campsite
    var campsiteToEdit by remember { mutableStateOf<Campsite?>(null) }
    var campsiteToView by remember { mutableStateOf<Campsite?>(null) }
    var campsites by remember { mutableStateOf<List<Campsite>>(emptyList()) }

    var lotteryToEdit by remember { mutableStateOf<Lottery?>(null) }
    var lotteryToView by remember { mutableStateOf<Lottery?>(null)}
    var lotteries by remember { mutableStateOf<List<Lottery>>(emptyList())}

    // Tracking the current screen on top of the back stack, if the user navigates Compose redraws and updates the screen.
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination  // Set the first screen to the current screen (defined as HOME below)

    // NavigationSuiteScaffold picks an appropriate nav layout for the screen size.
    // Screen is vertical (i.e. phone screen) -> bottom navbar
    // Screen is horizontal (i.e. tablet screen) -> sidebar
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            OwnerDestination.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(destination.icon),
                            contentDescription = null  // Label describes it already
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
            startDestination = OwnerDestination.HOME.route  // The first screen is set to HOME.
        ) {
            composable(OwnerDestination.HOME.route) {
                // Runs every time the home screen is shown
                LaunchedEffect(Unit) {
                    lotteryDB.getLotteriesByOwner(ownerId = androidId) { ownerLotteries ->
                        lotteries = ownerLotteries
                        // Draw winners for any lottery whose registration period has ended
                        ownerLotteries.forEach { lotteryDB.drawWinnersIfReady(it) }
                    }
                    campsiteDB.getCampsitesByOwner(ownerId = androidId) { campsites = it }
                }

                OwnerHomeScreen(
                    lotteries = lotteries,
                    campsites = campsites,
                    onEditLottery = { lottery ->
                        lotteryToEdit = lottery
                        navController.navigate(route = EditLottery)
                    },
                    onCreateLottery = {
                        lotteryToEdit = null
                        navController.navigate(route = EditLottery)
                    },
                    onViewWaitlist = { lottery ->
                        lotteryToView = lottery
                        navController.navigate(route = WaitingList)
                    },
                    onCreateCampsite = {
                        campsiteToEdit = null
                        navController.navigate(route = EditCampsite)
                    },
                    onEditCampsite = { campsite ->
                        campsiteToEdit = campsite
                        navController.navigate(route = EditCampsite)
                    },
                    onPreviewCampsite = { campsite ->
                        campsiteToView = campsite
                        navController.navigate(route = ViewCampsite)
                    },
                    onDatesBooked = { navController.navigate(route = DatesBooked) },
                    onViewReservations = { navController.navigate(route = ViewReservations) }
                )
            }
            composable<DatesBooked> { DatesBookedScreen({ navController.popBackStack() }) }
            composable<EditCampsite> {
                val existing = campsiteToEdit
                val isEdit = existing != null

                // remember so the same object is used across recompositions
                val campsite = remember {
                    existing ?: Campsite()
                }

                EditCampsiteScreen(
                    ownerId = androidId,
                    isEdit = isEdit,
                    campsite = campsite,
                    onBack = { navController.popBackStack() },
                    onSubmit = { submitted ->
                        if (isEdit) {
                            ownerHandler.updateCampsiteDetails(campsiteDB, submitted)
                        } else {
                            ownerHandler.registerCampsite(campsiteDB, submitted)
                        }
                        // Navigate directly back to home page, ensuring campsite list gets reloaded
                        // This avoids scenario where if campsite was edited from the ViewCampsiteScreen,
                        // submitting here would pop back to the ViewCampsiteScreen, which holds the
                        // old version of the campsite (unedited)
                        navController.popBackStack(OwnerDestination.HOME.route, inclusive = false)
                    }
                )
            }
            composable<EditLottery> {
                val existing = lotteryToEdit
                val lottery = remember { existing ?: Lottery() }

                EditLotteryScreen(
                    isEdit = existing != null,
                    lottery = lottery,
                    availableCampsites = campsites,
                    onBack = { navController.popBackStack() },
                    onSubmit = { submitted ->
                        if (existing != null) {
                            ownerHandler.updateLottery(lotteryDB, submitted)
                        } else {
                            ownerHandler.createLottery(lotteryDB, submitted)
                        }
                        navController.popBackStack(OwnerDestination.HOME.route, inclusive = false)
                    }
                )
            }
            composable<ViewCampsite> {
                val campsite = campsiteToView
                if (campsite == null) {
                    // Nothing selected, go back
                    LaunchedEffect(Unit) { navController.popBackStack() }
                } else {
                    ViewCampsiteScreen(
                        campsite = campsite,
                        onBack = { navController.popBackStack() },
                        onEdit = {
                            campsiteToEdit = campsite
                            navController.navigate(route = EditCampsite)
                        }
                    )
                }
            }
            composable<ViewReservations> { ViewReservationsScreen() }
            composable<WaitingList> {
                val lottery = lotteryToView
                if (lottery == null) {
                    LaunchedEffect(Unit) { navController.popBackStack() }
                } else {
                    var entrants by remember { mutableStateOf<List<User>>(emptyList()) }
                    LaunchedEffect(lottery.id) {
                        lotteryDB.viewEntrants(lottery.id) { entrants = it }
                    }
                    WaitingListScreen(
                        entrants = entrants,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
