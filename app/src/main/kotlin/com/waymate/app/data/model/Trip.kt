package com.waymate.app.data.model

enum class TripStatus {
    UPCOMING,
    COMPLETED,
    CANCELLED
}

data class Trip(
    val id: String,
    val bookingReference: String,
    val pickup: String,
    val destination: String,
    val dateTime: String,
    val status: TripStatus,
    val rideType: String, // "Solo Private", "Shared Pool", "Airport Express"
    val fare: Double,
    val driverName: String,
    val driverPhoto: String,
    val driverRating: Double,
    val vehicleModel: String,
    val plateNumber: String,
    val distanceKm: Double,
    val durationMinutes: Int,
    val userRating: Int? = null,
    val baseFare: Double = fare * 0.7,
    val tollAndAirportTax: Double = fare * 0.2,
    val discount: Double = fare * 0.1
)
