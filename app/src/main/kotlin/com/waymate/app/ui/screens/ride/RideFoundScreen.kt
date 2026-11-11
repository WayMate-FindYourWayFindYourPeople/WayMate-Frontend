package com.waymate.app.ui.screens.ride

import android.content.Intent
import android.net.Uri
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
fun RideFoundScreen(
    viewModel: RideViewModel,
    onNavigateToLiveTracking: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val driver = uiState.assignedDriver
    val context = LocalContext.current

    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "Ride Confirmed!",
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
                    text = "View Live Track",
                    onClick = onNavigateToLiveTracking,
                    leadingIcon = {
                        Icon(Icons.Default.Navigation, contentDescription = null, tint = Color.White)
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
            // Arrival ETA Hero Card
            WayMateCard(
                backgroundColor = WayMateGreenContainer,
                borderColor = WayMateGreen.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Driver Arriving in",
                            style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextSecondary)
                        )
                        Text(
                            text = "${uiState.trackingEtaMinutes} Minutes",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateGreenDark,
                                fontSize = 26.sp
                            )
                        )
                    }

                    // Start OTP Callout
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "START OTP",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateGreenDark,
                                fontSize = 10.sp
                            )
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(WayMateWhite)
                                .border(1.dp, WayMateGreen, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = uiState.rideOtp,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WayMateTextPrimary,
                                    fontSize = 18.sp,
                                    letterSpacing = 2.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Driver & Vehicle Card
            if (driver != null) {
                WayMateCard(backgroundColor = WayMateWhite) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .border(2.dp, WayMateGreen, CircleShape)
                        ) {
                            AsyncImage(
                                model = driver.photoUrl,
                                contentDescription = driver.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = driver.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = WayMateTextPrimary,
                                        fontSize = 18.sp
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified Driver",
                                    tint = WayMateSky,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(3.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = WayMateWarning,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${driver.rating}",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "(${driver.totalTrips} trips)",
                                    style = MaterialTheme.typography.labelSmall.copy(color = WayMateTextMuted)
                                )
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = WayMateDivider)

                    // Vehicle Details Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = driver.vehicleModel,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WayMateTextPrimary
                                )
                            )
                            Text(
                                text = "Color: ${driver.vehicleColor}",
                                style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextSecondary)
                            )
                        }

                        // Vehicle Registration Number Plate
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFF7D6))
                                .border(1.dp, Color(0xFFC7A720), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = driver.plateNumber,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B2217),
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Contact Actions: Call Driver, Chat, Share Trip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Call Driver
                WayMateCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${driver?.phone ?: "+919811233445"}"))
                            context.startActivity(intent)
                        },
                    backgroundColor = WayMateWhite
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(WayMateGreenContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", tint = WayMateGreenDark)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Call Driver",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateTextPrimary
                            )
                        )
                    }
                }

                // Chat
                WayMateCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToChat() },
                    backgroundColor = WayMateWhite
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(WayMateSkyContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ChatBubble, contentDescription = "Chat", tint = WayMateSkyDark)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Message",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateTextPrimary
                            )
                        )
                    }
                }

                // Share Trip
                WayMateCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "I'm travelling with driver ${driver?.name} in ${driver?.vehicleModel} (${driver?.plateNumber}). Track my WayMate ride live.")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Ride Details"))
                        },
                    backgroundColor = WayMateWhite
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(WayMateDaisyContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = WayMateDaisyDark)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Share Trip",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateTextPrimary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Route summary
            WayMateCard(backgroundColor = WayMateWhite) {
                RouteTimeline(
                    pickup = uiState.pickupLocation,
                    destination = uiState.destinationLocation
                )
            }
        }
    }
}
