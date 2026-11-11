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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.data.model.PoolType
import com.waymate.app.data.model.RideCategory
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.PoolViewModel
import com.waymate.app.ui.viewmodel.RideViewModel

@Composable
fun ChooseRideTypeScreen(
    rideViewModel: RideViewModel,
    poolViewModel: PoolViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAvailablePools: () -> Unit,
    onNavigateToAvailableRides: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(RideCategory.POOL) }
    var selectedPoolPreference by remember { mutableStateOf(PoolType.ALL) }

    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "Choose Ride Type",
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
                    text = if (selectedCategory == RideCategory.POOL) "View Available Pools" else "View Available Solo Cabs",
                    onClick = {
                        rideViewModel.selectRideType(selectedCategory)
                        if (selectedCategory == RideCategory.POOL) {
                            poolViewModel.setFilter(selectedPoolPreference)
                            onNavigateToAvailablePools()
                        } else {
                            onNavigateToAvailableRides()
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
                text = "Select Your Preferred Ride Mode",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary,
                    fontSize = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Choose between shared cost-saving pools or dedicated solo rides.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = WayMateTextSecondary,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Option: Pool Ride
            RideTypeSelectCard(
                title = "Pool Ride",
                subtitle = "Share with verified passengers & split tolls",
                fareNotice = "Avg ₹280 - ₹390 / seat",
                icon = Icons.Default.Groups,
                accentColor = WayMateGreen,
                isSelected = selectedCategory == RideCategory.POOL,
                onClick = { selectedCategory = RideCategory.POOL }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Option: Solo Ride
            RideTypeSelectCard(
                title = "Solo Ride",
                subtitle = "Private car exclusively for you & your group",
                fareNotice = "From ₹420",
                icon = Icons.Default.DirectionsCar,
                accentColor = WayMateSky,
                isSelected = selectedCategory == RideCategory.SOLO,
                onClick = { selectedCategory = RideCategory.SOLO }
            )

            // If Pool Ride selected, show Women Only vs Mixed filter selection
            if (selectedCategory == RideCategory.POOL) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Pool Group Preference",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateTextPrimary,
                        fontSize = 17.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Women Only Selection
                PoolPreferenceSelectCard(
                    title = "Women Only Pool",
                    description = "Exclusively female drivers and co-travellers for enhanced comfort and peace of mind.",
                    badge = "Verified Safe",
                    icon = Icons.Default.Female,
                    accentColor = WayMateDaisy,
                    isSelected = selectedPoolPreference == PoolType.WOMEN_ONLY,
                    onClick = { selectedPoolPreference = PoolType.WOMEN_ONLY }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Mixed Pool Selection
                PoolPreferenceSelectCard(
                    title = "Mixed Pool (All Passengers)",
                    description = "Open to all verified airport commuters. Maximum pool options with quickest departures.",
                    badge = "Fastest Pickup",
                    icon = Icons.Default.Groups,
                    accentColor = WayMateSky,
                    isSelected = selectedPoolPreference == PoolType.MIXED,
                    onClick = { selectedPoolPreference = PoolType.MIXED }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // All Pools Selection
                PoolPreferenceSelectCard(
                    title = "Show All Available Pools",
                    description = "Browse both Women-Only and Mixed carpools on this route.",
                    badge = "All Options",
                    icon = Icons.Default.Checklist,
                    accentColor = WayMateGreen,
                    isSelected = selectedPoolPreference == PoolType.ALL,
                    onClick = { selectedPoolPreference = PoolType.ALL }
                )
            }
        }
    }
}

@Composable
private fun RideTypeSelectCard(
    title: String,
    subtitle: String,
    fareNotice: String,
    icon: ImageVector,
    accentColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) accentColor else WayMateBorder,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) WayMateWhite else WayMateWhite.copy(alpha = 0.9f)
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
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = WayMateTextPrimary,
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = WayMateTextSecondary,
                            fontSize = 12.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = fareNotice,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) accentColor else Color.Transparent)
                    .border(2.dp, if (isSelected) accentColor else WayMateBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PoolPreferenceSelectCard(
    title: String,
    description: String,
    badge: String,
    icon: ImageVector,
    accentColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) accentColor else WayMateBorder,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WayMateWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = WayMateTextPrimary,
                            fontSize = 15.sp
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = accentColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = WayMateTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            )
        }
    }
}
