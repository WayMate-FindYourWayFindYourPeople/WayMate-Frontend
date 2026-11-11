package com.waymate.app.data.model

enum class PoolType {
    ALL,
    WOMEN_ONLY,
    MIXED
}

data class PoolPassenger(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val isHost: Boolean = false,
    val isVerified: Boolean = true,
    val pickupPoint: String? = null
)

data class PoolItem(
    val id: String,
    val host: PoolPassenger,
    val pickup: String,
    val destination: String,
    val date: String,
    val time: String,
    val estimatedArrival: String,
    val totalSeats: Int,
    val availableSeats: Int,
    val farePerPerson: Double,
    val regularSoloFare: Double,
    val poolType: PoolType,
    val passengers: List<PoolPassenger>,
    val luggagePerPerson: Int = 1,
    val stopsCount: Int = 1,
    val vehicleModel: String = "Maruti Suzuki Ertiga",
    val vehiclePlate: String = "DL 03 CY 8820",
    val description: String = "Direct airport transfer pool. Leaving right outside Gate 4 Arrivals.",
    val isJoinedByMe: Boolean = false,
    val isHostedByMe: Boolean = false
)
