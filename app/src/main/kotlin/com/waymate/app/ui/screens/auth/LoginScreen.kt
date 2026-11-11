package com.waymate.app.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.R
import com.waymate.app.ui.components.WayMateLogo
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.AuthViewModel

// Screen-local design tokens matching the reference mockup
private val LoginCreamBackground = Color(0xFFF9F4E9)
private val LoginGreen = Color(0xFF72933C)
private val LoginTextPrimary = Color(0xFF1B2217)
private val LoginTextSecondary = Color(0xFF535D4C)
private val LoginBorderColor = Color(0xFFD5DFCA)
private val LoginInputBackground = Color(0xFFFBFBF9)
private val LoginGreenDark = Color(0xFF5E8635)

/**
 * WayMate Login Screen redesigned to match the reference mockup.
 *
 * Visual design:
 * - Top airport terminal photograph (bg_login_airport_header) with sunset lighting.
 * - Flowing curved cream overlay transitioning into the main content.
 * - Centered official WayMate logo asset.
 * - Welcome Back heading and travel supporting text.
 * - Email input with envelope icon, rounded border, and validation.
 * - Full-width rounded green Log In button with forward arrow.
 * - Centered "or" divider.
 * - Rounded outlined "Continue with Google" button with Google G icon.
 * - "New to WayMate? Create Account" navigation link.
 * - Bottom airport landscape illustration (bg_login_airport_footer) with control tower,
 *   terminal building, airplane, and green hills.
 */
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToOtp: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var localErrorMessage by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginCreamBackground)
    ) {
        // Main scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Top Section: Airport Terminal Photograph with Curved Cream Overlay & Centered Logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp)
            ) {
                // Large airport terminal photograph with sunset lighting and ascending plane
                Image(
                    painter = painterResource(id = R.drawable.bg_login_airport_header),
                    contentDescription = "Airport terminal background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alignment = androidx.compose.ui.BiasAlignment(-0.2f, 0f)
                )

                // Flowing curved cream-colored overlay that transitions into the main content
                HeaderCurveOverlay(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .align(Alignment.BottomCenter)
                )

                // Official WayMate Logo centered inside the curved dome
                WayMateLogo(
                    height = 58.dp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                )
            }

            // 2. Middle Section: Form Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Welcome Back Heading
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = LoginTextPrimary,
                        fontSize = 28.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Supporting Text
                Text(
                    text = "Log in to continue your travels\nwith WayMate.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = LoginTextSecondary,
                        fontSize = 15.sp,
                        lineHeight = 21.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(26.dp))

                // Email Input Field
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = {
                        localErrorMessage = null
                        viewModel.onEmailChange(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(LoginInputBackground),
                    placeholder = {
                        Text(
                            text = "Enter your email",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color(0xFF828E7C),
                                fontSize = 15.sp
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Mail,
                            contentDescription = "Email icon",
                            tint = Color(0xFF6B7265)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = LoginInputBackground,
                        unfocusedContainerColor = LoginInputBackground,
                        focusedBorderColor = LoginGreen,
                        unfocusedBorderColor = LoginBorderColor,
                        focusedTextColor = LoginTextPrimary,
                        unfocusedTextColor = LoginTextPrimary,
                        errorBorderColor = WayMateError
                    ),
                    isError = localErrorMessage != null || uiState.errorMessage != null
                )

                // Error Message
                val activeError = localErrorMessage ?: uiState.errorMessage
                if (activeError != null) {
                    Text(
                        text = activeError,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = WayMateError,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(top = 6.dp, start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Log In Button (Full-width rounded green button with right arrow)
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        val email = uiState.email.trim()
                        if (email.isBlank()) {
                            localErrorMessage = "Please enter your email"
                        } else if (!email.contains("@") || !email.contains(".")) {
                            localErrorMessage = "Please enter a valid email address"
                        } else {
                            localErrorMessage = null
                            onNavigateToOtp()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    enabled = !uiState.isLoading,
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LoginGreen,
                        contentColor = Color.White,
                        disabledContainerColor = LoginGreen.copy(alpha = 0.6f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Log In",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Divider ("or")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = Color(0xFFE2E0D4)
                    )
                    Text(
                        text = "or",
                        modifier = Modifier.padding(horizontal = 14.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF6B7265),
                            fontSize = 14.sp
                        )
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = Color(0xFFE2E0D4)
                    )
                }

                // Continue with Google Button
                OutlinedButton(
                    onClick = {
                        // Preserved hook for Google Sign-In
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.dp, LoginBorderColor),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = LoginTextPrimary
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        GoogleLogoIcon(modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Continue with Google",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = LoginTextPrimary,
                                fontSize = 15.sp
                            )
                        )
                    }
                }

                // Sign Up Navigation Link
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "New to WayMate? ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = LoginTextPrimary,
                            fontSize = 14.sp
                        )
                    )
                    Text(
                        text = "Create Account",
                        modifier = Modifier.clickable { onNavigateToSignUp() },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = LoginGreenDark,
                            fontSize = 14.sp,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }
            }

            // 3. Bottom Section: Airport Terminal & Tower Landscape Illustration
            Image(
                painter = painterResource(id = R.drawable.bg_login_airport_footer),
                contentDescription = "Airport illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.BottomCenter
            )
        }

        // Top Back Arrow Button (Overlayed with status bar inset)
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 8.dp, top = 4.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = LoginTextPrimary
            )
        }
    }
}

/**
 * Flowing curved cream overlay that smoothly transitions the top photograph into the cream content.
 */
@Composable
private fun HeaderCurveOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            moveTo(0f, size.height)
            lineTo(0f, size.height * 0.70f)
            // Quadratic bezier arc that peaks upwards at horizontal center
            quadraticTo(
                size.width * 0.5f,
                -size.height * 0.15f,
                size.width,
                size.height * 0.70f
            )
            lineTo(size.width, size.height)
            close()
        }
        drawPath(path = path, color = LoginCreamBackground)
    }
}

/**
 * Authentic Google 4-color 'G' icon rendered with vector precision.
 */
@Composable
private fun GoogleLogoIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val sizePx = size.minDimension
        val strokeW = sizePx * 0.22f
        val radius = (sizePx - strokeW) / 2f
        val centerOffset = Offset(size.width / 2f, size.height / 2f)

        // Red arc (top)
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 180f,
            sweepAngle = 135f,
            useCenter = false,
            topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeW, cap = StrokeCap.Round)
        )
        // Yellow arc (left)
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 135f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeW, cap = StrokeCap.Round)
        )
        // Green arc (bottom)
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 45f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeW, cap = StrokeCap.Round)
        )
        // Blue arc (right)
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = 315f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeW)
        )
        // Blue horizontal crossbar
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(centerOffset.x, centerOffset.y),
            end = Offset(centerOffset.x + radius, centerOffset.y),
            strokeWidth = strokeW,
            cap = StrokeCap.Square
        )
    }
}
