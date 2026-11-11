package com.waymate.app.ui.screens.airport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*

@Composable
fun AirportRideScreen(
    onNavigateBack: () -> Unit,
    onNavigateToFlightDetails: () -> Unit
) {
    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "Smart Airport Transfer",
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
                    text = "Book Airport Ride",
                    onClick = onNavigateToFlightDetails,
                    leadingIcon = {
                        Icon(Icons.Default.FlightTakeoff, contentDescription = null, tint = Color.White)
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
                .padding(top = 12.dp, bottom = 24.dp)
        ) {
            // Hero Travel Banner with Photography & Visuals
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1436491865332-7a61a109cc05?auto=format&fit=crop&w=1200&q=80",
                        contentDescription = "Airport Runway & Flight Transfer",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Dark gradient scrim for legibility
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0x99000000),
                                        Color(0xDD000000)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(18.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(WayMateGreen)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "FLIGHT SYNCHRONIZED",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 10.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Stress-Free Terminal Connections",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        )
                        Text(
                            text = "Never worry about delays, surges or finding your cab at arrivals",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Why Book with WayMate Airport?",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary,
                    fontSize = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Benefit Cards
            WayMateCard(
                backgroundColor = WayMateWhite,
                cornerRadius = 18.dp
            ) {
                AirportBenefitItem(
                    icon = Icons.Default.Update,
                    iconColor = WayMateSky,
                    title = "Live Flight Delay Tracking",
                    description = "We track your flight number in real time. If your flight is delayed or lands early, your pickup adjusts automatically."
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = WayMateDivider)

                AirportBenefitItem(
                    icon = Icons.Default.HourglassTop,
                    iconColor = WayMateGreen,
                    title = "45-Minute Free Waiting Time",
                    description = "Plenty of time to clear immigration, collect baggage from conveyor belts, and walk comfortably to the arrival pickup zone."
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = WayMateDivider)

                AirportBenefitItem(
                    icon = Icons.Default.MoneyOff,
                    iconColor = WayMateDaisy,
                    title = "Fixed Terminal Pricing",
                    description = "Zero dynamic surge pricing. Tolls, parking fees, and airport entry taxes are all bundled transparently in one price."
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = WayMateDivider)

                AirportBenefitItem(
                    icon = Icons.Default.Luggage,
                    iconColor = WayMateGreenDark,
                    title = "Guaranteed Luggage Space",
                    description = "Every vehicle is checked for ample trunk room to ensure passenger bags are safely accommodated without squeezing."
                )
            }
        }
    }
}

@Composable
private fun AirportBenefitItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary,
                    fontSize = 15.sp
                )
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = WayMateTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            )
        }
    }
}
