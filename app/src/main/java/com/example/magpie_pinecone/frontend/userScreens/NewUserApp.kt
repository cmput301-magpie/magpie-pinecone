package com.example.magpie_pinecone.frontend.userScreens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class NewUserScreens() {
    Welcome,
    Signup
}

@Composable
fun NewUserApp(onUserSignedUp: () -> Unit) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NewUserScreens.Welcome.name,
        modifier = Modifier
    ) {
        composable(route = NewUserScreens.Welcome.name) {
            WelcomeScreen(onClickGetStarted = {
                navController.navigate(NewUserScreens.Signup.name)
            })
        }
        composable(route = NewUserScreens.Signup.name) {
            SignupScreen(onClickSignUp = onUserSignedUp)
        }
    }
}