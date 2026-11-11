package com.waymate.app.data.repository

import com.waymate.app.data.model.Trip
import com.waymate.app.data.model.TripStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface TripRepository {
    val trips: StateFlow<List<Trip>>
    suspend fun getTripsByStatus(status: TripStatus): List<Trip>
    suspend fun getTripById(id: String): Trip?
    suspend fun cancelTrip(id: String): Boolean
}

class MockTripRepository : TripRepository {

    private val initialTrips = listOf(
        Trip(
            id = "trip_001",
            bookingReference = "WM-DEL-8921",
            pickup = "Terminal 3, IGI Airport (DEL)",
            destination = "Cyber City, DLF Phase 2, Gurugram",
            dateTime = "Today, 11:15 AM",
            status = TripStatus.UPCOMING,
            rideType = "WayMate Go (Private)",
            fare = 420.0,
            driverName = "Rajesh Verma",
            driverPhoto = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
            driverRating = 4.93,
            vehicleModel = "Toyota Hyryder (Arctic White)",
            plateNumber = "DL 01 AB 4022",
            distanceKm = 14.8,
            durationMinutes = 28
        ),
        Trip(
            id = "trip_002",
            bookingReference = "WM-BLR-4412",
            pickup = "Kempegowda Airport Terminal 2",
            destination = "Indiranagar 100ft Road, Bengaluru",
            dateTime = "Tomorrow, 04:30 PM",
            status = TripStatus.UPCOMING,
            rideType = "Shared Pool (Mixed)",
            fare = 390.0,
            driverName = "Vikram Malhotra",
            driverPhoto = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=400&q=80",
            driverRating = 4.88,
            vehicleModel = "Maruti Ertiga Smart Hybrid",
            plateNumber = "KA 04 MP 1190",
            distanceKm = 38.2,
            durationMinutes = 65
        ),
        Trip(
            id = "trip_003",
            bookingReference = "WM-MUM-7731",
            pickup = "Terminal 2, CSMI Airport, Mumbai",
            destination = "Bandra Kurla Complex (BKC)",
            dateTime = "Oct 28, 2026, 09:40 PM",
            status = TripStatus.COMPLETED,
            rideType = "WayMate Premier",
            fare = 540.0,
            driverName = "Manoj Patil",
            driverPhoto = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=400&q=80",
            driverRating = 4.96,
            vehicleModel = "Honda City VX",
            plateNumber = "MH 02 FJ 3311",
            distanceKm = 12.4,
            durationMinutes = 32,
            userRating = 5
        ),
        Trip(
            id = "trip_004",
            bookingReference = "WM-DEL-6218",
            pickup = "Connaught Place Inner Circle",
            destination = "Terminal 1D Domestic, IGI Airport",
            dateTime = "Oct 19, 2026, 06:15 AM",
            status = TripStatus.COMPLETED,
            rideType = "Shared Pool (Women Only)",
            fare = 290.0,
            driverName = "Sunita Deshmukh",
            driverPhoto = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=400&q=80",
            driverRating = 4.98,
            vehicleModel = "Tata Tigor EV",
            plateNumber = "DL 02 EV 9918",
            distanceKm = 16.5,
            durationMinutes = 38,
            userRating = 5
        ),
        Trip(
            id = "trip_005",
            bookingReference = "WM-DEL-5510",
            pickup = "Aerocity Metro Station, Delhi",
            destination = "Sector 54, Golf Course Rd, Gurugram",
            dateTime = "Oct 12, 2026, 08:30 PM",
            status = TripStatus.CANCELLED,
            rideType = "WayMate Go",
            fare = 380.0,
            driverName = "Amit Kumar",
            driverPhoto = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
            driverRating = 4.79,
            vehicleModel = "Hyundai Aura",
            plateNumber = "HR 26 DQ 7721",
            distanceKm = 18.0,
            durationMinutes = 35
        )
    )

    private val _trips = MutableStateFlow(initialTrips)
    override val trips: StateFlow<List<Trip>> = _trips.asStateFlow()

    override suspend fun getTripsByStatus(status: TripStatus): List<Trip> {
        return _trips.value.filter { it.status == status }
    }

    override suspend fun getTripById(id: String): Trip? {
        return _trips.value.find { it.id == id }
    }

    override suspend fun cancelTrip(id: String): Boolean {
        delay(500)
        val list = _trips.value.toMutableList()
        val index = list.indexOfFirst { it.id == id }
        if (index != -1) {
            list[index] = list[index].copy(status = TripStatus.CANCELLED)
            _trips.value = list
            return true
        }
        return false
    }
}
