package com.example.practika.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practika.theme.LightBlue
import com.example.practika.theme.PrimaryColor
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(
    phoneNumber: String,
    onOtpVerified: () -> Unit
) {
    var otp by remember { mutableStateOf("") }
    var timer by remember { mutableStateOf(60) }

    LaunchedEffect(key1 = timer) {
        if (timer > 0) {
            delay(1000L)
            timer--
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlue)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Verify your number",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter the OTP sent to $phoneNumber",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(48.dp))

        OtpTextField(otp = otp, onOtpChange = { otp = it })

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onOtpVerified() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = otp.length == 4, // Assuming a 4-digit OTP
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            Text("Verify", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { if (timer == 0) timer = 60 }, enabled = timer == 0) {
            Text(
                if (timer > 0) "Resend OTP in ${timer}s" else "Resend OTP",
                color = if (timer > 0) Color.Gray else PrimaryColor
            )
        }
    }
}

@Composable
fun OtpTextField(otp: String, onOtpChange: (String) -> Unit) {
    BasicTextField(
        value = otp,
        onValueChange = {
            if (it.length <= 4) { // Limit to 4 digits
                onOtpChange(it)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(4) { index ->
                    val char = when {
                        index < otp.length -> otp[index].toString()
                        else -> ""
                    }
                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(64.dp)
                            .border(
                                1.dp,
                                if (index < otp.length) PrimaryColor else Color.Gray,
                                RoundedCornerShape(8.dp)
                            )
                            .background(
                                Color.White,
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    )
}
