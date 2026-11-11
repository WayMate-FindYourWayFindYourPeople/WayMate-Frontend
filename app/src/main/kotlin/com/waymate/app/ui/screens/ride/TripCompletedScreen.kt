package com.waymate.app.ui.screens.ride

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.RideViewModel

@Composable
fun TripCompletedScreen(
    viewModel: RideViewModel,
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val compliments = listOf("Clean Car", "Smooth Driving", "Polite Host", "On Time", "Eco Driver")

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WayMateWhite)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                WayMateButton(
                    text = "Submit Rating & Done",
                    onClick = onNavigateToHome
                )

                WayMateOutlinedButton(
                    text = "Download Tax Invoice / Receipt",
                    onClick = {
                        Toast.makeText(context, "Receipt downloaded to device storage", Toast.LENGTH_SHORT).show()
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Download, contentDescription = null, tint = WayMateTextPrimary)
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
                .padding(top = 20.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Success Checkmark Badge
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(WayMateGreenContainer)
                    .border(3.dp, WayMateGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = WayMateGreenDark,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Trip Completed!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary,
                    fontSize = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Hope you enjoyed your journey with WayMate",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = WayMateTextSecondary,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Route & Summary Card
            WayMateCard(backgroundColor = WayMateWhite) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Route Summary",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = WayMateTextPrimary
                        )
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(WayMateGreenContainer)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "COMPLETED",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = WayMateGreenDark,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                RouteTimeline(
                    pickup = uiState.pickupLocation,
                    destination = uiState.destinationLocation
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = WayMateDivider)

                // Stats row: Date, duration, distance
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Date & Time", style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextMuted))
                        Text(text = "${uiState.travelDate}, ${uiState.travelTime}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    }

                    Column {
                        Text(text = "Duration", style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextMuted))
                        Text(text = "${uiState.tripDurationMins} mins", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    }

                    Column {
                        Text(text = "Distance", style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextMuted))
                        Text(text = "${uiState.tripDistanceKm} km", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fare breakdown card
            WayMateCard(backgroundColor = WayMateWhite) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Paid",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = WayMateTextPrimary
                        )
                    )
                    Text(
                        text = "₹${uiState.tripFare.toInt()}",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = WayMateGreenDark
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Paid via", style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextMuted))
                    Text(text = uiState.selectedPaymentMethod, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Base Vehicle Fare", style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextMuted))
                    Text(text = "₹${(uiState.tripFare * 0.75).toInt()}", style = MaterialTheme.typography.bodySmall)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Tolls & Airport Surcharge", style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextMuted))
                    Text(text = "₹${(uiState.tripFare * 0.25).toInt()}", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rate Your Ride
            WayMateCard(backgroundColor = WayMateWhite) {
                Text(
                    text = "Rate Your Ride",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "How was your experience with ${uiState.assignedDriver?.name ?: "your driver"}?",
                    style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextSecondary)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Interactive 5 Star Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (star in 1..5) {
                        val isFilled = star <= uiState.ratingGiven
                        IconButton(onClick = { viewModel.setRating(star) }) {
                            Icon(
                                imageVector = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "$star stars",
                                tint = if (isFilled) WayMateWarning else WayMateBorder,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Compliments Chips
                Text(
                    text = "Give a compliment",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = WayMateTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    compliments.take(3).forEach { comp ->
                        val isSelected = uiState.selectedFeedbackTags.contains(comp)
                        WayMateFilterChip(
                            text = comp,
                            isSelected = isSelected,
                            onClick = { viewModel.toggleFeedbackTag(comp) },
                            activeColor = WayMateGreen,
                            activeTextColor = Color.White
                        )
                    }
                }
            }
        }
    }
}
