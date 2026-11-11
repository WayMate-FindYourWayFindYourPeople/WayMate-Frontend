package com.waymate.app.data.repository

import com.waymate.app.data.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface RideRepository {
    fun getVehicleOptions(pickup: String, destination: String): List<VehicleOption>
    suspend fun findDriver(selectedVehicleId: String): Driver
    fun getUpcomingRide(): RideBooking?
    suspend fun bookRide(booking: RideBooking): RideBooking
    suspend fun rateTrip(tripId: String, rating: Int, feedback: String?): Boolean
}

class MockRideRepository : RideRepository {

    private val vehicleOptions = listOf(
        VehicleOption(
            id = "v_go",
            name = "WayMate Go",
            subtitle = "Affordable, compact rides for everyday travel",
            fare = 420.0,
            originalFare = 490.0,
            etaMinutes = 3,
            distanceKm = 14.8,
            capacity = 4,
            luggageCapacity = 2,
            isPopular = true
        ),
        VehicleOption(
            id = "v_electric",
            name = "WayMate Green EV",
            subtitle = "Zero emissions, whisper-quiet electric cab",
            fare = 460.0,
            originalFare = 520.0,
            etaMinutes = 5,
            distanceKm = 14.8,
            capacity = 4,
            luggageCapacity = 2,
            isElectric = true
        ),
        VehicleOption(
            id = "v_premier",
            name = "WayMate Premier",
            subtitle = "Top-rated drivers & spacious comfortable sedans",
            fare = 580.0,
            originalFare = 650.0,
            etaMinutes = 6,
            distanceKm = 14.8,
            capacity = 4,
            luggageCapacity = 3,
            isFastest = true
        ),
        VehicleOption(
            id = "v_xl",
            name = "WayMate XL",
            subtitle = "Spacious 6-seater SUV with extra airport luggage room",
            fare = 780.0,
            originalFare = 890.0,
            etaMinutes = 8,
            distanceKm = 14.8,
            capacity = 6,
            luggageCapacity = 5
        )
    )

    private val sampleDriver = Driver(
        id = "drv_884",
        name = "Rajesh Verma",
        photoUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
        rating = 4.93,
        totalTrips = 2410,
        phone = "+91 98112 33445",
        vehicleModel = "Toyota Urban Cruiser Hyryder (Hybrid)",
        vehicleColor = "Arctic White",
        plateNumber = "DL 01 AB 4022",
        startOtp = "4821"
    )

    override fun getVehicleOptions(pickup: String, destination: String): List<VehicleOption> {
        return vehicleOptions
    }

    override suspend fun findDriver(selectedVehicleId: String): Driver {
        delay(2500) // simulate matching animation
        return sampleDriver
    }

    override fun getUpcomingRide(): RideBooking {
        return RideBooking(
            id = "RB-9921",
            pickup = "Terminal 3, IGI Airport (DEL)",
            destination = "Cyber City, DLF Phase 2, Gurugram",
            date = "Today",
            time = "11:15 AM",
            passengers = 1,
            luggage = 2,
            rideCategory = RideCategory.SOLO,
            selectedVehicle = vehicleOptions[0],
            assignedDriver = sampleDriver,
            status = "CONFIRMED"
        )
    }

    override suspend fun bookRide(booking: RideBooking): RideBooking {
        delay(1000)
        return booking.copy(assignedDriver = sampleDriver, status = "CONFIRMED")
    }

    override suspend fun rateTrip(tripId: String, rating: Int, feedback: String?): Boolean {
        delay(400)
        return true
    }
}
