package com.waymate.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waymate.app.data.model.RideBooking
import com.waymate.app.data.model.User
import com.waymate.app.data.repository.AuthRepository
import com.waymate.app.data.repository.RideRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val user: User = User(),
    val upcomingRide: RideBooking? = null,
    val unreadNotificationsCount: Int = 2,
    val unreadMessagesCount: Int = 1,
    val searchDestinationQuery: String = "",
    val activePromotionText: String = "Airport Transfers from ₹280 • Zero surge pricing on shared pools"
)

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val rideRepository: RideRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                if (user != null) {
                    _uiState.value = _uiState.value.copy(user = user)
                }
            }
        }
        val upcoming = rideRepository.getUpcomingRide()
        _uiState.value = _uiState.value.copy(upcomingRide = upcoming)
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchDestinationQuery = query)
    }
}
