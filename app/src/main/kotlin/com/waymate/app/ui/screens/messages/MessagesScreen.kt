package com.waymate.app.ui.screens.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.waymate.app.data.model.Conversation
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.MessageViewModel

@Composable
fun MessagesScreen(
    viewModel: MessageViewModel,
    onNavigateBack: () -> Unit,
    onOpenConversation: (Conversation) -> Unit,
    onBottomTabSelected: (BottomNavTab) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "Messages",
                onBackClick = onNavigateBack
            )
        },
        bottomBar = {
            WayMateBottomBar(
                currentTab = BottomNavTab.MESSAGES,
                onTabSelected = onBottomTabSelected
            )
        },
        containerColor = WayMateCream
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                WayMateTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Search drivers, pool hosts, or support...",
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = WayMateGreenDark)
                    }
                )
            }

            val filteredList = if (searchQuery.isBlank()) {
                uiState.conversations
            } else {
                uiState.conversations.filter {
                    it.participantName.contains(searchQuery, ignoreCase = true) ||
                    it.participantRole.contains(searchQuery, ignoreCase = true) ||
                    it.lastMessage.contains(searchQuery, ignoreCase = true)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList) { conv ->
                    ConversationCard(
                        conversation = conv,
                        onClick = {
                            viewModel.openConversation(conv)
                            onOpenConversation(conv)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ConversationCard(
    conversation: Conversation,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WayMateWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, WayMateBorder.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with Online dot
            Box {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                ) {
                    AsyncImage(
                        model = conversation.avatarUrl,
                        contentDescription = conversation.participantName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                if (conversation.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(WayMateGreen)
                            .border(2.dp, WayMateWhite, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.participantName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = WayMateTextPrimary,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = conversation.timestamp,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = WayMateTextMuted,
                            fontSize = 11.sp
                        )
                    )
                }

                Text(
                    text = conversation.participantRole,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = WayMateGreenDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = conversation.lastMessage,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (conversation.unreadCount > 0) WayMateTextPrimary else WayMateTextSecondary,
                        fontWeight = if (conversation.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 13.sp
                    ),
                    maxLines = 1
                )
            }

            if (conversation.unreadCount > 0) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(WayMateDaisy),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = conversation.unreadCount.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = WayMateBorder,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
