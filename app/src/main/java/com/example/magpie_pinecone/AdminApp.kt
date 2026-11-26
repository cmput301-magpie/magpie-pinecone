package com.example.magpie_pinecone

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.magpie_pinecone.frontend.adminScreens.AdminHomeScreen
import com.example.magpie_pinecone.ui.components.ComingSoonScreen

enum class AdminDestination(
    val route: String
) {
    HOME("adminHome"),
    CAMPSITES("browseCampsites"),
    PROFILES("browseProfiles"),
    IMAGES("browseImages"),
    NOTIFICATION_LOG("browseNotifications"),
    MESSAGE_LOG("browseMessages"),
    USE_AS_ENTRANT("adminEntrantView"),
    USE_AS_OWNER("adminOwnerView")
}

@Composable
fun AdminApp(androidId: String) {
    val navController = rememberNavController()

    Scaffold { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AdminDestination.HOME.route,
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            composable(AdminDestination.HOME.route) {
                AdminHomeScreen(
                    onCampsiteBrowse = {
                        navController.navigate(AdminDestination.CAMPSITES.route)
                    },
                    onProfileBrowse = {
                        navController.navigate(AdminDestination.PROFILES.route)
                    },
                    onImageBrowse = {
                        navController.navigate(AdminDestination.IMAGES.route)
                    },
                    onNotificationLog = {
                        navController.navigate(AdminDestination.NOTIFICATION_LOG.route)
                    },
                    onMessageLog = {
                        navController.navigate(AdminDestination.MESSAGE_LOG.route)
                    },
                    onEntrantView = {
                        navController.navigate(AdminDestination.USE_AS_ENTRANT.route)
                    },
                    onOwnerView = {
                        navController.navigate(AdminDestination.USE_AS_OWNER.route)
                    }
                )
            }
            composable(AdminDestination.CAMPSITES.route) {
                ComingSoonScreen(
                    title = "Browse Pinecone Campsites",
                    onBack = {
                        navController.popBackStack()
                    }
                )
                TODO("Create and link CampsiteBrowseScreen")
            }
            composable(AdminDestination.PROFILES.route) {
                ComingSoonScreen(
                    title = "Browse Pinecone Profiles",
                    onBack = {
                        navController.popBackStack()
                    }
                )
                TODO("Create and link ProfileBrowseScreen")
            }
            composable(AdminDestination.IMAGES.route) {
                ComingSoonScreen(
                    title = "Browse Pinecone Images",
                    onBack = {
                        navController.popBackStack()
                    }
                )
                TODO("Create and link ImageBrowseScreen")
            }
            composable(AdminDestination.NOTIFICATION_LOG.route) {
                ComingSoonScreen(
                    title = "Notification Log",
                    onBack = {
                        navController.popBackStack()
                    }
                )
                TODO("Create and link NotificationLogScreen")
            }
            composable(AdminDestination.MESSAGE_LOG.route) {
                ComingSoonScreen(
                    title = "Message Log",
                    onBack = {
                        navController.popBackStack()
                    }
                )
                TODO("Create and link MessageLogScreen")
            }
            composable(AdminDestination.USE_AS_ENTRANT.route) {
                ComingSoonScreen(
                    title = "Entrant View",
                    onBack = {
                        navController.popBackStack()
                    }
                )
                TODO("Create and link EntrantViewScreen")
            }
            composable(AdminDestination.USE_AS_OWNER.route) {
                ComingSoonScreen(
                    title = "Owner View",
                    onBack = {
                        navController.popBackStack()
                    }
                )
                TODO("Create and link OwnerViewScreen")
            }
        }
    }
}