package com.waymate.app.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.ProfileViewModel

@Composable
fun SettingsScreen(
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            WayMateTopBar(
                title = "Settings",
                onBackClick = onNavigateBack
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
                .padding(top = 16.dp, bottom = 24.dp)
        ) {
            Text(
                text = "Notifications & Alerts",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            WayMateCard(backgroundColor = WayMateWhite) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Push Notifications",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = "Receive live driver arrival and pool updates",
                            style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextSecondary)
                        )
                    }
                    Switch(
                        checked = uiState.pushNotificationsEnabled,
                        onCheckedChange = viewModel::togglePushNotifications,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                            checkedTrackColor = WayMateGreen
                        )
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = WayMateDivider)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SMS Status Updates",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = "Receive OTP and driver details via text message",
                            style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextSecondary)
                        )
                    }
                    Switch(
                        checked = uiState.rideUpdatesSms,
                        onCheckedChange = viewModel::toggleSmsUpdates,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                            checkedTrackColor = WayMateGreen
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Legal & About",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = WayMateTextPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            WayMateCard(backgroundColor = WayMateWhite) {
                Text(
                    text = "WayMate Smart Transfer v1.0.0",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Native Android App powered by Kotlin & Jetpack Compose",
                    style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextSecondary)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = WayMateDivider)

                Text(
                    text = "Terms of Service • Privacy Policy • Ride Sharing Community Guidelines",
                    style = MaterialTheme.typography.bodySmall.copy(color = WayMateTextMuted, fontSize = 12.sp)
                )
            }
        }
    }
}
