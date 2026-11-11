package com.waymate.app.data.repository

import com.waymate.app.data.model.ChatMessage
import com.waymate.app.data.model.Conversation
import com.waymate.app.data.model.NotificationItem
import com.waymate.app.data.model.NotificationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface MessageRepository {
    val conversations: StateFlow<List<Conversation>>
    fun getMessagesForConversation(convId: String): List<ChatMessage>
    suspend fun sendMessage(convId: String, text: String): ChatMessage
}

class MockMessageRepository : MessageRepository {

    private val _conversations = MutableStateFlow(
        listOf(
            Conversation(
                id = "conv_1",
                participantName = "Rajesh Verma",
                participantRole = "Driver • Toyota Hyryder (DL 01 AB 4022)",
                avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
                lastMessage = "I have reached outside Pillar 14 near Gate 4. Waiting in hazard lights.",
                timestamp = "11:12 AM",
                unreadCount = 1,
                isOnline = true
            ),
            Conversation(
                id = "conv_2",
                participantName = "Priya Sharma",
                participantRole = "Pool Host • IGI T3 to Cyber Hub",
                avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80",
                lastMessage = "Hey Alex! Ananya and I are near the baggage claim belt 6. Meet at the cab bay?",
                timestamp = "Yesterday",
                unreadCount = 0,
                isOnline = false
            ),
            Conversation(
                id = "conv_3",
                participantName = "WayMate Support",
                participantRole = "Official Support 24x7",
                avatarUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=300&q=80",
                lastMessage = "Your receipt for trip WM-MUM-7731 has been sent to your registered email.",
                timestamp = "Oct 28",
                unreadCount = 0,
                isOnline = true
            )
        )
    )
    override val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    private val messagesMap = mutableMapOf(
        "conv_1" to mutableListOf(
            ChatMessage("m1", "Rajesh Verma", "Hello Alex, I've accepted your ride to Cyber City.", "11:05 AM", false),
            ChatMessage("m2", "Alex Morgan", "Hi Rajesh! I just walked out of Terminal 3 Arrivals.", "11:08 AM", true),
            ChatMessage("m3", "Rajesh Verma", "Great. Please walk towards Lane 2, Commercial Pickup Bay.", "11:09 AM", false),
            ChatMessage("m4", "Alex Morgan", "Almost there, near Pillar 14.", "11:11 AM", true),
            ChatMessage("m5", "Rajesh Verma", "I have reached outside Pillar 14 near Gate 4. Waiting in hazard lights.", "11:12 AM", false)
        ),
        "conv_2" to mutableListOf(
            ChatMessage("m21", "Priya Sharma", "Welcome to the T3 shared pool!", "Yesterday", false),
            ChatMessage("m22", "Alex Morgan", "Thanks Priya, looking forward to saving costs together.", "Yesterday", true),
            ChatMessage("m23", "Priya Sharma", "Hey Alex! Ananya and I are near the baggage claim belt 6. Meet at the cab bay?", "Yesterday", false)
        ),
        "conv_3" to mutableListOf(
            ChatMessage("m31", "WayMate Support", "Hello! Welcome to WayMate smart airport transfers.", "Oct 28", false),
            ChatMessage("m32", "WayMate Support", "Your receipt for trip WM-MUM-7731 has been sent to your registered email.", "Oct 28", false)
        )
    )

    override fun getMessagesForConversation(convId: String): List<ChatMessage> {
        return messagesMap[convId] ?: emptyList()
    }

    override suspend fun sendMessage(convId: String, text: String): ChatMessage {
        val newMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderName = "Alex Morgan",
            text = text,
            timestamp = "Just now",
            isFromMe = true
        )
        val list = messagesMap.getOrPut(convId) { mutableListOf() }
        list.add(newMsg)
        return newMsg
    }
}

interface NotificationRepository {
    val notifications: StateFlow<List<NotificationItem>>
    suspend fun markAsRead(id: String)
    suspend fun clearAll()
}

class MockNotificationRepository : NotificationRepository {

    private val initialNotifications = listOf(
        NotificationItem(
            id = "n_1",
            title = "Driver Arriving in 3 Mins",
            message = "Rajesh Verma in Toyota Hyryder (DL 01 AB 4022) is approaching Terminal 3 Pillar 14.",
            timestamp = "3m ago",
            type = NotificationType.DRIVER_ARRIVAL,
            isRead = false
        ),
        NotificationItem(
            id = "n_2",
            title = "Ride Confirmed!",
            message = "Your booking from IGI T3 to Cyber City Gurugram is locked at ₹420. OTP is 4821.",
            timestamp = "12m ago",
            type = NotificationType.RIDE_CONFIRMATION,
            isRead = false
        ),
        NotificationItem(
            id = "n_3",
            title = "New Co-Passenger Joined Your Pool",
            message = "Ananya joined your pool 'IGI T3 to Cyber Hub'. 2 seats remaining.",
            timestamp = "2h ago",
            type = NotificationType.POOL_UPDATE,
            isRead = true
        ),
        NotificationItem(
            id = "n_4",
            title = "Flight Delay Protection Active",
            message = "Flight AI-804 on-time status monitored. Your driver wait time is free up to 45 mins.",
            timestamp = "Yesterday",
            type = NotificationType.REMINDER,
            isRead = true
        ),
        NotificationItem(
            id = "n_5",
            title = "Payment Successful: ₹540",
            message = "Trip WM-MUM-7731 completed. You saved 3.2 kg CO2 with shared routing.",
            timestamp = "Oct 28",
            type = NotificationType.PAYMENT,
            isRead = true
        ),
        NotificationItem(
            id = "n_6",
            title = "Airport Transfer Perk: 20% Off",
            message = "Use code AIRWAY20 on your next Bengaluru or Delhi airport pool ride.",
            timestamp = "Oct 25",
            type = NotificationType.PROMOTION,
            isRead = true
        )
    )

    private val _notifications = MutableStateFlow(initialNotifications)
    override val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    override suspend fun markAsRead(id: String) {
        val list = _notifications.value.toMutableList()
        val index = list.indexOfFirst { it.id == id }
        if (index != -1) {
            list[index] = list[index].copy(isRead = true)
            _notifications.value = list
        }
    }

    override suspend fun clearAll() {
        _notifications.value = emptyList()
    }
}
