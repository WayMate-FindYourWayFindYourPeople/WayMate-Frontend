package com.waymate.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waymate.app.data.model.User
import com.waymate.app.data.repository.AuthRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val mobileNumber: String = "",
    val countryCode: String = "+91",
    val otp: String = "",
    val otpCountdown: Int = 45,
    val canResendOtp: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    val isOtpSent: Boolean = false,
    val isAuthenticated: Boolean = false,

    // Sign up fields
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val agreeToTerms: Boolean = false,
    val isRegisteredSuccess: Boolean = false
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        // Pre-fill phone if previously registered
        authRepository.getRegisteredPhone()?.let { phone ->
            _uiState.value = _uiState.value.copy(mobileNumber = phone)
        }
    }

    fun onMobileNumberChange(number: String) {
        val digits = number.filter { it.isDigit() }.take(10)
        _uiState.value = _uiState.value.copy(
            mobileNumber = digits,
            errorMessage = null
        )
    }

    fun onCountryCodeChange(code: String) {
        _uiState.value = _uiState.value.copy(countryCode = code)
    }

    fun onOtpChange(otp: String) {
        val digits = otp.filter { it.isDigit() }.take(6)
        _uiState.value = _uiState.value.copy(
            otp = digits,
            errorMessage = null
        )
    }

    fun sendOtp(onSuccess: () -> Unit) {
        val phone = _uiState.value.mobileNumber
        if (phone.length < 10) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter a valid 10-digit mobile number")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.sendOtp(phone, _uiState.value.countryCode)
            result.onSuccess { hint ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isOtpSent = true,
                    infoMessage = hint,
                    otp = "1234" // Pre-filled demo convenience as requested
                )
                startCountdown()
                onSuccess()
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = err.message ?: "Failed to send OTP"
                )
            }
        }
    }

    fun resendOtp() {
        if (!_uiState.value.canResendOtp) return
        sendOtp { }
    }

    private fun startCountdown() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(otpCountdown = 45, canResendOtp = false)
        timerJob = viewModelScope.launch {
            for (i in 44 downTo 0) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    otpCountdown = i,
                    canResendOtp = (i == 0)
                )
            }
        }
    }

    fun verifyOtp(onSuccess: () -> Unit) {
        val otp = _uiState.value.otp
        if (otp.length < 4) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter the 4-digit OTP")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.verifyOtp(_uiState.value.mobileNumber, otp)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = true
                )
                onSuccess()
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = err.message ?: "Invalid OTP"
                )
            }
        }
    }

    // Sign Up Handlers
    fun onFullNameChange(name: String) {
        _uiState.value = _uiState.value.copy(fullName = name, errorMessage = null)
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun onPasswordChange(pwd: String) {
        _uiState.value = _uiState.value.copy(password = pwd, errorMessage = null)
    }

    fun onConfirmPasswordChange(pwd: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = pwd, errorMessage = null)
    }

    fun onAgreeToTermsChange(agreed: Boolean) {
        _uiState.value = _uiState.value.copy(agreeToTerms = agreed, errorMessage = null)
    }

    fun submitSignUp(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.fullName.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter your full name")
            return
        }
        if (state.mobileNumber.length < 10) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid 10-digit mobile number")
            return
        }
        if (!state.email.contains("@")) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid email address")
            return
        }
        if (state.password.length < 6) {
            _uiState.value = state.copy(errorMessage = "Password must be at least 6 characters")
            return
        }
        if (state.password != state.confirmPassword) {
            _uiState.value = state.copy(errorMessage = "Passwords do not match")
            return
        }
        if (!state.agreeToTerms) {
            _uiState.value = state.copy(errorMessage = "Please agree to the Terms and Conditions")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)
            val result = authRepository.signUp(
                state.fullName,
                state.mobileNumber,
                state.email,
                state.password
            )
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRegisteredSuccess = true,
                    infoMessage = "Account created successfully! Please log in with your mobile number."
                )
                onSuccess()
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = err.message ?: "Sign up failed"
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, infoMessage = null)
    }
}
