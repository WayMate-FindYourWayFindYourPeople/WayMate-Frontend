package com.waymate.app.data.model

data class ChatMessage(
    val id: String,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean
)

data class Conversation(
    val id: String,
    val participantName: String,
    val participantRole: String, // "Driver (Toyota Hyryder)", "Pool Host (BLR -> Whitefield)", "WayMate Support"
    val avatarUrl: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = true
)

enum class NotificationType {
    RIDE_CONFIRMATION,
    POOL_UPDATE,
    DRIVER_ARRIVAL,
    PAYMENT,
    REMINDER,
    PROMOTION
}

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val type: NotificationType,
    val isRead: Boolean = false,
    val actionDeepLink: String? = null
)
