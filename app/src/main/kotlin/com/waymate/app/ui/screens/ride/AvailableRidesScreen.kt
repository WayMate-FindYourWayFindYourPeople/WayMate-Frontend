package com.waymate.app.ui.screens.ride

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.data.model.VehicleOption
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.RideViewModel

@Composable
fun AvailableRidesScreen(
    viewModel: RideViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToRideMatching: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }
    var showPaymentSelector by remember { mutableStateOf(false) }

    val paymentOptions = listOf(
        "WayMate Wallet (₹1,450.00)",
        "UPI (Google Pay / PhonePe)",
        "HDFC Credit Card (•• 8820)",
        "Cash on Delivery"
    )

    if (showPaymentSelector) {
        AlertDialog(
            onDismissRequest = { showPaymentSelector = false },
            title = {
                Text(
                    text = "Select Payment Method",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    paymentOptions.forEach { opt ->
                        val isSelected = opt == uiState.selectedPaymentMethod
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) WayMateGreenContainer else WayMateWhite)
                                .clickable {
                                    viewModel.selectPaymentMethod(opt)
                                    showPaymentSelector = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = opt,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) WayMateGreenDark else WayMateTextPrimary
                                )
                            )
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = WayMateGreenDark)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPaymentSelector = false }) {
                    Text("Close", color = WayMateGreenDark)
                }
            },
            containerColor = WayMateCream,
            shape = RoundedCornerShape(18.dp)
        )
    }

    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "Available Cabs",
                onBackClick = onNavigateBack
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WayMateWhite)
                    .padding(20.dp)
            ) {
                // Payment Method Selector Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(WayMateCreamSurface)
                        .clickable { showPaymentSelector = true }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = WayMateGreenDark,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = uiState.selectedPaymentMethod,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = WayMateTextPrimary
                            )
                        )
                    }

                    Text(
                        text = "Change",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = WayMateGreenDark
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Confirm Ride Button
                WayMateButton(
                    text = "Confirm ${uiState.selectedVehicle?.name ?: "Ride"} • ₹${uiState.selectedVehicle?.fare?.toInt() ?: 420}",
                    onClick = onNavigateToRideMatching
                )
            }
        },
        containerColor = WayMateCream
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Route Summary Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WayMateWhite)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                RouteTimeline(
                    pickup = uiState.pickupLocation,
                    destination = uiState.destinationLocation
                )
            }

            // Filters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Fastest", "Electric EV", "Spacious 6-Seat").forEach { filter ->
                    val isSelected = filter == selectedFilter
                    WayMateFilterChip(
                        text = filter,
                        isSelected = isSelected,
                        onClick = { selectedFilter = filter }
                    )
                }
            }

            // Vehicle Options List
            val filteredVehicles = when (selectedFilter) {
                "Fastest" -> uiState.availableVehicles.filter { it.isFastest || it.etaMinutes <= 3 }
                "Electric EV" -> uiState.availableVehicles.filter { it.isElectric }
                "Spacious 6-Seat" -> uiState.availableVehicles.filter { it.capacity >= 6 }
                else -> uiState.availableVehicles
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredVehicles) { vehicle ->
                    val isSelected = uiState.selectedVehicle?.id == vehicle.id
                    VehicleCard(
                        vehicle = vehicle,
                        isSelected = isSelected,
                        onSelect = { viewModel.selectVehicle(vehicle) }
                    )
                }
            }
        }
    }
}

@Composable
private fun VehicleCard(
    vehicle: VehicleOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onSelect() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) WayMateGreen else WayMateBorder,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) WayMateGreenContainer.copy(alpha = 0.25f) else WayMateWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when {
                                vehicle.isElectric -> WayMateGreenContainer
                                vehicle.capacity >= 6 -> WayMateDaisyContainer
                                else -> WayMateSkyContainer
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when {
                            vehicle.isElectric -> Icons.Default.ElectricCar
                            vehicle.capacity >= 6 -> Icons.Default.AirportShuttle
                            else -> Icons.Default.DirectionsCar
                        },
                        contentDescription = null,
                        tint = when {
                            vehicle.isElectric -> WayMateGreenDark
                            vehicle.capacity >= 6 -> WayMateDaisyDark
                            else -> WayMateSkyDark
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = vehicle.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateTextPrimary,
                                fontSize = 16.sp
                            )
                        )
                        if (vehicle.isPopular) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(WayMateGreen)
                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "POPULAR",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                    }

                    Text(
                        text = "${vehicle.etaMinutes} mins away • ${vehicle.capacity} seats • ${vehicle.luggageCapacity} bags",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = WayMateTextSecondary,
                            fontSize = 12.sp
                        )
                    )

                    Text(
                        text = vehicle.subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = WayMateTextMuted,
                            fontSize = 11.sp
                        ),
                        maxLines = 1
                    )
                }
            }

            // Fare column
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${vehicle.fare.toInt()}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateGreenDark,
                        fontSize = 19.sp
                    )
                )
                if (vehicle.originalFare != null) {
                    Text(
                        text = "₹${vehicle.originalFare.toInt()}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = WayMateTextMuted,
                            textDecoration = TextDecoration.LineThrough,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}
