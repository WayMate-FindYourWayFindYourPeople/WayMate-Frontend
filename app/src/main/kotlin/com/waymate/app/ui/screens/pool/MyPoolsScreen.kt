package com.waymate.app.ui.screens.pool

import android.content.Intent
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
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.waymate.app.data.model.PoolItem
import com.waymate.app.data.model.PoolType
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.PoolViewModel

@Composable
fun MyPoolsScreen(
    viewModel: PoolViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCreatePool: () -> Unit,
    onBottomTabSelected: (BottomNavTab) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isHosting = uiState.isHostingTabSelected
    val context = LocalContext.current
    var managePoolDialogItem by remember { mutableStateOf<PoolItem?>(null) }

    if (managePoolDialogItem != null) {
        val pool = managePoolDialogItem!!
        AlertDialog(
            onDismissRequest = { managePoolDialogItem = null },
            title = {
                Text(
                    text = "Manage Pool (${pool.pickup} -> ${pool.destination})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Departure: ${pool.date} at ${pool.time}")
                    Text("Total Confirmed Passengers: ${pool.passengers.size}")
                    Text("Total Fare Collected: ₹${(pool.farePerPerson * pool.passengers.size).toInt()}")
                }
            },
            confirmButton = {
                Button(
                    onClick = { managePoolDialogItem = null },
                    colors = ButtonDefaults.buttonColors(containerColor = WayMateGreen)
                ) {
                    Text("Close", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { managePoolDialogItem = null },
                    colors = ButtonDefaults.textButtonColors(contentColor = WayMateError)
                ) {
                    Text("Cancel Pool")
                }
            },
            containerColor = WayMateCream,
            shape = RoundedCornerShape(18.dp)
        )
    }

    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "My Pools",
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
        bottomBar = {
            WayMateBottomBar(
                currentTab = BottomNavTab.POOLS,
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
            // Tabs: Joined vs Hosting
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(WayMateWhite)
                    .border(1.dp, WayMateBorder, RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                // Joined Tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (!isHosting) WayMateGreen else Color.Transparent)
                        .clickable { viewModel.setMyPoolsTab(false) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Joined Pools (${uiState.myJoinedPools.size})",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (!isHosting) FontWeight.Bold else FontWeight.Medium,
                            color = if (!isHosting) Color.White else WayMateTextSecondary
                        )
                    )
                }

                // Hosting Tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isHosting) WayMateGreen else Color.Transparent)
                        .clickable { viewModel.setMyPoolsTab(true) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Hosting (${uiState.myHostedPools.size})",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (isHosting) FontWeight.Bold else FontWeight.Medium,
                            color = if (isHosting) Color.White else WayMateTextSecondary
                        )
                    )
                }
            }

            val listToShow = if (isHosting) uiState.myHostedPools else uiState.myJoinedPools

            if (listToShow.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = WayMateTextMuted,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (isHosting) "You haven't hosted any pools yet" else "No joined pools right now",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isHosting) "Create your first pool to offer empty seats on your next trip." else "Browse available airport pools and join verified commuters.",
                            style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextSecondary),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Button(
                            onClick = onNavigateToCreatePool,
                            colors = ButtonDefaults.buttonColors(containerColor = WayMateGreen)
                        ) {
                            Text("Create a Pool", color = Color.White)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(listToShow) { pool ->
                        MyPoolCard(
                            pool = pool,
                            isHost = isHosting,
                            onShare = {
                                val shareIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "Join my WayMate airport shared pool from ${pool.pickup} to ${pool.destination} on ${pool.date} at ${pool.time}! Fare is only ₹${pool.farePerPerson.toInt()}.")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Pool via"))
                            },
                            onManage = { managePoolDialogItem = pool }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MyPoolCard(
    pool: PoolItem,
    isHost: Boolean,
    onShare: () -> Unit,
    onManage: () -> Unit
) {
    WayMateCard(backgroundColor = WayMateWhite) {
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
                    .background(WayMateGreenContainer)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = if (isHost) "HOSTING" else "JOINED",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = WayMateGreenDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        RouteTimeline(
            pickup = pool.pickup,
            destination = pool.destination,
            pickupTime = pool.time,
            destinationTime = pool.estimatedArrival
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Passengers Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                pool.passengers.forEachIndexed { idx, p ->
                    Box(
                        modifier = Modifier
                            .offset(x = (-6 * idx).dp)
                            .size(28.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, WayMateWhite, CircleShape)
                    ) {
                        AsyncImage(
                            model = p.avatarUrl,
                            contentDescription = p.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${pool.passengers.size} Passenger${if (pool.passengers.size > 1) "s" else ""}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = WayMateTextSecondary,
                        fontSize = 12.sp
                    )
                )
            }

            Text(
                text = "₹${pool.farePerPerson.toInt()} / seat",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateGreenDark
                )
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = WayMateDivider)

        // Actions: Share & Manage
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onShare,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, WayMateBorder),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Icon(Icons.Outlined.Share, contentDescription = null, tint = WayMateTextPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Share Pool", color = WayMateTextPrimary, style = MaterialTheme.typography.labelMedium)
            }

            Button(
                onClick = onManage,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = WayMateGreen),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Icon(Icons.Default.Tune, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Manage Pool", color = Color.White, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
