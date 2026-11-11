package com.waymate.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.waymate.app.data.model.FlightDetails
import com.waymate.app.data.model.FlightDirection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AirportUiState(
    val flightDetails: FlightDetails = FlightDetails(),
    val popularAirports: List<String> = listOf(
        "Indira Gandhi Int'l Airport (DEL)",
        "Kempegowda Int'l Airport (BLR)",
        "Chhatrapati Shivaji Maharaj Int'l (BOM)",
        "Rajiv Gandhi Int'l Airport (HYD)"
    ),
    val selectedTerminal: String = "Terminal 3",
    val availableTerminals: List<String> = listOf("Terminal 1", "Terminal 2", "Terminal 3")
)

class AirportViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AirportUiState())
    val uiState: StateFlow<AirportUiState> = _uiState.asStateFlow()

    fun onFlightNumberChange(flightNo: String) {
        val upper = flightNo.uppercase().trim()
        val airlineName = when {
            upper.startsWith("AI") -> "Air India"
            upper.startsWith("6E") -> "IndiGo"
            upper.startsWith("UK") -> "Vistara"
            upper.startsWith("SG") -> "SpiceJet"
            upper.startsWith("QP") -> "Akasa Air"
            upper.startsWith("EK") -> "Emirates"
            upper.startsWith("BA") -> "British Airways"
            else -> _uiState.value.flightDetails.airline
        }
        _uiState.value = _uiState.value.copy(
            flightDetails = _uiState.value.flightDetails.copy(
                flightNumber = upper,
                airline = airlineName
            )
        )
    }

    fun onAirportChange(airport: String) {
        _uiState.value = _uiState.value.copy(
            flightDetails = _uiState.value.flightDetails.copy(airport = airport)
        )
    }

    fun onTerminalChange(terminal: String) {
        _uiState.value = _uiState.value.copy(
            selectedTerminal = terminal,
            flightDetails = _uiState.value.flightDetails.copy(airportTerminal = terminal)
        )
    }

    fun onDirectionChange(direction: FlightDirection) {
        _uiState.value = _uiState.value.copy(
            flightDetails = _uiState.value.flightDetails.copy(direction = direction)
        )
    }

    fun onDateChange(date: String) {
        _uiState.value = _uiState.value.copy(
            flightDetails = _uiState.value.flightDetails.copy(flightDate = date)
        )
    }

    fun onTimeChange(time: String) {
        _uiState.value = _uiState.value.copy(
            flightDetails = _uiState.value.flightDetails.copy(flightTime = time)
        )
    }

    fun onDestinationChange(destination: String) {
        _uiState.value = _uiState.value.copy(
            flightDetails = _uiState.value.flightDetails.copy(destination = destination)
        )
    }

    fun incrementLuggage() {
        val current = _uiState.value.flightDetails.luggageQuantity
        if (current < 8) {
            _uiState.value = _uiState.value.copy(
                flightDetails = _uiState.value.flightDetails.copy(luggageQuantity = current + 1)
            )
        }
    }

    fun decrementLuggage() {
        val current = _uiState.value.flightDetails.luggageQuantity
        if (current > 1) {
            _uiState.value = _uiState.value.copy(
                flightDetails = _uiState.value.flightDetails.copy(luggageQuantity = current - 1)
            )
        }
    }
}
