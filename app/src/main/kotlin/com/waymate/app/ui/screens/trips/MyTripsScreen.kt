package com.waymate.app.ui.screens.trips

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.waymate.app.data.model.Trip
import com.waymate.app.data.model.TripStatus
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.TripViewModel

@Composable
fun MyTripsScreen(
    viewModel: TripViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToLiveTracking: () -> Unit,
    onBottomTabSelected: (BottomNavTab) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var receiptTrip by remember { mutableStateOf<Trip?>(null) }

    if (receiptTrip != null) {
        val trip = receiptTrip!!
        AlertDialog(
            onDismissRequest = { receiptTrip = null },
            title = {
                Text(
                    text = "Receipt: ${trip.bookingReference}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Service: ${trip.rideType}")
                    Text("From: ${trip.pickup}")
                    Text("To: ${trip.destination}")
                    Text("Distance: ${trip.distanceKm} km • Duration: ${trip.durationMinutes} min")
                    HorizontalDivider(color = WayMateDivider)
                    Text("Base Fare: ₹${trip.baseFare.toInt()}")
                    Text("Tolls & Surcharges: ₹${trip.tollAndAirportTax.toInt()}")
                    Text("Discount: -₹${trip.discount.toInt()}", color = WayMateGreenDark)
                    Text("Total Paid: ₹${trip.fare.toInt()}", fontWeight = FontWeight.Bold, color = WayMateGreenDark)
                }
            },
            confirmButton = {
                Button(
                    onClick = { receiptTrip = null },
                    colors = ButtonDefaults.buttonColors(containerColor = WayMateGreen)
                ) {
                    Text("Done", color = Color.White)
                }
            },
            containerColor = WayMateCream,
            shape = RoundedCornerShape(18.dp)
        )
    }

    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "My Trips",
                onBackClick = onNavigateBack
            )
        },
        bottomBar = {
            WayMateBottomBar(
                currentTab = BottomNavTab.TRIPS,
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
            // Segmented Tabs: Upcoming | Completed | Cancelled
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(WayMateWhite)
                    .border(1.dp, WayMateBorder, RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                TripStatus.values().forEach { status ->
                    val isSelected = status == uiState.selectedStatus
                    val count = when (status) {
                        TripStatus.UPCOMING -> uiState.upcomingTrips.size
                        TripStatus.COMPLETED -> uiState.completedTrips.size
                        TripStatus.CANCELLED -> uiState.cancelledTrips.size
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) WayMateGreen else Color.Transparent)
                            .clickable { viewModel.setStatusFilter(status) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${status.name.lowercase().capitalize()} ($count)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else WayMateTextSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            val tripsToShow = when (uiState.selectedStatus) {
                TripStatus.UPCOMING -> uiState.upcomingTrips
                TripStatus.COMPLETED -> uiState.completedTrips
                TripStatus.CANCELLED -> uiState.cancelledTrips
            }

            if (tripsToShow.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = WayMateTextMuted,
                            modifier = Modifier.size(52.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "No ${uiState.selectedStatus.name.lowercase()} trips found",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Your travel history will appear here once booked.",
                            style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextSecondary)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(tripsToShow) { trip ->
                        TripCardItem(
                            trip = trip,
                            onTrackClick = onNavigateToLiveTracking,
                            onReceiptClick = { receiptTrip = trip },
                            onCancelClick = { viewModel.cancelTrip(trip.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TripCardItem(
    trip: Trip,
    onTrackClick: () -> Unit,
    onReceiptClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    WayMateCard(backgroundColor = WayMateWhite) {
        // Status Badge & Booking Ref
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = trip.bookingReference,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary
                )
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when (trip.status) {
                            TripStatus.UPCOMING -> WayMateGreenContainer
                            TripStatus.COMPLETED -> WayMateSkyContainer
                            TripStatus.CANCELLED -> Color(0xFFFFEAEA)
                        }
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = trip.status.name,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = when (trip.status) {
                            TripStatus.UPCOMING -> WayMateGreenDark
                            TripStatus.COMPLETED -> WayMateSkyDark
                            TripStatus.CANCELLED -> WayMateError
                        },
                        fontSize = 10.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Route Timeline
        RouteTimeline(
            pickup = trip.pickup,
            destination = trip.destination
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Driver & Vehicle row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                ) {
                    AsyncImage(
                        model = trip.driverPhoto,
                        contentDescription = trip.driverName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "${trip.driverName} • ${trip.vehicleModel}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = WayMateTextPrimary
                        )
                    )
                    Text(
                        text = "${trip.dateTime} • ${trip.rideType}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = WayMateTextMuted,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Text(
                text = "₹${trip.fare.toInt()}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateGreenDark
                )
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = WayMateDivider)

        // Action Buttons depending on status
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (trip.status) {
                TripStatus.UPCOMING -> {
                    TextButton(
                        onClick = onCancelClick,
                        colors = ButtonDefaults.textButtonColors(contentColor = WayMateError)
                    ) {
                        Text("Cancel Ride")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onTrackClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WayMateGreen),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Track Live", color = Color.White)
                    }
                }
                TripStatus.COMPLETED -> {
                    OutlinedButton(
                        onClick = onReceiptClick,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WayMateBorder),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("View Receipt", color = WayMateTextPrimary)
                    }
                }
                TripStatus.CANCELLED -> {
                    Text(
                        text = "Booking Cancelled",
                        style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextMuted)
                    )
                }
            }
        }
    }
}
