package com.example.magpie_pinecone

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun PineconeApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("newUser") { NewUserScreen(navController) }
        composable("viewCampsite") {ViewCampsiteScreen(navController)}
        composable("searchCampsite") {SearchCampsiteScreen(navController)}
    }
}
