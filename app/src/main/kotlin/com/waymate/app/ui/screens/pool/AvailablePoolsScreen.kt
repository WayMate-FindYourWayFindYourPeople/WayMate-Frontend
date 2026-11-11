package com.waymate.app.ui.screens.pool

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.waymate.app.data.model.PoolItem
import com.waymate.app.data.model.PoolType
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.PoolViewModel

@Composable
fun AvailablePoolsScreen(
    viewModel: PoolViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToPoolDetails: (PoolItem) -> Unit,
    onNavigateToCreatePool: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "Available Pools",
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(onClick = onNavigateToCreatePool) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Pool",
                            tint = WayMateGreenDark
                        )
                    }
                }
            )
        },
        containerColor = WayMateCream
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Route Header Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WayMateWhite)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FlightLand,
                                contentDescription = null,
                                tint = WayMateSky,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Airport Arrivals Route",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WayMateTextPrimary
                                )
                            )
                        }
                        Text(
                            text = "Today • From Terminal 3 & 1",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = WayMateTextMuted,
                                fontSize = 12.sp
                            )
                        )
                    }

                    // Button to create a pool
                    TextButton(
                        onClick = onNavigateToCreatePool,
                        colors = ButtonDefaults.textButtonColors(contentColor = WayMateGreenDark)
                    ) {
                        Icon(Icons.Default.AddCircleOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Create Pool",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // Filter Chips Bar (All, Women Only, Mixed)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // All
                WayMateFilterChip(
                    text = "All Pools",
                    isSelected = uiState.selectedFilter == PoolType.ALL,
                    onClick = { viewModel.setFilter(PoolType.ALL) }
                )

                // Women Only (Daisy)
                WayMateFilterChip(
                    text = "Women Only",
                    isSelected = uiState.selectedFilter == PoolType.WOMEN_ONLY,
                    onClick = { viewModel.setFilter(PoolType.WOMEN_ONLY) },
                    activeColor = WayMateDaisy,
                    activeTextColor = Color.White
                )

                // Mixed (Sky)
                WayMateFilterChip(
                    text = "Mixed",
                    isSelected = uiState.selectedFilter == PoolType.MIXED,
                    onClick = { viewModel.setFilter(PoolType.MIXED) },
                    activeColor = WayMateSky,
                    activeTextColor = Color.White
                )
            }

            // Pools List
            if (uiState.filteredPools.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = WayMateTextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No pools matching this filter",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try switching filters or create a new pool.",
                            style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextSecondary)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(uiState.filteredPools) { pool ->
                        PoolCardItem(
                            pool = pool,
                            onViewDetails = {
                                viewModel.selectPool(pool)
                                onNavigateToPoolDetails(pool)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PoolCardItem(
    pool: PoolItem,
    onViewDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onViewDetails() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = WayMateWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, WayMateBorder.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row: Type Badge + Available Seats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (pool.poolType == PoolType.WOMEN_ONLY) {
                    WomenOnlyBadge()
                } else {
                    MixedPoolBadge()
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (pool.availableSeats > 0) WayMateGreenContainer else WayMateBorder.copy(alpha = 0.3f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (pool.availableSeats > 0) "${pool.availableSeats} of ${pool.totalSeats} seats open" else "Full",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (pool.availableSeats > 0) WayMateGreenDark else WayMateTextMuted,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Route Timeline
            RouteTimeline(
                pickup = pool.pickup,
                destination = pool.destination,
                pickupTime = pool.time,
                destinationTime = pool.estimatedArrival
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Co-passenger avatars + Host info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatars row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    pool.passengers.take(3).forEachIndexed { idx, passenger ->
                        Box(
                            modifier = Modifier
                                .offset(x = (-8 * idx).dp)
                                .size(32.dp)
                                .clip(CircleShape)
                                .border(2.dp, WayMateWhite, CircleShape)
                        ) {
                            AsyncImage(
                                model = passenger.avatarUrl,
                                contentDescription = passenger.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(if (pool.passengers.size > 1) 4.dp else 8.dp))

                    Text(
                        text = "Host: ${pool.host.name.split(" ").first()}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = WayMateTextSecondary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                }

                // Fare Per Person
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "₹${pool.farePerPerson.toInt()}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateGreenDark,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = " / person",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = WayMateTextMuted,
                                fontSize = 11.sp
                            )
                        )
                    }

                    Text(
                        text = "Solo: ₹${pool.regularSoloFare.toInt()}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = WayMateTextMuted,
                            textDecoration = TextDecoration.LineThrough,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = WayMateDivider)

            // View Details Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View Pool Details",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateGreenDark
                    )
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = WayMateGreenDark,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
