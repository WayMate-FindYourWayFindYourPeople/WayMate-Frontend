package com.waymate.app.ui.screens.airport

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.data.model.FlightDirection
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.AirportViewModel

@Composable
fun FlightDetailsScreen(
    viewModel: AirportViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToChooseRideType: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val details = uiState.flightDetails

    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "Flight & Trip Details",
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
                    text = "Continue to Choose Ride Type",
                    onClick = onNavigateToChooseRideType
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
                text = "Enter Your Flight Info",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary,
                    fontSize = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "We will synchronize driver arrival times directly with radar flight status",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = WayMateTextSecondary,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Arrival / Departure Segmented Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(WayMateWhite)
                    .border(1.dp, WayMateBorder, RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                val isArrival = details.direction == FlightDirection.ARRIVAL
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isArrival) WayMateSky else Color.Transparent)
                        .clickable { viewModel.onDirectionChange(FlightDirection.ARRIVAL) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlightLand,
                            contentDescription = null,
                            tint = if (isArrival) Color.White else WayMateTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Flight Arrival (Landing)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isArrival) FontWeight.Bold else FontWeight.Medium,
                                color = if (isArrival) Color.White else WayMateTextSecondary
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (!isArrival) WayMateSky else Color.Transparent)
                        .clickable { viewModel.onDirectionChange(FlightDirection.DEPARTURE) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlightTakeoff,
                            contentDescription = null,
                            tint = if (!isArrival) Color.White else WayMateTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Flight Departure",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (!isArrival) FontWeight.Bold else FontWeight.Medium,
                                color = if (!isArrival) Color.White else WayMateTextSecondary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            WayMateCard(
                backgroundColor = WayMateWhite,
                cornerRadius = 18.dp
            ) {
                // Flight Number
                WayMateTextField(
                    value = details.flightNumber,
                    onValueChange = viewModel::onFlightNumberChange,
                    label = "Flight Number",
                    placeholder = "e.g. AI-804, 6E-2041, UK-992",
                    leadingIcon = {
                        Icon(Icons.Default.AirplanemodeActive, contentDescription = null, tint = WayMateSkyDark)
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Airline Display
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detected Airline:",
                        style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextMuted)
                    )
                    Text(
                        text = details.airline,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = WayMateGreenDark
                        )
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = WayMateDivider)

                // Airport Selector
                WayMateTextField(
                    value = details.airport,
                    onValueChange = viewModel::onAirportChange,
                    label = "Airport",
                    placeholder = "Select airport",
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = WayMateSkyDark)
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Terminal selector chips
                Text(
                    text = "Airport Terminal",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = WayMateTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.availableTerminals.forEach { term ->
                        val isSelected = term == uiState.selectedTerminal
                        WayMateFilterChip(
                            text = term,
                            isSelected = isSelected,
                            onClick = { viewModel.onTerminalChange(term) },
                            activeColor = WayMateSky,
                            activeTextColor = Color.White
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = WayMateDivider)

                // Date & Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    WayMateTextField(
                        value = details.flightDate,
                        onValueChange = viewModel::onDateChange,
                        modifier = Modifier.weight(1f),
                        label = "Flight Date",
                        leadingIcon = {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = WayMateGreenDark, modifier = Modifier.size(18.dp))
                        }
                    )

                    WayMateTextField(
                        value = details.flightTime,
                        onValueChange = viewModel::onTimeChange,
                        modifier = Modifier.weight(1f),
                        label = "Landing / Takeoff",
                        leadingIcon = {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = WayMateGreenDark, modifier = Modifier.size(18.dp))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Destination Address
                WayMateTextField(
                    value = details.destination,
                    onValueChange = viewModel::onDestinationChange,
                    label = if (details.direction == FlightDirection.ARRIVAL) "Drop-Off Destination" else "Pickup Origin",
                    placeholder = "Enter full address or landmark",
                    leadingIcon = {
                        Icon(Icons.Default.Home, contentDescription = null, tint = WayMateGreenDark)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Luggage counter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Luggage Quantity",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateTextPrimary
                            )
                        )
                        Text(
                            text = "Check-in trolley & bags",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = WayMateTextMuted,
                                fontSize = 12.sp
                            )
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        IconButton(
                            onClick = viewModel::decrementLuggage,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(WayMateCreamSurface)
                                .border(1.dp, WayMateBorder, CircleShape)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(16.dp))
                        }

                        Text(
                            text = details.luggageQuantity.toString(),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        IconButton(
                            onClick = viewModel::incrementLuggage,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(WayMateGreenContainer)
                                .border(1.dp, WayMateGreen, CircleShape)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = WayMateGreenDark, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
