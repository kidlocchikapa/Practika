
package com.example.practika.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practika.theme.PrimaryColor

@Composable
fun CallingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryColor)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Calling Agent...",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(32.dp))

        CircularProgressIndicator(color = Color.White)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Connecting you to an available agent",
            fontSize = 16.sp,
            color = Color.White
        )

        Text(
            text = "Ringing...",
            fontSize = 16.sp,
            color = Color.White
        )
    }
}
