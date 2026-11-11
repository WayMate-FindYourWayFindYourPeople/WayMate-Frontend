package com.waymate.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waymate.app.data.model.User
import com.waymate.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User = User(),
    val pushNotificationsEnabled: Boolean = true,
    val rideUpdatesSms: Boolean = true,
    val emergencyContactName: String = "Sarah Morgan (+91 98765 11223)",
    val savedHomeAddress: String = "Flat 402, Cyber Heights, Sector 54, Gurugram",
    val savedWorkAddress: String = "Building 10C, DLF Cyber City, Gurugram",
    val savedAirport: String = "Indira Gandhi Int'l Airport (DEL) Terminal 3",
    val defaultPaymentMethod: String = "WayMate Wallet (₹1,450.00)"
)

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                if (user != null) {
                    _uiState.value = _uiState.value.copy(user = user)
                }
            }
        }
    }

    fun togglePushNotifications(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(pushNotificationsEnabled = enabled)
    }

    fun toggleSmsUpdates(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(rideUpdatesSms = enabled)
    }

    fun updateProfile(name: String, email: String, phone: String) {
        val current = _uiState.value.user
        val updated = current.copy(
            fullName = name,
            email = email,
            phone = phone
        )
        _uiState.value = _uiState.value.copy(user = updated)
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onLoggedOut()
        }
    }
}
