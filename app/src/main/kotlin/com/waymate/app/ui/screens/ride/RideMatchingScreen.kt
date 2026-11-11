package com.waymate.app.ui.screens.ride

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.ui.components.WayMateCard
import com.waymate.app.ui.components.WayMateLogo
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.MatchingStep
import com.waymate.app.ui.viewmodel.RideViewModel

@Composable
fun RideMatchingScreen(
    viewModel: RideViewModel,
    onRideMatched: () -> Unit,
    onCancelRide: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startMatching {
            onRideMatched()
        }
    }

    // Animated radar ripples
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val rippleScale1 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple1"
    )
    val rippleAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha1"
    )

    val rippleScale2 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple2"
    )
    val rippleAlpha2 by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha2"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WayMateCream)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WayMateLogo(height = 36.dp)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(WayMateGreenContainer)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "CONNECTING...",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = WayMateGreenDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // Radar Animation Centerpiece
            Box(
                modifier = Modifier
                    .size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                // Expanding ripple 1
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .scale(rippleScale1)
                        .clip(CircleShape)
                        .background(WayMateGreen.copy(alpha = rippleAlpha1))
                )

                // Expanding ripple 2
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .scale(rippleScale2)
                        .clip(CircleShape)
                        .background(WayMateSky.copy(alpha = rippleAlpha2))
                )

                // Central pulsing vehicle sphere
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(WayMateWhite)
                        .border(3.dp, WayMateGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = WayMateGreenDark,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            // Matching Status Stepper Card
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = when (uiState.matchingStep) {
                        MatchingStep.FINDING_RIDE -> "Finding Your Ride..."
                        MatchingStep.SEARCHING_NEARBY_DRIVERS -> "Searching Nearby Verified Drivers..."
                        MatchingStep.MATCHING_ROUTE -> "Matching Route & Terminal Pickup..."
                        MatchingStep.CONFIRMING_VEHICLE -> "Confirming Vehicle..."
                        MatchingStep.COMPLETED -> "Driver Found!"
                    },
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateTextPrimary,
                        fontSize = 20.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${uiState.selectedVehicle?.name ?: "WayMate Cab"} • ${uiState.pickupLocation}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = WayMateTextSecondary,
                        fontSize = 13.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Linear animated progress indicator
                LinearProgressIndicator(
                    progress = { uiState.matchingProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = WayMateGreen,
                    trackColor = WayMateBorder.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Steps indicators
                WayMateCard(backgroundColor = WayMateWhite) {
                    MatchingStepRow(
                        title = "Finding your ride",
                        isCompleted = uiState.matchingStep.ordinal >= MatchingStep.FINDING_RIDE.ordinal,
                        isActive = uiState.matchingStep == MatchingStep.FINDING_RIDE
                    )
                    MatchingStepRow(
                        title = "Searching nearby drivers",
                        isCompleted = uiState.matchingStep.ordinal >= MatchingStep.SEARCHING_NEARBY_DRIVERS.ordinal,
                        isActive = uiState.matchingStep == MatchingStep.SEARCHING_NEARBY_DRIVERS
                    )
                    MatchingStepRow(
                        title = "Matching your route & luggage space",
                        isCompleted = uiState.matchingStep.ordinal >= MatchingStep.MATCHING_ROUTE.ordinal,
                        isActive = uiState.matchingStep == MatchingStep.MATCHING_ROUTE
                    )
                    MatchingStepRow(
                        title = "Confirming ride & dispatching driver",
                        isCompleted = uiState.matchingStep.ordinal >= MatchingStep.CONFIRMING_VEHICLE.ordinal,
                        isActive = uiState.matchingStep == MatchingStep.CONFIRMING_VEHICLE
                    )
                }
            }

            // Cancel Ride button
            OutlinedButton(
                onClick = onCancelRide,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, WayMateBorder),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = WayMateError)
            ) {
                Text(
                    text = "Cancel Search",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun MatchingStepRow(
    title: String,
    isCompleted: Boolean,
    isActive: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isCompleted && !isActive) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = WayMateGreen,
                modifier = Modifier.size(18.dp)
            )
        } else if (isActive) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = WayMateGreen,
                strokeWidth = 2.dp
            )
        } else {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(WayMateBorder.copy(alpha = 0.5f))
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                color = if (isActive) WayMateTextPrimary else if (isCompleted) WayMateTextSecondary else WayMateTextMuted,
                fontSize = 13.sp
            )
        )
    }
}
