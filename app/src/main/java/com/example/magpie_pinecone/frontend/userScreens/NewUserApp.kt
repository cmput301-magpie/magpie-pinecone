package com.example.magpie_pinecone.frontend.userScreens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Screens used during the new-user onboarding flow.
 */
enum class NewUserScreens {
    Welcome,
    Signup
}

/**
 * Controls navigation between the welcome and profile setup screens.
 *
 * @param onUserSignedUp Called after profile setup has been completed.
 */
@Composable
fun NewUserApp(
    onUserSignedUp: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NewUserScreens.Welcome.name
    ) {
        composable(route = NewUserScreens.Welcome.name) {
            WelcomeScreen(
                onClickGetStarted = {
                    navController.navigate(NewUserScreens.Signup.name)
                }
            )
        }

        composable(route = NewUserScreens.Signup.name) {
            SignupScreen(
                onClickSignUp = onUserSignedUp,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}