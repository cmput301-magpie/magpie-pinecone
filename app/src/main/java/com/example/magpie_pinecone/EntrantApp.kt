package com.example.magpie_pinecone

import android.provider.Settings
import android.widget.Toast
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
import androidx.compose.runtime.setValue
import com.example.magpie_pinecone.backend.UserClasses.UserDB
import androidx.compose.ui.platform.LocalContext
import com.example.magpie_pinecone.backend.UserClasses.User
import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.campsiteClasses.CampsiteDB
import com.example.magpie_pinecone.backend.reservationClasses.Reservation
import com.example.magpie_pinecone.backend.reservationClasses.ReservationDB
import com.example.magpie_pinecone.backend.entrantClasses.EntrantHandler
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.ownerClasses.OwnerHandler
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.example.magpie_pinecone.backend.lotteryClasses.LotteryDB
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import com.example.magpie_pinecone.frontend.entrantScreens.ScanQRCodeScreen
import com.example.magpie_pinecone.frontend.entrantScreens.SettingsScreen
import com.example.magpie_pinecone.ui.components.ComingSoonScreen
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

/*
 * EntrantApp.kt contains all relevant functions for navigating between entrant screens.
 * Additionally, EntrantApp.kt stores persistent data for use across entrant screens.
 *
 * Composable Functions:
 * EntrantApp handles navigation and data persistence across entrant screens.
 */

/**
 * The 4 tabs of the bottom navigation bar, matching Figma: Home, Search, Settings, Scan QR.
 */
enum class EntrantDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    HOME("entrantHome", "Home", Icons.Filled.Menu),
    SEARCH("searchCampsite", "Search", Icons.Filled.Search),
    SETTINGS("entrantSettings", "Settings", Icons.Filled.Settings),
    SCAN_QR("entrantScanQr", "Scan QR", Icons.Filled.QrCodeScanner)
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

@Serializable
object ComingSoon

