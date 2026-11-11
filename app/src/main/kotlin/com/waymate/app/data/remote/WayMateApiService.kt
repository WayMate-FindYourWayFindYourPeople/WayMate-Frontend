package com.waymate.app.data.remote

import com.waymate.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

data class SendOtpRequest(val mobileNumber: String, val countryCode: String = "+91")
data class SendOtpResponse(val success: Boolean, val message: String, val testOtpHint: String?)
data class VerifyOtpRequest(val mobileNumber: String, val otp: String)
data class AuthResponse(val success: Boolean, val token: String?, val user: User?)
data class SignUpRequest(
    val fullName: String,
    val phone: String,
    val email: String,
    val password: String
)

interface WayMateApiService {
    @POST("auth/send-otp")
    suspend fun sendOtp(@Body request: SendOtpRequest): Response<SendOtpResponse>

    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<AuthResponse>

    @POST("auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthResponse>

    @GET("rides/available")
    suspend fun getAvailableRides(
        @Query("pickup") pickup: String,
        @Query("destination") destination: String
    ): Response<List<VehicleOption>>

    @GET("pools/available")
    suspend fun getAvailablePools(
        @Query("type") poolType: String? = null
    ): Response<List<PoolItem>>

    @POST("pools/create")
    suspend fun createPool(@Body pool: PoolItem): Response<PoolItem>

    @GET("trips")
    suspend fun getTrips(@Query("status") status: String? = null): Response<List<Trip>>
}
