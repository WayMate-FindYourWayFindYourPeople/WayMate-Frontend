package com.waymate.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WayMateCream)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Official WayMate Logo
                WayMateLogo(
                    height = 64.dp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Way",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = WayMateTextPrimary,
                            fontSize = 30.sp
                        )
                    )
                    Text(
                        text = "Mate",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = WayMateGreen,
                            fontSize = 30.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Airport Ride-Sharing & Smart Transfers",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = WayMateTextSecondary,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Feature Highlights
                WayMateCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = WayMateWhite
                ) {
                    FeatureRow(
                        icon = Icons.Default.DirectionsCar,
                        iconTint = WayMateGreen,
                        title = "Book Private Rides",
                        subtitle = "Instant door-to-door city and terminal transfers with zero surge"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FeatureRow(
                        icon = Icons.Default.Groups,
                        iconTint = WayMateDaisy,
                        title = "Share a Ride Pool",
                        subtitle = "Cut airport travel costs by up to 45% with verified co-travellers"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FeatureRow(
                        icon = Icons.Default.AirplanemodeActive,
                        iconTint = WayMateSky,
                        title = "Smart Flight Tracking",
                        subtitle = "Complimentary 45-min wait time for delayed domestic & int'l flights"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FeatureRow(
                        icon = Icons.Default.Security,
                        iconTint = WayMateGreenDark,
                        title = "Verified Safety & Women-Only",
                        subtitle = "Option for exclusive women-only pools and 24x7 trip telemetry"
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WayMateButton(
                    text = "Get Started",
                    onClick = onNavigateToLogin,
                    containerColor = WayMateGreen,
                    contentColor = Color.White
                )

                WayMateSecondaryButton(
                    text = "Log In with Mobile",
                    onClick = onNavigateToLogin
                )

                WayMateOutlinedButton(
                    text = "Create New Account",
                    onClick = onNavigateToSignUp,
                    borderColor = WayMateBorder,
                    contentColor = WayMateTextPrimary
                )
            }
        }
    }
}

@Composable
private fun FeatureRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
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
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = WayMateTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            )
        }
    }
}
