package com.example.practika.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practika.data.Provider
import com.example.practika.data.allProviders

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderListScreen(onNavigateToCall: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Select Provider") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(allProviders) { provider ->
                ProviderListItem(provider = provider, onClick = {
                    onNavigateToCall(provider.name)
                })
            }
        }
    }
}

@Composable
fun ProviderListItem(provider: Provider, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (provider.logo) {
                is Int -> {
                    Image(
                        painter = painterResource(id = provider.logo as Int),
                        contentDescription = "${provider.name} Logo",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                }
                is ImageVector -> {
                    Icon(
                        imageVector = provider.logo as ImageVector,
                        contentDescription = "${provider.name} Logo",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = provider.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
