package com.waymate.app.ui.screens.ride

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.RideViewModel

@Composable
fun BookRideScreen(
    viewModel: RideViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAvailableRides: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "Book a Ride",
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
                WayMateButton(
                    text = "Find Rides",
                    onClick = onNavigateToAvailableRides,
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                    }
                )
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
            Text(
                text = "Trip Details",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary,
                    fontSize = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pickup & Destination card with swap button
            WayMateCard(
                backgroundColor = WayMateWhite,
                cornerRadius = 20.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Pickup
                        WayMateTextField(
                            value = uiState.pickupLocation,
                            onValueChange = viewModel::onPickupChange,
                            label = "Pickup Location",
                            placeholder = "Enter pickup point or airport terminal",
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(WayMateSky)
                                )
                            }
                        )

                        // Destination
                        WayMateTextField(
                            value = uiState.destinationLocation,
                            onValueChange = viewModel::onDestinationChange,
                            label = "Destination",
                            placeholder = "Enter drop-off location",
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(WayMateGreen)
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Swap locations button
                    IconButton(
                        onClick = viewModel::swapLocations,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(WayMateCreamSurface)
                            .border(1.dp, WayMateBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Swap Locations",
                            tint = WayMateGreenDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Travel Date & Time
            Text(
                text = "Schedule",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Date picker trigger
                WayMateCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = WayMateWhite
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = WayMateGreenDark,
                            modifier = Modifier.size(18.dp)
                        )
                        Column {
                            Text(
                                text = "Date",
                                style = MaterialTheme.typography.labelSmall.copy(color = WayMateTextMuted)
                            )
                            Text(
                                text = uiState.travelDate,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WayMateTextPrimary
                                )
                            )
                        }
                    }
                }

                // Time picker trigger
                WayMateCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = WayMateWhite
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = WayMateGreenDark,
                            modifier = Modifier.size(18.dp)
                        )
                        Column {
                            Text(
                                text = "Time",
                                style = MaterialTheme.typography.labelSmall.copy(color = WayMateTextMuted)
                            )
                            Text(
                                text = uiState.travelTime,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WayMateTextPrimary
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Passengers & Luggage Counters
            Text(
                text = "Passengers & Luggage",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            WayMateCard(
                backgroundColor = WayMateWhite,
                cornerRadius = 18.dp
            ) {
                // Passengers Counter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(WayMateGreenContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = WayMateGreenDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Passengers",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WayMateTextPrimary
                                )
                            )
                            Text(
                                text = "Ages 12+ or lap infants",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = WayMateTextMuted,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    CounterControls(
                        count = uiState.passengerCount,
                        onDecrement = viewModel::decrementPassengers,
                        onIncrement = viewModel::incrementPassengers
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 14.dp),
                    color = WayMateDivider
                )

                // Luggage Counter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(WayMateSkyContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Luggage,
                                contentDescription = null,
                                tint = WayMateSkyDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Luggage Bags",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WayMateTextPrimary
                                )
                            )
                            Text(
                                text = "Check-in or large trolley bags",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = WayMateTextMuted,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    CounterControls(
                        count = uiState.luggageCount,
                        onDecrement = viewModel::decrementLuggage,
                        onIncrement = viewModel::incrementLuggage
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Airport Guarantee Note
            WayMateCard(
                backgroundColor = WayMateCreamSurface,
                borderColor = WayMateBorder,
                elevation = 0.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = WayMateGreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Guaranteed luggage trunk space and terminal pickup zone navigation included.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = WayMateTextSecondary,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun CounterControls(
    count: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        IconButton(
            onClick = onDecrement,
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(WayMateCreamSurface)
                .border(1.dp, WayMateBorder, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrement",
                tint = WayMateTextPrimary,
                modifier = Modifier.size(16.dp)
            )
        }

        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = WayMateTextPrimary,
                fontSize = 16.sp
            ),
            modifier = Modifier.width(18.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        IconButton(
            onClick = onIncrement,
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(WayMateGreenContainer)
                .border(1.dp, WayMateGreen, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increment",
                tint = WayMateGreenDark,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
