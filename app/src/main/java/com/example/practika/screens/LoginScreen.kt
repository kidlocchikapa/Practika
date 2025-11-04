package com.example.practika.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.SubscriptionManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practika.theme.PrimaryLight
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoginScreen(onNavigateToOtp: (String) -> Unit) {
    val context = LocalContext.current
    var phoneNumbers by remember { mutableStateOf<List<String>>(emptyList()) }

    val readPhoneNumbersPermission = rememberPermissionState(Manifest.permission.READ_PHONE_NUMBERS)

    LaunchedEffect(readPhoneNumbersPermission.status) {
        if (readPhoneNumbersPermission.status.isGranted) {
            phoneNumbers = getPhoneNumbers(context)
        } else {
            readPhoneNumbersPermission.launchPermissionRequest()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryLight)
    ) {
        Text(
            text = "Gentcaller",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 100.dp)
        )

        Card(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(450.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Verify Your Number", fontSize = 26.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))

                if (readPhoneNumbersPermission.status.isGranted) {
                    if (phoneNumbers.isNotEmpty()) {
                        Text("Please select your phone number to continue.", fontSize = 16.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(32.dp))
                        phoneNumbers.forEachIndexed { index, number ->
                            SimCard(simSlot = index + 1, phoneNumber = number) {
                                onNavigateToOtp(number)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    } else {
                        Text("No active SIM cards found.", textAlign = TextAlign.Center)
                    }
                } else {
                    PermissionDeniedContent(shouldShowRationale = readPhoneNumbersPermission.status.shouldShowRationale, context = context)
                }
            }
        }
    }
}

@Composable
private fun PermissionDeniedContent(shouldShowRationale: Boolean, context: Context) {
    val text = if (shouldShowRationale) {
        "Reading your phone number is important for registration. Please grant the permission to continue."
    } else {
        "Permission to read phone numbers is required. Please enable it in the app settings."
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", context.packageName, null)
            context.startActivity(intent)
        }) {
            Text("Open Settings")
        }
    }
}

@Composable
private fun SimCard(simSlot: Int, phoneNumber: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Phone, contentDescription = "SIM", tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(16.dp))
            Column {
                Text("SIM $simSlot", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(phoneNumber, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@SuppressLint("MissingPermission", "HardwareIds")
private fun getPhoneNumbers(context: Context): List<String> {
    val numbers = mutableListOf<String>()
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as? SubscriptionManager
            subscriptionManager?.activeSubscriptionInfoList?.forEach { info ->
                val number = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    subscriptionManager.getPhoneNumber(info.subscriptionId)
                } else {
                     @Suppress("DEPRECATION")
                    (context.getSystemService(Context.TELEPHONY_SERVICE) as android.telephony.TelephonyManager).line1Number
                }
                if (!number.isNullOrEmpty()) {
                    numbers.add(number)
                }
            }
        }
    } catch (e: Exception) {
        // Silently handle exceptions
    }
    return numbers.distinct().filter { it.isNotEmpty() }
}
