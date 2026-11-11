package com.waymate.app.data.model

enum class RideCategory {
    SOLO,
    POOL,
    AIRPORT
}

data class Driver(
    val id: String,
    val name: String,
    val photoUrl: String,
    val rating: Double,
    val totalTrips: Int,
    val phone: String,
    val vehicleModel: String,
    val vehicleColor: String,
    val plateNumber: String,
    val startOtp: String
)

data class VehicleOption(
    val id: String,
    val name: String,
    val subtitle: String,
    val fare: Double,
    val originalFare: Double? = null,
    val etaMinutes: Int,
    val distanceKm: Double,
    val capacity: Int,
    val luggageCapacity: Int,
    val isElectric: Boolean = false,
    val isPopular: Boolean = false,
    val isFastest: Boolean = false
)

data class RideBooking(
    val id: String = "rb_101",
    val pickup: String = "Terminal 3, IGI Airport (DEL)",
    val destination: String = "DLF Cyber City, Sector 24, Gurugram",
    val date: String = "Today, Nov 7",
    val time: String = "10:30 AM",
    val passengers: Int = 1,
    val luggage: Int = 2,
    val rideCategory: RideCategory = RideCategory.SOLO,
    val selectedVehicle: VehicleOption? = null,
    val assignedDriver: Driver? = null,
    val status: String = "PENDING"
)
