package com.waymate.app.di

import com.waymate.app.data.repository.*

class AppContainer {
    val authRepository: AuthRepository by lazy { MockAuthRepository() }
    val rideRepository: RideRepository by lazy { MockRideRepository() }
    val poolRepository: PoolRepository by lazy { MockPoolRepository() }
    val tripRepository: TripRepository by lazy { MockTripRepository() }
    val messageRepository: MessageRepository by lazy { MockMessageRepository() }
    val notificationRepository: NotificationRepository by lazy { MockNotificationRepository() }
}

object WayMateAppLocator {
    lateinit var container: AppContainer

    fun initialize() {
        container = AppContainer()
    }
}
