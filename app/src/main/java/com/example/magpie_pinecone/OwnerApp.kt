package com.example.magpie_pinecone

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
fun OwnerApp() {
    val navController = rememberNavController()

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
                OwnerHomeScreen(
                    { navController.navigate(route = EditLottery) },
                    { navController.navigate(route = WaitingList) },
                    { navController.navigate(route = EditCampsite) },
                    { navController.navigate(route = ViewCampsite) },
                    { navController.navigate(route = DatesBooked) },
                    { navController.navigate(route = ViewReservations) })
            }
            composable<DatesBooked> { DatesBookedScreen({ navController.popBackStack() }) }
            composable<EditCampsite> { EditCampsiteScreen({ navController.popBackStack() }) }
            composable<EditLottery> { EditLotteryScreen({ navController.popBackStack() }) }
            composable<ViewCampsite> { ViewCampsiteScreen({ navController.popBackStack() }) }
            composable<ViewReservations> { ViewReservationsScreen() }
            composable<WaitingList> {
                WaitingListScreen({ navController.popBackStack() })
            }
        }
    }
}