@Composable
fun EntrantApp(androidId: String, onProfileDeleted: () -> Unit) {
    val navController = rememberNavController()

    val context = LocalContext.current

    var selectedCampsite by remember {
        mutableStateOf<Campsite?>(null)
    }

    // What the result screen should say (reserved or cancelled)
    var resultTitle by remember { mutableStateOf("Campsite Reserved") }
    var resultMessage by remember { mutableStateOf("") }

    var campsiteToReserve by remember {
        mutableStateOf<Campsite?>(null)
    }

    val userDB = remember { UserDB() }
    var curUser by remember {
        mutableStateOf<User?>(null)
    }

    var curEntrant by remember {
        mutableStateOf<Entrant?>(null)
    }

    LaunchedEffect(Unit) {
        userDB.readUser(androidId) { fetchedUser ->
            curUser = fetchedUser
        }

        userDB.readEntrant(androidId) { fetchedEntrant ->
            curEntrant = fetchedEntrant
        }
    }

    val campsiteDB = remember { CampsiteDB() }
    val lotteryDB = remember { LotteryDB() }
    val reservationDB = remember { ReservationDB() }
    val entrantHandler = remember { EntrantHandler() }

    var lotteries by remember {
        mutableStateOf<List<Lottery>>(emptyList())
    }

    // Tracking the current screen on top of the back stack.
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    // Reservation Variables
    var activeReservations by remember {
        mutableStateOf<List<Reservation>>(emptyList())
    }

    // NavigationSuiteScaffold picks an appropriate nav layout for the screen size.
    // Screen is vertical (i.e. phone screen) -> bottom navbar
    // Screen is horizontal (i.e. tablet screen) -> sidebar
    // Figma shows no nav bar on the Campsite Details and Reserve screens.
    val hideNavBar =
        currentDestination?.hasRoute(CampsiteDetails::class) == true ||
                currentDestination?.hasRoute(CampsiteReserve::class) == true

    NavigationSuiteScaffold(
        layoutType = if (hideNavBar) {
            NavigationSuiteType.None
        } else {
            NavigationSuiteType.NavigationBar
        },
        navigationSuiteItems = {
            EntrantDestination.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(destination.label)
                    },
                    selected = currentDestination?.hierarchy
                        ?.any { it.route == destination.route } == true,
                    onClick = {
                        navController.navigate(destination.route) {
                            popUpTo(EntrantDestination.HOME.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
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
            composable(EntrantDestination.HOME.route) {
                // Runs every time the home screen is shown
                LaunchedEffect(Unit) {
                    lotteryDB.getLotteriesByEntrant(
                        entrantId = androidId
                    ) {
                        lotteries = it
                    }
                }

                EntrantHomeScreen(
                    lotteries = lotteries,
                    onMyReservations = {
                        navController.navigate(
                            route = EntrantViewReservations
                        )
                    },
                    onNotifications = {
                        navController.navigate(ComingSoon)
                    },
                    curEntrant = curEntrant,
                    onLotteryDetails = { lottery ->
                        selectedCampsite = lottery.campsites[0]
                        navController.navigate(CampsiteDetails)
                    },
                    onReserveLottery = { lottery ->
                        campsiteToReserve = lottery.campsites[0]
                        navController.navigate(CampsiteReserve)
                    },
                    onLeaveLottery = { lottery ->
                        if (curEntrant != null) {
                            lotteryDB.removeEntrant(
                                lottery,
                                curEntrant!!
                            )
                        }
                    }
                )
            }

            composable<AppPolicyGuidelines> {
                AppPolicyGuidelinesScreen {
                    navController.popBackStack()
                }
            }

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
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                } else {
                    var lottery by remember {
                        mutableStateOf<Lottery?>(null)
                    }

                    LaunchedEffect(campsite) {
                        lotteryDB.getLotteriesByOwner(
                            ownerId = campsite.ownerId
                        ) {
                            lottery = it.firstOrNull()
                        }
                    }

                    CampsiteDetailsScreen(
                        campsite = campsite,
                        isWinner = false,
                        onReserve = {
                            navController.navigate(CampsiteReserve)
                        },
                        onBack = {
                            if (!navController.popBackStack()) {
                                navController.navigate(
                                    EntrantDestination.SEARCH.route
                                )
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

            composable<CampsiteReserve> {
                CampsiteReserveScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onUserAcceptedRules = {
                        userDB.setRulesAccepted(
                            androidId,
                            true
                        )
                    },
                    campsiteId = campsiteToReserve?.ID ?: "",
                    onReserved = {
                        resultTitle = "Campsite Reserved"
                        resultMessage =
                            "Your reservation was placed successfully!"

                        navController.navigate(CampsiteResult)
                    }
                )
            }

            composable<CampsiteResult> {
                CampsiteResultScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    title = resultTitle,
                    message = resultMessage
                )
            }

            composable<EntrantViewReservations> {
                // Grab active reservations every time view reservations is loaded
                LaunchedEffect(Unit) {
                    reservationDB.getActiveReservationsByEntrant(
                        entrantID = androidId
                    ) {
                        activeReservations = it
                    }
                }

                EntrantViewReservationsScreen(
                    activeReservations = activeReservations,
                    campsiteDB = campsiteDB,
                    onCancelled = {
                        resultTitle = "Campsite Cancelled"
                        resultMessage =
                            "Your reservation was cancelled."

                        navController.navigate(CampsiteResult)
                    },
                    onBack = {navController.popBackStack()}
                )
            }

            composable<Profile> {
                ProfileScreen(
                    navController,
                    curUser = curUser,
                    onProfileDeleted = {
                        curUser?.let { user ->
                            userDB.deleteUser(user) {
                                onProfileDeleted()
                            }
                        }
                    }
                )
            }

            composable<EditProfile> {
                EditProfileScreen(
                    navController,
                    curUser,
                    onUpdateProfile = {
                        userDB.updateUser(it)
                    }
                )
            }

            // Settings tab
            composable(EntrantDestination.SETTINGS.route) {
                SettingsScreen(
                    onProfile = {
                        navController.navigate(Profile)
                    },
                    onViewReservations = {
                        navController.navigate(
                            EntrantViewReservations
                        )
                    },
                    onAppPolicy = {
                        navController.navigate(
                            AppPolicyGuidelines
                        )
                    },
                    onComingSoon = {
                        navController.navigate(ComingSoon)
                    }
                )
            }

            // Scan QR tab (US 01.12.01)
            composable(EntrantDestination.SCAN_QR.route) {
                ScanQRCodeScreen(
                    onBack = {navController.popBackStack()},
                    onScanSuccessful = {
                        campsiteDB.readCampsite(it, {
                            if (it != null) {
                                selectedCampsite = it
                                navController.navigate(CampsiteDetails)
                            } else {
                                Toast.makeText(context, "Invalid campsite ID", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                )
            }

            // Notifications and Messages from Settings (not done yet)
            composable<ComingSoon> {
                ComingSoonScreen(
                    title = "Coming soon",
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}