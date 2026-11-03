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
import com.example.magpie_pinecone.frontend.entrantScreens.HomeScreen
import com.example.magpie_pinecone.frontend.entrantScreens.SearchCampsiteScreen

enum class EntrantDestination(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int,
) {
    HOME("entrantHome", "Home", R.drawable.ic_home),  // icons are temporary -Albert
    SEARCH("searchCampsite", "Search", R.drawable.ic_home),
}

@Composable
fun EntrantApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

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
                    selected = currentDestination?.hierarchy
                        ?.any { it.route == destination.route } == true,
                    onClick = {
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = EntrantDestination.HOME.route
        ) {
            composable(EntrantDestination.HOME.route) { HomeScreen(navController) }
            composable(EntrantDestination.SEARCH.route) { SearchCampsiteScreen(navController) }
        }
    }
}