package com.example.magpie_pinecone

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.magpie_pinecone.frontend.entrantScreens.HomeScreen
import com.example.magpie_pinecone.frontend.entrantScreens.NewUserScreen
import com.example.magpie_pinecone.frontend.entrantScreens.SearchCampsiteScreen
import com.example.magpie_pinecone.frontend.entrantScreens.ViewCampsiteScreen

enum class AppDestinations(  // Destinations remain unfinished -Albert
    val route: String,
    val label: String,
    @DrawableRes val icon: Int,  // Unsure if we should implement icons like this -Albert
) {
    HOME("home", "Home", R.drawable.ic_home),  // All icons are temporary right now -Albert
    NEW_USER("newUser", "New User", R.drawable.ic_home),
    VIEW_CAMPSITE("viewCampsite", "View Campsite", R.drawable.ic_home),
    SEARCH_CAMPSITE("searchCampsite", "Search Campsite", R.drawable.ic_home),
}

@Composable
fun PineconeApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(destination.icon),
                            contentDescription = null // label already describes it -Albert
                        )
                    },
                    label = { Text(destination.label) },
                    selected = currentDestination?.hierarchy
                        ?.any { it.route == destination.route } == true,
                    onClick = {
                        navController.navigate(destination.route) {
                            // Debatable if these options are really necessary -Albert
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = AppDestinations.HOME.route
        ) {
            composable(AppDestinations.HOME.route) { HomeScreen(navController) }
            composable(AppDestinations.NEW_USER.route) { NewUserScreen(navController) }
            composable(AppDestinations.VIEW_CAMPSITE.route) { ViewCampsiteScreen(navController) }
            composable(AppDestinations.SEARCH_CAMPSITE.route) { SearchCampsiteScreen(navController) }
        }
    }
}