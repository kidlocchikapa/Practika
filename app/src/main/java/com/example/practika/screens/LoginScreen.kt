package com.example.practika.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practika.theme.PrimaryLight

@Composable
fun LoginScreen(onNavigateToOtp: (String) -> Unit) {
    val context = LocalContext.current
    var phoneNumbers by remember { mutableStateOf<List<String>>(emptyList()) }
    var detectionComplete by remember { mutableStateOf(false) }
    var permissionsRequested by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions.values.all { it }) {
                phoneNumbers = getPhoneNumbers(context)
            }
            detectionComplete = true
            permissionsRequested = true
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS))
    }

    Box(
        modifier = Modifier.fillMaxSize().background(PrimaryLight)
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

                if (!permissionsRequested) {
                    // Show a loading indicator or placeholder while waiting for permission result
                    CircularProgressIndicator()
                } else if (phoneNumbers.isNotEmpty()) {
                    Text("Please select your phone number to continue.", fontSize = 16.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(32.dp))
                    phoneNumbers.forEachIndexed { index, number ->
                        SimCard(simSlot = index + 1, phoneNumber = number) { onNavigateToOtp(number) }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                } else if (detectionComplete) {
                     ManualInputFallback(onNavigateToOtp = onNavigateToOtp)
                } else {
                     PermissionDeniedContent(context = context) { 
                        permissionLauncher.launch(arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS))
                     }
                }
            }
        }
    }
}

@Composable
private fun ManualInputFallback(onNavigateToOtp: (String) -> Unit) {
    var manualPhoneNumber by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Couldn't detect your number automatically. Please enter it below.",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = manualPhoneNumber,
            onValueChange = { manualPhoneNumber = it },
            label = { Text("Phone Number") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onNavigateToOtp(manualPhoneNumber) },
            enabled = manualPhoneNumber.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Continue")
        }
    }
}

@Composable
private fun PermissionDeniedContent(context: Context, onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(
            "This app needs permission to read your phone number to simplify registration. Please enable it in the app settings.",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = onRetry) {
                Text("Retry")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", context.packageName, null)
                context.startActivity(intent)
            }) {
                Text("Open Settings")
            }
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

@SuppressLint("MissingPermission")
private fun getPhoneNumbers(context: Context): List<String> {
    val numbers = mutableListOf<String>()
    val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as? SubscriptionManager
        ?: return emptyList()

    try {
        subscriptionManager.activeSubscriptionInfoList?.forEach { info ->
            val number = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                subscriptionManager.getPhoneNumber(info.subscriptionId)
            } else {
                @Suppress("DEPRECATION")
                (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).createForSubscriptionId(info.subscriptionId).line1Number
            }
            if (!number.isNullOrEmpty()) {
                numbers.add(number)
            }
        }
    } catch (e: SecurityException) {
        // Permissions are checked by the caller.
    }
    return numbers.distinct().filter { it.isNotEmpty() }
}
