package com.waymate.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.waymate.app.di.WayMateAppLocator
import com.waymate.app.ui.components.BottomNavTab
import com.waymate.app.ui.screens.airport.AirportRideScreen
import com.waymate.app.ui.screens.airport.FlightDetailsScreen
import com.waymate.app.ui.screens.auth.*
import com.waymate.app.ui.screens.home.HomeScreen
import com.waymate.app.ui.screens.messages.ChatDetailScreen
import com.waymate.app.ui.screens.messages.MessagesScreen
import com.waymate.app.ui.screens.notifications.NotificationsScreen
import com.waymate.app.ui.screens.pool.*
import com.waymate.app.ui.screens.profile.EditProfileScreen
import com.waymate.app.ui.screens.profile.ProfileScreen
import com.waymate.app.ui.screens.profile.SettingsScreen
import com.waymate.app.ui.screens.ride.*
import com.waymate.app.ui.screens.trips.MyTripsScreen
import com.waymate.app.ui.viewmodel.*

@Composable
fun WayMateNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val container = WayMateAppLocator.container

    // ViewModels with Shared Repositories
    val authViewModel = rememberViewModel { AuthViewModel(container.authRepository) }
    val homeViewModel = rememberViewModel { HomeViewModel(container.authRepository, container.rideRepository) }
    val rideViewModel = rememberViewModel { RideViewModel(container.rideRepository) }
    val airportViewModel = rememberViewModel { AirportViewModel() }
    val poolViewModel = rememberViewModel { PoolViewModel(container.poolRepository) }
    val tripViewModel = rememberViewModel { TripViewModel(container.tripRepository) }
    val messageViewModel = rememberViewModel { MessageViewModel(container.messageRepository) }
    val notificationViewModel = rememberViewModel { NotificationViewModel(container.notificationRepository) }
    val profileViewModel = rememberViewModel { ProfileViewModel(container.authRepository) }

    fun handleBottomTabSelection(tab: BottomNavTab) {
        val targetRoute = when (tab) {
            BottomNavTab.HOME -> Screen.Home.route
            BottomNavTab.TRIPS -> Screen.MyTrips.route
            BottomNavTab.POOLS -> Screen.MyPools.route
            BottomNavTab.MESSAGES -> Screen.Messages.route
            BottomNavTab.PROFILE -> Screen.Profile.route
        }
        navController.navigate(targetRoute) {
            popUpTo(Screen.Home.route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        // ================= AUTHENTICATION FLOW =================
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToOtp = { navController.navigate(Screen.OtpVerification.route) },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                viewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.OtpVerification.route) {
            OtpVerificationScreen(
                viewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() },
                onVerificationSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // ================= HOME DASHBOARD =================
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToChooseRide = { navController.navigate(Screen.ChooseRide.route) },
                onNavigateToAirportRide = { navController.navigate(Screen.AirportRide.route) },
                onNavigateToPools = { navController.navigate(Screen.AvailablePools.route) },
                onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                onNavigateToLiveTracking = { navController.navigate(Screen.LiveTracking.route) },
                onBottomTabSelected = ::handleBottomTabSelection
            )
        }

        // ================= PRIVATE RIDE JOURNEY =================
        composable(Screen.ChooseRide.route) {
            ChooseRideScreen(
                viewModel = rideViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToBookRide = { navController.navigate(Screen.BookRide.route) },
                onNavigateToPoolRides = { navController.navigate(Screen.AvailablePools.route) }
            )
        }

        composable(Screen.BookRide.route) {
            BookRideScreen(
                viewModel = rideViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAvailableRides = { navController.navigate(Screen.AvailableRides.route) }
            )
        }

        composable(Screen.AvailableRides.route) {
            AvailableRidesScreen(
                viewModel = rideViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRideMatching = { navController.navigate(Screen.RideMatching.route) }
            )
        }

        composable(Screen.RideMatching.route) {
            RideMatchingScreen(
                viewModel = rideViewModel,
                onRideMatched = {
                    navController.navigate(Screen.RideFound.route) {
                        popUpTo(Screen.AvailableRides.route) { inclusive = true }
                    }
                },
                onCancelRide = { navController.popBackStack() }
            )
        }

        composable(Screen.RideFound.route) {
            RideFoundScreen(
                viewModel = rideViewModel,
                onNavigateToLiveTracking = { navController.navigate(Screen.LiveTracking.route) },
                onNavigateToChat = {
                    messageViewModel.openConversation(container.messageRepository.conversations.value[0])
                    navController.navigate(Screen.ChatDetail.createRoute("conv_1"))
                },
                onNavigateBack = { navController.navigate(Screen.Home.route) }
            )
        }

        composable(Screen.LiveTracking.route) {
            LiveTrackingScreen(
                viewModel = rideViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChat = {
                    messageViewModel.openConversation(container.messageRepository.conversations.value[0])
                    navController.navigate(Screen.ChatDetail.createRoute("conv_1"))
                },
                onTripCompleted = {
                    navController.navigate(Screen.TripCompleted.route) {
                        popUpTo(Screen.LiveTracking.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.TripCompleted.route) {
            TripCompletedScreen(
                viewModel = rideViewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // ================= AIRPORT RIDE JOURNEY =================
        composable(Screen.AirportRide.route) {
            AirportRideScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToFlightDetails = { navController.navigate(Screen.FlightDetails.route) }
            )
        }

        composable(Screen.FlightDetails.route) {
            FlightDetailsScreen(
                viewModel = airportViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChooseRideType = { navController.navigate(Screen.ChooseRideType.route) }
            )
        }

        // ================= POOL JOURNEY =================
        composable(Screen.ChooseRideType.route) {
            ChooseRideTypeScreen(
                rideViewModel = rideViewModel,
                poolViewModel = poolViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAvailablePools = { navController.navigate(Screen.AvailablePools.route) },
                onNavigateToAvailableRides = { navController.navigate(Screen.AvailableRides.route) }
            )
        }

        composable(Screen.AvailablePools.route) {
            AvailablePoolsScreen(
                viewModel = poolViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPoolDetails = { pool ->
                    poolViewModel.selectPool(pool)
                    navController.navigate(Screen.PoolDetails.route)
                },
                onNavigateToCreatePool = { navController.navigate(Screen.CreatePool.route) }
            )
        }

        composable(Screen.PoolDetails.route) {
            PoolDetailsScreen(
                viewModel = poolViewModel,
                onNavigateBack = { navController.popBackStack() },
                onJoinSuccess = {
                    navController.navigate(Screen.MyPools.route) {
                        popUpTo(Screen.AvailablePools.route) { inclusive = false }
                    }
                }
            )
        }

        composable(Screen.CreatePool.route) {
            CreatePoolScreen(
                viewModel = poolViewModel,
                onNavigateBack = { navController.popBackStack() },
                onPoolCreated = {
                    poolViewModel.setMyPoolsTab(true)
                    navController.navigate(Screen.MyPools.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                }
            )
        }

        composable(Screen.MyPools.route) {
            MyPoolsScreen(
                viewModel = poolViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCreatePool = { navController.navigate(Screen.CreatePool.route) },
                onBottomTabSelected = ::handleBottomTabSelection
            )
        }

        // ================= MY TRIPS =================
        composable(Screen.MyTrips.route) {
            MyTripsScreen(
                viewModel = tripViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLiveTracking = { navController.navigate(Screen.LiveTracking.route) },
                onBottomTabSelected = ::handleBottomTabSelection
            )
        }

        // ================= MESSAGES & CHAT =================
        composable(Screen.Messages.route) {
            MessagesScreen(
                viewModel = messageViewModel,
                onNavigateBack = { navController.popBackStack() },
                onOpenConversation = { conv ->
                    messageViewModel.openConversation(conv)
                    navController.navigate(Screen.ChatDetail.createRoute(conv.id))
                },
                onBottomTabSelected = ::handleBottomTabSelection
            )
        }

        composable(Screen.ChatDetail.route) {
            ChatDetailScreen(
                viewModel = messageViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ================= NOTIFICATIONS =================
        composable(Screen.Notifications.route) {
            NotificationsScreen(
                viewModel = notificationViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ================= PROFILE & SETTINGS =================
        composable(Screen.Profile.route) {
            ProfileScreen(
                viewModel = profileViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditProfile = { navController.navigate(Screen.EditProfile.route) },
                onNavigateToMyTrips = { navController.navigate(Screen.MyTrips.route) },
                onNavigateToMyPools = { navController.navigate(Screen.MyPools.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onLogoutSuccess = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onBottomTabSelected = ::handleBottomTabSelection
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                viewModel = profileViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = profileViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
inline fun <reified VM : androidx.lifecycle.ViewModel> rememberViewModel(
    crossinline factory: () -> VM
): VM {
    return androidx.lifecycle.viewmodel.compose.viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return factory() as T
            }
        }
    )
}
