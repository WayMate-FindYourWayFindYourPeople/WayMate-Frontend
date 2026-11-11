package com.waymate.app.data.model

data class User(
    val id: String = "u_101",
    val fullName: String = "Alex Morgan",
    val phone: String = "+91 98765 43210",
    val email: String = "alex.morgan@waymate.com",
    val avatarUrl: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
    val rating: Double = 4.92,
    val totalTrips: Int = 38,
    val poolsShared: Int = 14,
    val co2SavedKg: Double = 42.5,
    val isVerified: Boolean = true,
    val preferredRideType: String = "Mixed"
)
