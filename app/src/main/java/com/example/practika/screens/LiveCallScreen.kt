package com.example.practika.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.practika.data.allProviders

// Main composable that orchestrates the permission flow and calling logic
@Composable
fun LiveCallScreen(
    providerName: String,
    onHangup: () -> Unit,
    onNavigateToInCall: (String) -> Unit
) {
    var permissionStatus by remember { mutableStateOf(PermissionStatus.UNDETERMINED) }

    // Use raw strings for permissions to bypass a potential compiler bug
    val permissionsToRequest = arrayOf(
        "android.permission.READ_PHONE_ACCOUNTS",
        "android.permission.CALL_PHONE"
    )

    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            // If all permissions are granted, update status. Otherwise, it's denied.
            permissionStatus = if (permissions.values.all { it }) {
                PermissionStatus.GRANTED
            } else {
                PermissionStatus.DENIED
            }
        }
    )

    when (permissionStatus) {
        PermissionStatus.UNDETERMINED -> {
            // Initially, show connecting UI and request permissions
            ConnectingUI(providerName, onHangup)
            LaunchedEffect(Unit) {
                permissionsLauncher.launch(permissionsToRequest)
            }
        }
        PermissionStatus.GRANTED -> {
            // Once permissions are granted, proceed to the calling logic
            CallingContent(providerName, onHangup, onNavigateToInCall)
        }
        PermissionStatus.DENIED -> {
            // If permissions are denied, show a clear explanation and options
            PermissionDeniedUI(onCancel = onHangup, onRetry = {
                permissionsLauncher.launch(permissionsToRequest)
            })
        }
    }
}

// Enum to manage the state of the permission request
private enum class PermissionStatus { UNDETERMINED, GRANTED, DENIED }

// Handles the actual call initiation and SIM selection logic
@SuppressLint("MissingPermission") // Permissions are handled by the caller composable
@Composable
private fun CallingContent(
    providerName: String,
    onHangup: () -> Unit,
    onNavigateToInCall: (String) -> Unit
) {
    val context = LocalContext.current
    var showSimDialog by remember { mutableStateOf(false) }
    var phoneAccounts by remember { mutableStateOf<List<PhoneAccountHandle>>(emptyList()) }
    val provider = remember { allProviders.find { it.name == providerName } }
    val numberToCall = provider?.tollFreeNumber

    // This effect runs once to check for SIMs and initiate the call flow
    LaunchedEffect(Unit) {
        if (numberToCall.isNullOrBlank()) {
            onHangup()
            return@LaunchedEffect
        }

        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        try {
            val accounts = telecomManager.callablePhoneAccounts
            if (accounts.size > 1) {
                phoneAccounts = accounts
                showSimDialog = true
            } else {
                makeCall(context, numberToCall, null)
                onNavigateToInCall(providerName)
            }
        } catch (e: SecurityException) {
            // Failsafe for devices with unusual security restrictions
            makeCall(context, numberToCall, null)
            onNavigateToInCall(providerName)
        }
    }

    if (showSimDialog) {
        SimSelectionDialog(
            phoneAccountHandles = phoneAccounts,
            onSimSelected = { handle ->
                showSimDialog = false
                makeCall(context, numberToCall, handle)
                onNavigateToInCall(providerName)
            },
            onDismiss = {
                showSimDialog = false
                onHangup()
            }
        )
    } else {
        // Show a connecting UI while the check is happening
        ConnectingUI(providerName, onHangup)
    }
}

// Places the call using the selected SIM (or default if none selected)
@SuppressLint("MissingPermission") // Permissions are handled by the caller composable
private fun makeCall(context: Context, number: String?, handle: PhoneAccountHandle?) {
    if (number.isNullOrBlank()) return
    val uri = Uri.fromParts("tel", number, null)

    val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
    val extras = Bundle().apply {
        if (handle != null) {
            putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, handle)
        }
    }
    try {
        CallManager.makeCall(context, number)
    } catch (e: SecurityException) {
        // Fallback to default dialer if permission is denied
        telecomManager.placeCall(uri, extras)
    }
}


@Composable
private fun PermissionDeniedUI(onCancel: () -> Unit, onRetry: () -> Unit) {
    val context = LocalContext.current
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(32.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "To make calls and choose a SIM, this app needs permissions to access phone accounts and make calls. Please grant these permissions to continue.",
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Row {
                Button(onClick = onRetry) { Text("Retry") }
                Spacer(Modifier.width(16.dp))
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.fromParts("package", context.packageName, null)
                    context.startActivity(intent)
                }) { Text("Open Settings") }
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = onCancel) { Text("Cancel") }
        }
    }
}

@Composable
private fun SimSelectionDialog(
    phoneAccountHandles: List<PhoneAccountHandle>,
    onSimSelected: (PhoneAccountHandle) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Choose SIM to call with", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(phoneAccountHandles) { handle ->
                        val phoneAccount = telecomManager.getPhoneAccount(handle)
                        val label = phoneAccount?.label?.toString() ?: "SIM ${phoneAccountHandles.indexOf(handle) + 1}"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSimSelected(handle) }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = label, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConnectingUI(providerName: String, onCancel: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Connecting to $providerName...", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(64.dp))
            Button(onClick = onCancel) { Text("Cancel") }
        }
    }
}
