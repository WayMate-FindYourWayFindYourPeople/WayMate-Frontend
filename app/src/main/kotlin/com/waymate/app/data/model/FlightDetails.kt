package com.waymate.app.data.model

enum class FlightDirection {
    ARRIVAL,
    DEPARTURE
}

data class FlightDetails(
    val flightNumber: String = "AI-804",
    val airline: String = "Air India",
    val airport: String = "Indira Gandhi Int'l Airport (DEL)",
    val airportTerminal: String = "Terminal 3",
    val direction: FlightDirection = FlightDirection.ARRIVAL,
    val flightDate: String = "Nov 07, 2026",
    val flightTime: String = "14:45",
    val destination: String = "Cyber City, DLF Phase 2, Gurugram",
    val luggageQuantity: Int = 2,
    val passengers: Int = 1
)
