package com.waymate.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waymate.app.data.model.*
import com.waymate.app.data.repository.RideRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class MatchingStep {
    FINDING_RIDE,
    SEARCHING_NEARBY_DRIVERS,
    MATCHING_ROUTE,
    CONFIRMING_VEHICLE,
    COMPLETED
}

data class RideUiState(
    // Booking Form
    val pickupLocation: String = "Terminal 3, IGI Airport (DEL)",
    val destinationLocation: String = "Cyber City, DLF Phase 2, Gurugram",
    val travelDate: String = "Today, Nov 7",
    val travelTime: String = "11:30 AM",
    val passengerCount: Int = 1,
    val luggageCount: Int = 2,
    val selectedRideType: RideCategory = RideCategory.SOLO,

    // Available rides
    val availableVehicles: List<VehicleOption> = emptyList(),
    val selectedVehicle: VehicleOption? = null,
    val selectedPaymentMethod: String = "WayMate Wallet (₹1,450)",

    // Matching flow
    val matchingStep: MatchingStep = MatchingStep.FINDING_RIDE,
    val matchingProgress: Float = 0.2f,

    // Ride Found & Live Tracking
    val assignedDriver: Driver? = null,
    val trackingEtaMinutes: Int = 4,
    val driverDistanceKm: Double = 1.2,
    val rideOtp: String = "4821",
    val tripDistanceKm: Double = 14.8,
    val tripDurationMins: Int = 26,
    val tripFare: Double = 420.0,
    val tripCompleted: Boolean = false,

    // Rating
    val ratingGiven: Int = 5,
    val selectedFeedbackTags: Set<String> = emptySet(),
    val feedbackComment: String = ""
)

class RideViewModel(
    private val rideRepository: RideRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RideUiState())
    val uiState: StateFlow<RideUiState> = _uiState.asStateFlow()

    private var matchingJob: Job? = null
    private var trackingJob: Job? = null

    init {
        loadVehicles()
    }

    private fun loadVehicles() {
        val list = rideRepository.getVehicleOptions(_uiState.value.pickupLocation, _uiState.value.destinationLocation)
        _uiState.value = _uiState.value.copy(
            availableVehicles = list,
            selectedVehicle = list.firstOrNull()
        )
    }

    fun onPickupChange(loc: String) {
        _uiState.value = _uiState.value.copy(pickupLocation = loc)
    }

    fun onDestinationChange(loc: String) {
        _uiState.value = _uiState.value.copy(destinationLocation = loc)
    }

    fun swapLocations() {
        val currentPickup = _uiState.value.pickupLocation
        val currentDest = _uiState.value.destinationLocation
        _uiState.value = _uiState.value.copy(
            pickupLocation = currentDest,
            destinationLocation = currentPickup
        )
    }

    fun onDateChange(date: String) {
        _uiState.value = _uiState.value.copy(travelDate = date)
    }

    fun onTimeChange(time: String) {
        _uiState.value = _uiState.value.copy(travelTime = time)
    }

    fun incrementPassengers() {
        if (_uiState.value.passengerCount < 6) {
            _uiState.value = _uiState.value.copy(passengerCount = _uiState.value.passengerCount + 1)
        }
    }

    fun decrementPassengers() {
        if (_uiState.value.passengerCount > 1) {
            _uiState.value = _uiState.value.copy(passengerCount = _uiState.value.passengerCount - 1)
        }
    }

    fun incrementLuggage() {
        if (_uiState.value.luggageCount < 8) {
            _uiState.value = _uiState.value.copy(luggageCount = _uiState.value.luggageCount + 1)
        }
    }

    fun decrementLuggage() {
        if (_uiState.value.luggageCount > 0) {
            _uiState.value = _uiState.value.copy(luggageCount = _uiState.value.luggageCount - 1)
        }
    }

    fun selectRideType(type: RideCategory) {
        _uiState.value = _uiState.value.copy(selectedRideType = type)
    }

    fun selectVehicle(vehicle: VehicleOption) {
        _uiState.value = _uiState.value.copy(
            selectedVehicle = vehicle,
            tripFare = vehicle.fare
        )
    }

    fun selectPaymentMethod(method: String) {
        _uiState.value = _uiState.value.copy(selectedPaymentMethod = method)
    }

    fun startMatching(onMatched: () -> Unit) {
        matchingJob?.cancel()
        _uiState.value = _uiState.value.copy(
            matchingStep = MatchingStep.FINDING_RIDE,
            matchingProgress = 0.15f
        )

        matchingJob = viewModelScope.launch {
            delay(1200)
            _uiState.value = _uiState.value.copy(
                matchingStep = MatchingStep.SEARCHING_NEARBY_DRIVERS,
                matchingProgress = 0.45f
            )
            delay(1200)
            _uiState.value = _uiState.value.copy(
                matchingStep = MatchingStep.MATCHING_ROUTE,
                matchingProgress = 0.75f
            )
            delay(1000)
            _uiState.value = _uiState.value.copy(
                matchingStep = MatchingStep.CONFIRMING_VEHICLE,
                matchingProgress = 0.95f
            )
            delay(800)
            val driver = rideRepository.findDriver(_uiState.value.selectedVehicle?.id ?: "v_go")
            _uiState.value = _uiState.value.copy(
                matchingStep = MatchingStep.COMPLETED,
                matchingProgress = 1.0f,
                assignedDriver = driver
            )
            onMatched()
            startLiveTrackingSimulation()
        }
    }

    private fun startLiveTrackingSimulation() {
        trackingJob?.cancel()
        trackingJob = viewModelScope.launch {
            for (min in 4 downTo 1) {
                delay(3000) // fast simulation cycle for demo
                _uiState.value = _uiState.value.copy(
                    trackingEtaMinutes = min,
                    driverDistanceKm = (min * 0.35).coerceAtLeast(0.1)
                )
            }
        }
    }

    fun completeTrip() {
        _uiState.value = _uiState.value.copy(tripCompleted = true)
    }

    fun setRating(rating: Int) {
        _uiState.value = _uiState.value.copy(ratingGiven = rating)
    }

    fun toggleFeedbackTag(tag: String) {
        val current = _uiState.value.selectedFeedbackTags.toMutableSet()
        if (current.contains(tag)) current.remove(tag) else current.add(tag)
        _uiState.value = _uiState.value.copy(selectedFeedbackTags = current)
    }

    fun setFeedbackComment(comment: String) {
        _uiState.value = _uiState.value.copy(feedbackComment = comment)
    }
}
