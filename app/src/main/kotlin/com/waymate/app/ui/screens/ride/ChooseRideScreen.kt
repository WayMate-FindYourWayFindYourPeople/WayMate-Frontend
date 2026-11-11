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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.data.model.RideCategory
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.RideViewModel

@Composable
fun ChooseRideScreen(
    viewModel: RideViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToBookRide: () -> Unit,
    onNavigateToPoolRides: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedOption by remember { mutableStateOf(uiState.selectedRideType) }

    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "Choose Ride Option",
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
                    text = if (selectedOption == RideCategory.SOLO) "Continue with Solo Ride" else "Find Shared Ride Pools",
                    onClick = {
                        viewModel.selectRideType(selectedOption)
                        if (selectedOption == RideCategory.SOLO) {
                            onNavigateToBookRide()
                        } else {
                            onNavigateToPoolRides()
                        }
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
                text = "How would you like to travel?",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary,
                    fontSize = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Select private door-to-door comfort or share expenses with verified co-passengers.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = WayMateTextSecondary,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Option 1: Solo Ride
            RideOptionCard(
                title = "Solo Ride",
                tagline = "Private, direct & fast travel",
                priceEstimate = "From ₹420",
                icon = Icons.Default.DirectionsCar,
                accentColor = WayMateGreen,
                isSelected = selectedOption == RideCategory.SOLO,
                onClick = { selectedOption = RideCategory.SOLO },
                features = listOf(
                    "Exclusive car for you and your luggage",
                    "Direct door-to-door route with zero intermediate stops",
                    "Choose from compact sedans, EVs or spacious 6-seater SUVs",
                    "Instant dispatch and real-time tracking"
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Option 2: Pool Ride
            RideOptionCard(
                title = "Pool Ride",
                tagline = "Shared, smart & eco-friendly transfer",
                priceEstimate = "From ₹250 (Save up to 45%)",
                badgeText = "Most Popular",
                icon = Icons.Default.Groups,
                accentColor = WayMateDaisy,
                isSelected = selectedOption == RideCategory.POOL,
                onClick = { selectedOption = RideCategory.POOL },
                features = listOf(
                    "Split tolls and fares with verified airport travellers",
                    "Options for exclusive Women-Only pools or Mixed groups",
                    "Dedicated luggage space guarantee for every passenger",
                    "Reduces city emissions and airport traffic congestion"
                )
            )
        }
    }
}

@Composable
private fun RideOptionCard(
    title: String,
    tagline: String,
    priceEstimate: String,
    icon: ImageVector,
    accentColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    features: List<String>,
    badgeText: String? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) accentColor else WayMateBorder,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) WayMateWhite else WayMateWhite.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateTextPrimary,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = tagline,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = WayMateTextSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                // Radio-like checkmark indicator
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) accentColor else Color.Transparent)
                        .border(
                            width = 2.dp,
                            color = if (isSelected) accentColor else WayMateBorder,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Price banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = priceEstimate,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    )
                    if (badgeText != null) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateDaisyDark,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bullet points
            features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = WayMateGreen,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = WayMateTextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }
    }
}
