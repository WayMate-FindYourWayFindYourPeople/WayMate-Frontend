package com.waymate.app.ui.navigation

sealed class Screen(val route: String) {
    // Auth Flow
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object OtpVerification : Screen("otp_verification")

    // Main Bottom Nav Tabs
    object Home : Screen("home")
    object MyTrips : Screen("my_trips")
    object Pools : Screen("pools")
    object Messages : Screen("messages")
    object Profile : Screen("profile")

    // Private Ride Journey
    object ChooseRide : Screen("choose_ride")
    object BookRide : Screen("book_ride")
    object AvailableRides : Screen("available_rides")
    object RideMatching : Screen("ride_matching")
    object RideFound : Screen("ride_found")
    object LiveTracking : Screen("live_tracking")
    object TripCompleted : Screen("trip_completed")

    // Airport Ride Journey
    object AirportRide : Screen("airport_ride")
    object FlightDetails : Screen("flight_details")

    // Pool Journey
    object ChooseRideType : Screen("choose_ride_type")
    object AvailablePools : Screen("available_pools")
    object PoolDetails : Screen("pool_details")
    object CreatePool : Screen("create_pool")
    object MyPools : Screen("my_pools")

    // Supportive screens
    object ChatDetail : Screen("chat_detail/{conversationId}") {
        fun createRoute(conversationId: String) = "chat_detail/$conversationId"
    }
    object Notifications : Screen("notifications")
    object EditProfile : Screen("edit_profile")
    object Settings : Screen("settings")
}
