package com.waymate.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
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
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToChooseRide: () -> Unit,
    onNavigateToAirportRide: () -> Unit,
    onNavigateToPools: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToLiveTracking: () -> Unit,
    onBottomTabSelected: (BottomNavTab) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showOfferRideDialog by remember { mutableStateOf(false) }

    if (showOfferRideDialog) {
        AlertDialog(
            onDismissRequest = { showOfferRideDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(WayMateDaisyContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = WayMateDaisyDark
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Offer a Ride",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = WayMateTextPrimary
                        )
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Driver & Host mode is coming soon in the upcoming update!",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = WayMateTextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Soon you will be able to verify your vehicle, list empty seats during your airport commute, and split fuel costs legally and seamlessly.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = WayMateTextSecondary,
                            lineHeight = 18.sp
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showOfferRideDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = WayMateGreen)
                ) {
                    Text("Got It", color = Color.White)
                }
            },
            containerColor = WayMateWhite,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        bottomBar = {
            WayMateBottomBar(
                currentTab = BottomNavTab.HOME,
                onTabSelected = onBottomTabSelected,
                unreadMessagesCount = uiState.unreadMessagesCount
            )
        },
        containerColor = WayMateCream
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 24.dp)
        ) {
            // Header: Official Logo + Greeting + Notifications Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WayMateBrandHeader(
                    logoHeight = 40.dp,
                    showTagline = true
                )

                // Notification Icon with unread badge
                IconButton(
                    onClick = onNavigateToNotifications,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(WayMateWhite)
                ) {
                    BadgedBox(
                        badge = {
                            if (uiState.unreadNotificationsCount > 0) {
                                Badge(
                                    containerColor = WayMateDaisy,
                                    contentColor = Color.White
                                ) {
                                    Text(uiState.unreadNotificationsCount.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = WayMateTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Greeting Card
            WayMateCard(
                backgroundColor = WayMateWhite,
                cornerRadius = 18.dp
            ) {
                Text(
                    text = "Hello, ${uiState.user.fullName.split(" ").firstOrNull() ?: "Traveller"} 👋",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateTextPrimary,
                        fontSize = 20.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Where are you heading today?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = WayMateTextSecondary,
                        fontSize = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Search Destination Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(WayMateCreamSurface)
                        .clickable { onNavigateToChooseRide() }
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null,
                            tint = WayMateGreenDark
                        )
                        Text(
                            text = if (uiState.searchDestinationQuery.isEmpty()) "Search airport, terminal or city drop-off..." else uiState.searchDestinationQuery,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (uiState.searchDestinationQuery.isEmpty()) WayMateTextMuted else WayMateTextPrimary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section: Main Passenger Quick Actions Grid
            Text(
                text = "Ride Services",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary,
                    fontSize = 17.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Book a Ride (Solo/Private)
                ActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Book a Ride",
                    subtitle = "Private & Solo",
                    icon = Icons.Default.DirectionsCar,
                    accentColor = WayMateGreen,
                    backgroundColor = WayMateWhite,
                    onClick = onNavigateToChooseRide
                )

                // Airport Ride
                ActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Airport Ride",
                    subtitle = "Flight Transfers",
                    icon = Icons.Default.AirplanemodeActive,
                    accentColor = WayMateSky,
                    backgroundColor = WayMateWhite,
                    onClick = onNavigateToAirportRide
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Share a Ride / Pool
                ActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Share a Ride",
                    subtitle = "Pool & Split Fare",
                    icon = Icons.Default.Groups,
                    accentColor = WayMateDaisy,
                    backgroundColor = WayMateWhite,
                    badgeText = "Save 45%",
                    badgeColor = WayMateDaisy,
                    onClick = onNavigateToPools
                )

                // Offer a Ride (Coming soon as instructed)
                ActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Offer a Ride",
                    subtitle = "Host a Carpool",
                    icon = Icons.Default.AddRoad,
                    accentColor = WayMateTextMuted,
                    backgroundColor = WayMateWhite,
                    badgeText = "Coming Soon",
                    badgeColor = WayMateTextMuted,
                    onClick = { showOfferRideDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Travel Promotional Banner
            WayMateCard(
                backgroundColor = WayMateGreenContainer,
                borderColor = WayMateGreen.copy(alpha = 0.5f),
                cornerRadius = 16.dp,
                elevation = 0.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(WayMateGreen)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "AIRPORT SPECIAL",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Save up to 45% on Shared Pools",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateTextPrimary,
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Zero surge pricing • Free 45-min wait for delayed flights",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = WayMateTextSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(WayMateWhite),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlightTakeoff,
                            contentDescription = null,
                            tint = WayMateGreenDark,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Upcoming Ride Information Card
            Text(
                text = "Upcoming Ride",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary,
                    fontSize = 17.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            val upcoming = uiState.upcomingRide
            if (upcoming != null) {
                WayMateCard(
                    backgroundColor = WayMateWhite,
                    cornerRadius = 16.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(WayMateSkyContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = null,
                                    tint = WayMateSky,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = upcoming.selectedVehicle?.name ?: "WayMate Cab",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = WayMateTextPrimary
                                    )
                                )
                                Text(
                                    text = "${upcoming.date} • ${upcoming.time}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = WayMateTextMuted,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(WayMateGreenContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "CONFIRMED",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = WayMateGreenDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    RouteTimeline(
                        pickup = upcoming.pickup,
                        destination = upcoming.destination,
                        pickupTime = upcoming.time,
                        destinationTime = "11:42 AM"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "OTP: ${upcoming.assignedDriver?.startOtp ?: "4821"}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WayMateTextPrimary
                                )
                            )
                            Text(
                                text = "Share with driver at start",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = WayMateTextMuted,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Button(
                            onClick = onNavigateToLiveTracking,
                            colors = ButtonDefaults.buttonColors(containerColor = WayMateGreen),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Navigation,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Live Track",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeText: String? = null,
    badgeColor: Color = WayMateDaisy
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, WayMateBorder.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (badgeText != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(badgeColor.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = badgeColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary,
                    fontSize = 15.sp
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = WayMateTextSecondary,
                    fontSize = 12.sp
                )
            )
        }
    }
}
