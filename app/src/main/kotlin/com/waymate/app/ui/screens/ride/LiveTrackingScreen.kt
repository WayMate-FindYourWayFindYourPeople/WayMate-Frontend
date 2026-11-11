package com.waymate.app.ui.screens.ride

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.RideViewModel

@Composable
fun LiveTrackingScreen(
    viewModel: RideViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToChat: () -> Unit,
    onTripCompleted: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val driver = uiState.assignedDriver
    val context = LocalContext.current

    // Animated car movement along path
    val infiniteTransition = rememberInfiniteTransition(label = "carMove")
    val carProgress by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "carProgress"
    )

    Scaffold(
        containerColor = Color(0xFFF3F6EC)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Mock Vector Map Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Background soft grid representing roads/city blocks
                val gridColor = Color(0xFFE2E7D5)
                val roadColor = Color(0xFFFFFFFF)

                // Draw secondary street grid
                for (x in 0..canvasWidth.toInt() step 80) {
                    drawLine(
                        color = gridColor,
                        start = Offset(x.toFloat(), 0f),
                        end = Offset(x.toFloat(), canvasHeight),
                        strokeWidth = 2f
                    )
                }
                for (y in 0..canvasHeight.toInt() step 80) {
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y.toFloat()),
                        end = Offset(canvasWidth, y.toFloat()),
                        strokeWidth = 2f
                    )
                }

                // Major highways
                drawLine(
                    color = roadColor,
                    start = Offset(0f, canvasHeight * 0.35f),
                    end = Offset(canvasWidth, canvasHeight * 0.45f),
                    strokeWidth = 20f
                )
                drawLine(
                    color = roadColor,
                    start = Offset(canvasWidth * 0.3f, 0f),
                    end = Offset(canvasWidth * 0.7f, canvasHeight),
                    strokeWidth = 24f
                )

                // Route Polyline
                val routePath = Path().apply {
                    moveTo(canvasWidth * 0.25f, canvasHeight * 0.28f)
                    cubicTo(
                        canvasWidth * 0.45f, canvasHeight * 0.32f,
                        canvasWidth * 0.5f, canvasHeight * 0.42f,
                        canvasWidth * 0.72f, canvasHeight * 0.48f
                    )
                }

                // Outer route glow
                drawPath(
                    path = routePath,
                    color = WayMateGreenLight.copy(alpha = 0.5f),
                    style = Stroke(width = 16f, cap = StrokeCap.Round)
                )

                // Main route line
                drawPath(
                    path = routePath,
                    color = WayMateGreen,
                    style = Stroke(width = 8f, cap = StrokeCap.Round)
                )

                // Pickup Marker (Sky Blue circle)
                val pickupX = canvasWidth * 0.25f
                val pickupY = canvasHeight * 0.28f
                drawCircle(
                    color = WayMateSky.copy(alpha = 0.3f),
                    radius = 24f,
                    center = Offset(pickupX, pickupY)
                )
                drawCircle(
                    color = WayMateSky,
                    radius = 12f,
                    center = Offset(pickupX, pickupY)
                )
                drawCircle(
                    color = Color.White,
                    radius = 5f,
                    center = Offset(pickupX, pickupY)
                )

                // Destination Marker (WayMate Green pin)
                val destX = canvasWidth * 0.72f
                val destY = canvasHeight * 0.48f
                drawCircle(
                    color = WayMateGreen.copy(alpha = 0.3f),
                    radius = 26f,
                    center = Offset(destX, destY)
                )
                drawCircle(
                    color = WayMateGreen,
                    radius = 14f,
                    center = Offset(destX, destY)
                )
                drawCircle(
                    color = Color.White,
                    radius = 6f,
                    center = Offset(destX, destY)
                )

                // Live animated driver car location
                val currentCarX = canvasWidth * (0.25f + (0.72f - 0.25f) * carProgress)
                val currentCarY = canvasHeight * (0.28f + (0.48f - 0.28f) * carProgress)

                drawCircle(
                    color = Color.Black.copy(alpha = 0.2f),
                    radius = 18f,
                    center = Offset(currentCarX, currentCarY + 4f)
                )
                drawCircle(
                    color = Color(0xFF1E2818),
                    radius = 16f,
                    center = Offset(currentCarX, currentCarY)
                )
                drawCircle(
                    color = WayMateGreen,
                    radius = 6f,
                    center = Offset(currentCarX, currentCarY)
                )
            }

            // Top Bar Overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(WayMateWhite)
                        .border(1.dp, WayMateBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = WayMateTextPrimary
                    )
                }

                // Live ETA Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(WayMateWhite)
                        .border(1.dp, WayMateBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(WayMateGreen)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Arriving in ${uiState.trackingEtaMinutes} mins",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateTextPrimary
                            )
                        )
                    }
                }

                // Emergency SOS Button
                IconButton(
                    onClick = {
                        val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                        context.startActivity(dial)
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFEEEE))
                        .border(1.dp, WayMateError.copy(alpha = 0.4f), CircleShape)
                ) {
                    Text(
                        text = "SOS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = WayMateError,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // Bottom Floating Telemetry & Driver Card
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                WayMateCard(
                    backgroundColor = WayMateWhite,
                    cornerRadius = 22.dp,
                    elevation = 6.dp
                ) {
                    // Header: Driver and Vehicle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, WayMateGreen, CircleShape)
                            ) {
                                AsyncImage(
                                    model = driver?.photoUrl ?: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
                                    contentDescription = driver?.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = driver?.name ?: "Rajesh Verma",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = WayMateTextPrimary
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = WayMateSky,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "${driver?.vehicleModel ?: "Toyota Hyryder"} • ${driver?.plateNumber ?: "DL 01 AB 4022"}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = WayMateTextSecondary,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        // OTP Callout
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(WayMateGreenContainer)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "PIN",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, color = WayMateGreenDark)
                                )
                                Text(
                                    text = uiState.rideOtp,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = WayMateTextPrimary,
                                        letterSpacing = 1.sp
                                    )
                                )
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = WayMateDivider)

                    // Route Progress Timeline
                    RouteTimeline(
                        pickup = uiState.pickupLocation,
                        destination = uiState.destinationLocation
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Contact actions + Complete trip simulation button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        IconButton(
                            onClick = {
                                val call = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${driver?.phone ?: "+919811233445"}"))
                                context.startActivity(call)
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(WayMateGreenContainer)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", tint = WayMateGreenDark)
                        }

                        IconButton(
                            onClick = onNavigateToChat,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(WayMateSkyContainer)
                        ) {
                            Icon(Icons.Default.ChatBubble, contentDescription = "Chat", tint = WayMateSkyDark)
                        }

                        Button(
                            onClick = {
                                viewModel.completeTrip()
                                onTripCompleted()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WayMateGreen)
                        ) {
                            Text(
                                text = "End Ride & Rate",
                                style = MaterialTheme.typography.titleSmall.copy(
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
