package com.waymate.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    onVerificationSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = WayMateTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WayMateCream)
            )
        },
        containerColor = WayMateCream
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                WayMateLogo(
                    height = 54.dp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Text(
                    text = "Verify OTP",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateTextPrimary,
                        fontSize = 28.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enter the 4-digit verification code sent to",
                    style = MaterialTheme.typography.bodyMedium.copy(color = WayMateTextSecondary)
                )

                Text(
                    text = "+91 ${uiState.mobileNumber.ifEmpty { "98765 43210" }}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Custom OTP Boxes Display
                val currentOtp = uiState.otp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 0 until 4) {
                        val char = if (i < currentOtp.length) currentOtp[i].toString() else ""
                        val isCurrent = i == currentOtp.length

                        Box(
                            modifier = Modifier
                                .size(62.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(WayMateWhite)
                                .border(
                                    width = if (isCurrent) 2.dp else 1.dp,
                                    color = if (isCurrent) WayMateGreen else WayMateBorder,
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WayMateTextPrimary,
                                    fontSize = 26.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Hidden/compact actual input field
                WayMateTextField(
                    value = uiState.otp,
                    onValueChange = viewModel::onOtpChange,
                    placeholder = "Enter 4-digit OTP (e.g. 1234)",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    isError = uiState.errorMessage != null,
                    errorMessage = uiState.errorMessage
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Demo hint
                WayMateCard(
                    backgroundColor = WayMateGreenContainer.copy(alpha = 0.5f),
                    borderColor = WayMateGreen.copy(alpha = 0.4f),
                    cornerRadius = 12.dp,
                    elevation = 0.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🔑 Demo OTP Code: 1234",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateGreenDark
                            )
                        )
                        TextButton(
                            onClick = { viewModel.onOtpChange("1234") },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "Auto-Fill",
                                color = WayMateGreenDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Resend OTP countdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uiState.canResendOtp) {
                        TextButton(onClick = viewModel::resendOtp) {
                            Text(
                                text = "Resend OTP",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WayMateSkyDark
                                )
                            )
                        }
                    } else {
                        Text(
                            text = "Resend OTP in 00:${uiState.otpCountdown.toString().padStart(2, '0')}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = WayMateTextMuted,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                WayMateButton(
                    text = "Verify & Continue",
                    onClick = {
                        viewModel.verifyOtp {
                            onVerificationSuccess()
                        }
                    },
                    isLoading = uiState.isLoading
                )
            }
        }
    }
}
