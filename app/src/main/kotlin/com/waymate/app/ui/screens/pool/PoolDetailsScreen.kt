package com.waymate.app.ui.screens.pool

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.waymate.app.data.model.PoolType
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.PoolViewModel

@Composable
fun PoolDetailsScreen(
    viewModel: PoolViewModel,
    onNavigateBack: () -> Unit,
    onJoinSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pool = uiState.selectedPool ?: return
    var showSuccessJoinDialog by remember { mutableStateOf(false) }

    if (showSuccessJoinDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessJoinDialog = false
                onJoinSuccess()
            },
            title = {
                Text(
                    text = "Joined Pool Successfully! 🎉",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateTextPrimary
                    )
                )
            },
            text = {
                Column {
                    Text(
                        text = "You are now booked for this shared transfer from ${pool.pickup} to ${pool.destination}.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = WayMateTextSecondary)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Departure: ${pool.date} at ${pool.time}. You can chat with ${pool.host.name} and co-passengers in your Messages tab.",
                        style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextMuted)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessJoinDialog = false
                        onJoinSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WayMateGreen)
                ) {
                    Text("View My Pools", color = Color.White)
                }
            },
            containerColor = WayMateCream,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "Pool Details",
                onBackClick = onNavigateBack
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WayMateWhite)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "₹${pool.farePerPerson.toInt()}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateGreenDark,
                                fontSize = 24.sp
                            )
                        )
                        Text(
                            text = "per seat (all incl.)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = WayMateTextMuted,
                                fontSize = 11.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    WayMateButton(
                        text = if (pool.isJoinedByMe) "Already Joined" else "Join Pool (${pool.availableSeats} left)",
                        onClick = {
                            viewModel.joinSelectedPool(1) {
                                showSuccessJoinDialog = true
                            }
                        },
                        enabled = !pool.isJoinedByMe && pool.availableSeats > 0,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
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
            // Badges & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (pool.poolType == PoolType.WOMEN_ONLY) {
                    WomenOnlyBadge()
                } else {
                    MixedPoolBadge()
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(WayMateGreenContainer)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${pool.availableSeats} Seats Available",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = WayMateGreenDark
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Route Card
            WayMateCard(backgroundColor = WayMateWhite) {
                Text(
                    text = "Route & Timeline",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                RouteTimeline(
                    pickup = pool.pickup,
                    destination = pool.destination,
                    pickupTime = pool.time,
                    destinationTime = pool.estimatedArrival
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = WayMateGreenDark, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = pool.date, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium))
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = WayMateGreenDark, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = pool.vehicleModel, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Host Information
            WayMateCard(backgroundColor = WayMateWhite) {
                Text(
                    text = "Pool Host",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(2.dp, WayMateGreen, CircleShape)
                    ) {
                        AsyncImage(
                            model = pool.host.avatarUrl,
                            contentDescription = pool.host.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = pool.host.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WayMateTextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "ID Verified",
                                tint = WayMateSky,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = "Verified Airport Commuter • 4.9 ★ Rating",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = WayMateTextSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = pool.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = WayMateTextSecondary,
                        lineHeight = 17.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Co-Passengers List
            WayMateCard(backgroundColor = WayMateWhite) {
                Text(
                    text = "Confirmed Co-Passengers (${pool.passengers.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                pool.passengers.forEach { passenger ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                        ) {
                            AsyncImage(
                                model = passenger.avatarUrl,
                                contentDescription = passenger.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = passenger.name,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = WayMateTextPrimary
                                )
                            )
                            Text(
                                text = if (passenger.isHost) "Pool Organizer" else "Passenger",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (passenger.isHost) WayMateGreenDark else WayMateTextMuted
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(WayMateCreamSurface)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "1 Seat",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = WayMateTextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fare breakdown card
            WayMateCard(
                backgroundColor = WayMateGreenContainer.copy(alpha = 0.5f),
                borderColor = WayMateGreen.copy(alpha = 0.4f),
                elevation = 0.dp
            ) {
                Text(
                    text = "Fare & Savings Breakdown",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateGreenDark
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Base Vehicle & Fuel Share", style = MaterialTheme.typography.bodySmall)
                    Text(text = "₹${(pool.farePerPerson * 0.75).toInt()}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Airport Entry & Toll Share", style = MaterialTheme.typography.bodySmall)
                    Text(text = "₹${(pool.farePerPerson * 0.25).toInt()}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = WayMateGreen.copy(alpha = 0.3f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "You Save Compared to Solo Cab",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = WayMateGreenDark)
                    )
                    Text(
                        text = "₹${(pool.regularSoloFare - pool.farePerPerson).toInt()} (Save 45%)",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = WayMateGreenDark)
                    )
                }
            }
        }
    }
}
