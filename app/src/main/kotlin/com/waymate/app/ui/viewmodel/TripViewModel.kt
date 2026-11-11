package com.waymate.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waymate.app.data.model.Trip
import com.waymate.app.data.model.TripStatus
import com.waymate.app.data.repository.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TripUiState(
    val selectedStatus: TripStatus = TripStatus.UPCOMING,
    val upcomingTrips: List<Trip> = emptyList(),
    val completedTrips: List<Trip> = emptyList(),
    val cancelledTrips: List<Trip> = emptyList(),
    val selectedTrip: Trip? = null,
    val isLoading: Boolean = false
)

class TripViewModel(
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripUiState())
    val uiState: StateFlow<TripUiState> = _uiState.asStateFlow()

    init {
        loadTrips()
    }

    private fun loadTrips() {
        viewModelScope.launch {
            tripRepository.trips.collect { allTrips ->
                _uiState.value = _uiState.value.copy(
                    upcomingTrips = allTrips.filter { it.status == TripStatus.UPCOMING },
                    completedTrips = allTrips.filter { it.status == TripStatus.COMPLETED },
                    cancelledTrips = allTrips.filter { it.status == TripStatus.CANCELLED }
                )
            }
        }
    }

    fun setStatusFilter(status: TripStatus) {
        _uiState.value = _uiState.value.copy(selectedStatus = status)
    }

    fun selectTrip(trip: Trip) {
        _uiState.value = _uiState.value.copy(selectedTrip = trip)
    }

    fun cancelTrip(tripId: String) {
        viewModelScope.launch {
            tripRepository.cancelTrip(tripId)
        }
    }
}
