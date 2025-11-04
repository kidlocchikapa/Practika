package com.example.practika.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practika.data.allProviders

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveCallScreen(providerName: String, onHangup: () -> Unit, onChatWithGentTalk: () -> Unit) {
    val provider = allProviders.find { it.name == providerName } ?: return

    val infiniteTransition = rememberInfiniteTransition(label = "LiveCallAnimation")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "LiveCallProgress"
    )

    val primaryColor = MaterialTheme.colorScheme.primary

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Connecting...") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF2F2F2)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .drawBehind {
                            drawArc(
                                color = Color.LightGray,
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 8f)
                            )
                            drawArc(
                                color = primaryColor,
                                startAngle = -90f,
                                sweepAngle = 360 * animatedProgress,
                                useCenter = false,
                                style = Stroke(width = 8f)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    when (provider.logo) {
                        is Int -> {
                            Image(
                                painter = painterResource(id = provider.logo as Int),
                                contentDescription = provider.name,
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Fit
                            )
                        }
                        is androidx.compose.ui.graphics.vector.ImageVector -> {
                            Icon(
                                imageVector = provider.logo as androidx.compose.ui.graphics.vector.ImageVector,
                                contentDescription = provider.name,
                                modifier = Modifier.size(150.dp),
                                tint = primaryColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Connecting to ${provider.name}...",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Searching for a free agent...",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.weight(1f))

                // Action buttons at the bottom
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 48.dp, start = 32.dp, end = 32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onChatWithGentTalk,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Chat with GentTalk", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = onHangup,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.CallEnd, contentDescription = "Hang up", tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Hang Up", color = Color.White)
                    }
                }
            }
        }
    }
}
