package com.waymate.app.data.repository

import com.waymate.app.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface AuthRepository {
    val currentUser: StateFlow<User?>
    val isAuthenticated: StateFlow<Boolean>

    suspend fun sendOtp(phone: String, countryCode: String = "+91"): Result<String>
    suspend fun verifyOtp(phone: String, otp: String): Result<User>
    suspend fun signUp(fullName: String, phone: String, email: String, password: String): Result<User>
    suspend fun logout()
    fun getRegisteredPhone(): String?
}

class MockAuthRepository : AuthRepository {
    private val _currentUser = MutableStateFlow<User?>(User())
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    override val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private var registeredPhone: String? = null
    private val validDemoOtp = "1234"

    override fun getRegisteredPhone(): String? = registeredPhone

    override suspend fun sendOtp(phone: String, countryCode: String): Result<String> {
        delay(600) // realistic network simulation
        val cleanPhone = phone.trim().replace(" ", "").replace("-", "")
        if (cleanPhone.length < 10) {
            return Result.failure(IllegalArgumentException("Please enter a valid 10-digit mobile number"))
        }
        registeredPhone = cleanPhone
        // Return clear demo hint without claiming real SMS went out without a gateway
        return Result.success("Demo OTP sent. Use 1234 to verify.")
    }

    override suspend fun verifyOtp(phone: String, otp: String): Result<User> {
        delay(500)
        if (otp.trim() == validDemoOtp) {
            val user = User(
                id = "usr_${System.currentTimeMillis() % 10000}",
                fullName = "Alex Morgan",
                phone = "+91 ${phone.takeLast(10)}",
                email = "alex.morgan@waymate.com",
                rating = 4.94,
                totalTrips = 38,
                poolsShared = 14,
                co2SavedKg = 42.5,
                isVerified = true
            )
            _currentUser.value = user
            _isAuthenticated.value = true
            return Result.success(user)
        } else {
            return Result.failure(IllegalArgumentException("Invalid OTP code. Please enter 1234."))
        }
    }

    override suspend fun signUp(
        fullName: String,
        phone: String,
        email: String,
        password: String
    ): Result<User> {
        delay(700)
        if (fullName.isBlank()) return Result.failure(IllegalArgumentException("Please enter your full name"))
        if (phone.trim().length < 10) return Result.failure(IllegalArgumentException("Please enter a valid phone number"))
        if (!email.contains("@")) return Result.failure(IllegalArgumentException("Please enter a valid email address"))
        if (password.length < 6) return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))

        val cleanPhone = phone.trim().replace(" ", "").replace("-", "")
        registeredPhone = cleanPhone

        val newUser = User(
            id = "usr_new_${System.currentTimeMillis() % 10000}",
            fullName = fullName.trim(),
            phone = "+91 ${cleanPhone.takeLast(10)}",
            email = email.trim(),
            rating = 5.0,
            totalTrips = 0,
            poolsShared = 0,
            co2SavedKg = 0.0,
            isVerified = true
        )
        // Store for subsequent login step
        return Result.success(newUser)
    }

    override suspend fun logout() {
        _currentUser.value = null
        _isAuthenticated.value = false
    }
}
