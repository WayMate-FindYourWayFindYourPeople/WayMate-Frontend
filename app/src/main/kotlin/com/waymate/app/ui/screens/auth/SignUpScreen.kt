package com.waymate.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.ui.components.*
import com.waymate.app.ui.theme.*
import com.waymate.app.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSuccessDialog by remember { mutableStateOf(false) }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onNavigateToLogin()
            },
            title = {
                Text(
                    text = "Registration Successful 🎉",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateTextPrimary
                    )
                )
            },
            text = {
                Text(
                    text = "Your WayMate account has been created for ${uiState.fullName}. Please log in with your mobile number to complete OTP verification.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = WayMateTextSecondary)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onNavigateToLogin()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WayMateGreen)
                ) {
                    Text("Proceed to Login", color = androidx.compose.ui.graphics.Color.White)
                }
            },
            containerColor = WayMateCream,
            shape = MaterialTheme.shapes.large
        )
    }

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
                    height = 48.dp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateTextPrimary,
                        fontSize = 28.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Join WayMate for smart airport transfers and shared pools",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = WayMateTextSecondary,
                        fontSize = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Full Name
                WayMateTextField(
                    value = uiState.fullName,
                    onValueChange = viewModel::onFullNameChange,
                    label = "Full Name",
                    placeholder = "Alex Morgan",
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = WayMateGreenDark)
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Mobile Number
                WayMateTextField(
                    value = uiState.mobileNumber,
                    onValueChange = viewModel::onMobileNumberChange,
                    label = "Mobile Number (+91)",
                    placeholder = "9876543210",
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = WayMateGreenDark)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Email
                WayMateTextField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    label = "Email Address",
                    placeholder = "alex.morgan@waymate.com",
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = WayMateGreenDark)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Password
                WayMateTextField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = "Password",
                    placeholder = "At least 6 characters",
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = WayMateGreenDark)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Confirm Password
                WayMateTextField(
                    value = uiState.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    label = "Confirm Password",
                    placeholder = "Re-enter password",
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = WayMateGreenDark)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Terms and Conditions checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = uiState.agreeToTerms,
                        onCheckedChange = viewModel::onAgreeToTermsChange,
                        colors = CheckboxDefaults.colors(
                            checkedColor = WayMateGreen,
                            checkmarkColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "I agree to the WayMate Terms of Service, Ride Sharing Community Guidelines & Privacy Policy",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = WayMateTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    )
                }

                if (uiState.errorMessage != null) {
                    Text(
                        text = uiState.errorMessage ?: "",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = WayMateError,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WayMateButton(
                    text = "Create Account",
                    onClick = {
                        viewModel.submitSignUp {
                            showSuccessDialog = true
                        }
                    },
                    isLoading = uiState.isLoading
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Already registered? ",
                        style = MaterialTheme.typography.bodyMedium.copy(color = WayMateTextSecondary)
                    )
                    TextButton(onClick = onNavigateToLogin) {
                        Text(
                            text = "Log In",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = WayMateGreenDark
                            )
                        )
                    }
                }
            }
        }
    }
}
