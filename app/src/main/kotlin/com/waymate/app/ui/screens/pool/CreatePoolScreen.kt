package com.waymate.app.ui.screens.pool

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
import com.waymate.app.data.model.PoolType
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.PoolViewModel

@Composable
fun CreatePoolScreen(
    viewModel: PoolViewModel,
    onNavigateBack: () -> Unit,
    onPoolCreated: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "Create a Ride Pool",
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
                    text = "Create Pool & Invite Passengers",
                    onClick = {
                        viewModel.submitCreatePool {
                            onPoolCreated()
                        }
                    },
                    isLoading = uiState.isCreating
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
                text = "Host a Shared Pool",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary,
                    fontSize = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Offer empty seats during your airport commute to split fares legally and cut travel costs.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = WayMateTextSecondary,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Pickup & Destination
            WayMateCard(backgroundColor = WayMateWhite) {
                WayMateTextField(
                    value = uiState.createPickup,
                    onValueChange = viewModel::onCreatePickupChange,
                    label = "Pickup Origin",
                    placeholder = "e.g. Terminal 3 Arrivals Pillar 14",
                    leadingIcon = {
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(WayMateSky))
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                WayMateTextField(
                    value = uiState.createDestination,
                    onValueChange = viewModel::onCreateDestinationChange,
                    label = "Drop-off Destination",
                    placeholder = "e.g. Cyber City Gurugram or Indiranagar",
                    leadingIcon = {
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(WayMateGreen))
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date & Departure Time
            WayMateCard(backgroundColor = WayMateWhite) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    WayMateTextField(
                        value = uiState.createDate,
                        onValueChange = viewModel::onCreateDateChange,
                        modifier = Modifier.weight(1f),
                        label = "Date",
                        leadingIcon = {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = WayMateGreenDark, modifier = Modifier.size(18.dp))
                        }
                    )

                    WayMateTextField(
                        value = uiState.createTime,
                        onValueChange = viewModel::onCreateTimeChange,
                        modifier = Modifier.weight(1f),
                        label = "Departure Time",
                        leadingIcon = {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = WayMateGreenDark, modifier = Modifier.size(18.dp))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Available Seats Offered
            WayMateCard(backgroundColor = WayMateWhite) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Available Seats Offered",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateTextPrimary
                            )
                        )
                        Text(
                            text = "Excluding yourself as host",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = WayMateTextMuted,
                                fontSize = 12.sp
                            )
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (s in 1..4) {
                            val isSelected = s == uiState.createAvailableSeats
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) WayMateGreen else WayMateCreamSurface)
                                    .border(1.dp, if (isSelected) WayMateGreen else WayMateBorder, RoundedCornerShape(10.dp))
                                    .clickable { viewModel.onCreateSeatsChange(s) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = s.toString(),
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else WayMateTextPrimary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Women Only vs Mixed Preference
            Text(
                text = "Pool Preference",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val isWomenOnly = uiState.createPoolType == PoolType.WOMEN_ONLY
                WayMateCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.onCreatePoolTypeChange(PoolType.WOMEN_ONLY) },
                    backgroundColor = if (isWomenOnly) WayMateDaisyContainer else WayMateWhite,
                    borderColor = if (isWomenOnly) WayMateDaisy else WayMateBorder
                ) {
                    Icon(
                        imageVector = Icons.Default.Female,
                        contentDescription = null,
                        tint = WayMateDaisyDark,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Women Only",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isWomenOnly) WayMateDaisyDark else WayMateTextPrimary
                        )
                    )
                    Text(
                        text = "Female co-passengers only",
                        style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextMuted, fontSize = 11.sp)
                    )
                }

                val isMixed = uiState.createPoolType == PoolType.MIXED
                WayMateCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.onCreatePoolTypeChange(PoolType.MIXED) },
                    backgroundColor = if (isMixed) WayMateSkyContainer else WayMateWhite,
                    borderColor = if (isMixed) WayMateSky else WayMateBorder
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = WayMateSkyDark,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Mixed Group",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isMixed) WayMateSkyDark else WayMateTextPrimary
                        )
                    )
                    Text(
                        text = "Open to all verified passengers",
                        style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextMuted, fontSize = 11.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Estimated Fare Calculation Card
            WayMateCard(
                backgroundColor = WayMateGreenContainer,
                borderColor = WayMateGreen.copy(alpha = 0.5f),
                elevation = 0.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Estimated Fare Per Seat",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateGreenDark
                            )
                        )
                        Text(
                            text = "Calculated automatically by route distance & tolls",
                            style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextSecondary, fontSize = 12.sp)
                        )
                    }

                    Text(
                        text = "₹${uiState.createEstimatedFarePerSeat.toInt()}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = WayMateGreenDark,
                            fontSize = 24.sp
                        )
                    )
                }
            }
        }
    }
}
