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
import com.example.magpie_pinecone.frontend.entrantScreens.EntrantHomeScreen
import com.example.magpie_pinecone.frontend.entrantScreens.SearchCampsiteScreen
import com.example.magpie_pinecone.frontend.entrantScreens.EditProfileScreen
import com.example.magpie_pinecone.frontend.entrantScreens.ProfileScreen

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
}

@Composable
fun EntrantApp(onProfileDeleted: () -> Unit) {
    val navController = rememberNavController()

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
            composable(EntrantDestination.HOME.route) { EntrantHomeScreen(navController) }
            composable(EntrantDestination.SEARCH.route) { SearchCampsiteScreen(navController) }
            composable("profile") { ProfileScreen(navController, onProfileDeleted) }
            composable("editProfile") { EditProfileScreen(navController) }
        }
    }
}