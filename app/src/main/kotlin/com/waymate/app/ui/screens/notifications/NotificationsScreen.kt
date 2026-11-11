package com.waymate.app.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.data.model.NotificationItem
import com.waymate.app.data.model.NotificationType
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.NotificationViewModel

@Composable
fun NotificationsScreen(
    viewModel: NotificationViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val filterTabs = listOf("All", "Rides", "Pools", "Payments")

    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "Notifications",
                onBackClick = onNavigateBack,
                actions = {
                    TextButton(onClick = viewModel::clearAll) {
                        Text(
                            "Clear All",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = WayMateTextMuted,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            )
        },
        containerColor = WayMateCream
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterTabs.forEach { tab ->
                    val isSelected = tab == uiState.selectedFilter
                    WayMateFilterChip(
                        text = tab,
                        isSelected = isSelected,
                        onClick = { viewModel.setFilter(tab) }
                    )
                }
            }

            val filteredNotifications = when (uiState.selectedFilter) {
                "Rides" -> uiState.notifications.filter { it.type == NotificationType.RIDE_CONFIRMATION || it.type == NotificationType.DRIVER_ARRIVAL }
                "Pools" -> uiState.notifications.filter { it.type == NotificationType.POOL_UPDATE }
                "Payments" -> uiState.notifications.filter { it.type == NotificationType.PAYMENT }
                else -> uiState.notifications
            }

            if (filteredNotifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = null,
                            tint = WayMateTextMuted,
                            modifier = Modifier.size(52.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No notifications yet",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateTextPrimary
                            )
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredNotifications) { item ->
                        NotificationCardItem(
                            item = item,
                            onClick = { viewModel.markAsRead(item.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCardItem(
    item: NotificationItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!item.isRead) WayMateWhite else WayMateWhite.copy(alpha = 0.8f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (!item.isRead) 1.5.dp else 1.dp,
            color = if (!item.isRead) WayMateGreen.copy(alpha = 0.6f) else WayMateBorder.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (!item.isRead) 2.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            val (icon: ImageVector, color: Color) = when (item.type) {
                NotificationType.DRIVER_ARRIVAL -> Icons.Default.DirectionsCar to WayMateGreen
                NotificationType.RIDE_CONFIRMATION -> Icons.Default.CheckCircle to WayMateSky
                NotificationType.POOL_UPDATE -> Icons.Default.Groups to WayMateDaisy
                NotificationType.PAYMENT -> Icons.Default.Payment to WayMateGreenDark
                NotificationType.REMINDER -> Icons.Default.Alarm to WayMateWarning
                NotificationType.PROMOTION -> Icons.Default.LocalOffer to WayMateDaisyDark
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (!item.isRead) FontWeight.Bold else FontWeight.SemiBold,
                            color = WayMateTextPrimary,
                            fontSize = 14.sp
                        ),
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = item.timestamp,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = WayMateTextMuted,
                            fontSize = 11.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = item.message,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (!item.isRead) WayMateTextPrimary else WayMateTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                )
            }

            if (!item.isRead) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(WayMateGreen)
                )
            }
        }
    }
}
