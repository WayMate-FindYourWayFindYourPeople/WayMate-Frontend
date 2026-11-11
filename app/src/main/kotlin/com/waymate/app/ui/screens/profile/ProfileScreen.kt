package com.waymate.app.ui.screens.profile

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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.waymate.app.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToMyTrips: () -> Unit,
    onNavigateToMyPools: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogoutSuccess: () -> Unit,
    onBottomTabSelected: (BottomNavTab) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = uiState.user

    var showLogoutConfirmDialog by remember { mutableStateOf(false) }
    var showPlacesDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showSafetyDialog by remember { mutableStateOf(false) }

    if (showLogoutConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirmDialog = false },
            title = {
                Text(
                    text = "Log Out of WayMate?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to log out? You will need to enter your registered phone number to sign back in.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = WayMateTextSecondary)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutConfirmDialog = false
                        viewModel.logout {
                            onLogoutSuccess()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WayMateError)
                ) {
                    Text("Log Out", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirmDialog = false }) {
                    Text("Cancel", color = WayMateTextPrimary)
                }
            },
            containerColor = WayMateCream,
            shape = RoundedCornerShape(18.dp)
        )
    }

    if (showPlacesDialog) {
        AlertDialog(
            onDismissRequest = { showPlacesDialog = false },
            title = { Text("Saved Places", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("🏠 Home: ${uiState.savedHomeAddress}")
                    Text("🏢 Work: ${uiState.savedWorkAddress}")
                    Text("✈️ Favorite Airport: ${uiState.savedAirport}")
                }
            },
            confirmButton = {
                Button(onClick = { showPlacesDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = WayMateGreen)) {
                    Text("Done", color = Color.White)
                }
            },
            containerColor = WayMateCream,
            shape = RoundedCornerShape(18.dp)
        )
    }

    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false },
            title = { Text("Payment Methods", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("🟢 Default: ${uiState.defaultPaymentMethod}")
                    Text("💳 HDFC Bank Credit Card (•• 8820)")
                    Text("📱 UPI ID: alex.morgan@okaxis")
                }
            },
            confirmButton = {
                Button(onClick = { showPaymentDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = WayMateGreen)) {
                    Text("Done", color = Color.White)
                }
            },
            containerColor = WayMateCream,
            shape = RoundedCornerShape(18.dp)
        )
    }

    if (showSafetyDialog) {
        AlertDialog(
            onDismissRequest = { showSafetyDialog = false },
            title = { Text("Safety & Support", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🛡️ Verified Passenger ID: ACTIVE")
                    Text("📞 Emergency Contact: ${uiState.emergencyContactName}")
                    Text("🚨 24x7 Incident Hotline: 1800-WAYMATE")
                    Text("⚡ Real-time Telemetry: GPS tracking with live share")
                }
            },
            confirmButton = {
                Button(onClick = { showSafetyDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = WayMateGreen)) {
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
                title = "Profile",
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = WayMateTextPrimary
                        )
                    }
                }
            )
        },
        bottomBar = {
            WayMateBottomBar(
                currentTab = BottomNavTab.PROFILE,
                onTabSelected = onBottomTabSelected
            )
        },
        containerColor = WayMateCream
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 10.dp, bottom = 28.dp)
        ) {
            // Profile Card Header
            WayMateCard(backgroundColor = WayMateWhite) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .border(2.5.dp, WayMateGreen, CircleShape)
                    ) {
                        AsyncImage(
                            model = user.avatarUrl,
                            contentDescription = user.fullName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = user.fullName,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WayMateTextPrimary,
                                    fontSize = 19.sp
                                )
                            )
                            if (user.isVerified) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = WayMateSky,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Text(
                            text = user.phone,
                            style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextSecondary)
                        )

                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.labelSmall.copy(color = WayMateTextMuted, fontSize = 11.sp)
                        )
                    }

                    IconButton(onClick = onNavigateToEditProfile) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit Profile",
                            tint = WayMateGreenDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Row: Total Trips, CO2 Saved, Pools Shared
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                WayMateStatCard(
                    title = "Total Trips",
                    value = "${user.totalTrips}",
                    subtitle = "All Rides",
                    modifier = Modifier.weight(1f),
                    accentColor = WayMateGreenDark
                )

                WayMateStatCard(
                    title = "CO2 Saved",
                    value = "${user.co2SavedKg} kg",
                    subtitle = "Eco Impact",
                    modifier = Modifier.weight(1f),
                    accentColor = WayMateSky
                )

                WayMateStatCard(
                    title = "Pools Shared",
                    value = "${user.poolsShared}",
                    subtitle = "Carpooled",
                    modifier = Modifier.weight(1f),
                    accentColor = WayMateDaisyDark
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Profile Menu Options
            Text(
                text = "Account & Preferences",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            WayMateCard(backgroundColor = WayMateWhite) {
                ProfileMenuItem(
                    icon = Icons.Outlined.DirectionsCar,
                    iconTint = WayMateGreenDark,
                    title = "My Trips",
                    subtitle = "Upcoming, completed and receipts",
                    onClick = onNavigateToMyTrips
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = WayMateDivider)

                ProfileMenuItem(
                    icon = Icons.Outlined.Groups,
                    iconTint = WayMateDaisy,
                    title = "My Pools",
                    subtitle = "Hosting and joined airport carpools",
                    onClick = onNavigateToMyPools
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = WayMateDivider)

                ProfileMenuItem(
                    icon = Icons.Outlined.AccountBalanceWallet,
                    iconTint = WayMateSky,
                    title = "Payment Methods",
                    subtitle = "WayMate Wallet, UPI, Saved Cards",
                    onClick = { showPaymentDialog = true }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = WayMateDivider)

                ProfileMenuItem(
                    icon = Icons.Outlined.LocationOn,
                    iconTint = WayMateGreen,
                    title = "Saved Places",
                    subtitle = "Home, work & terminal addresses",
                    onClick = { showPlacesDialog = true }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = WayMateDivider)

                ProfileMenuItem(
                    icon = Icons.Outlined.Security,
                    iconTint = WayMateGreenDark,
                    title = "Safety & Support",
                    subtitle = "Emergency contacts, SOS, 24x7 helpline",
                    onClick = { showSafetyDialog = true }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = WayMateDivider)

                ProfileMenuItem(
                    icon = Icons.Outlined.Settings,
                    iconTint = WayMateTextSecondary,
                    title = "Settings",
                    subtitle = "Notifications, language, privacy",
                    onClick = onNavigateToSettings
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Logout Button
            OutlinedButton(
                onClick = { showLogoutConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, WayMateError.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = WayMateError)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Log Out",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
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
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = WayMateTextSecondary,
                    fontSize = 12.sp
                )
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = WayMateBorder,
            modifier = Modifier.size(18.dp)
        )
    }
}
