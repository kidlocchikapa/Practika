// screens/HomeScreen.kt
package com.example.practika.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Data classes for call history
data class CallHistory(
    val id: String,
    val providerName: String,
    val category: String,
    val duration: String,
    val date: Date,
    val categoryIcon: ImageVector,
    val categoryColor: Color
)

data class CTACard(
    val imageUrl: String,
    val contentDescription: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onNavigateToCalling: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    // Sample call history data
    val callHistoryList = remember {
        listOf(
            CallHistory(
                id = "1",
                providerName = "ESCOM",
                category = "Utility",
                duration = "5:42",
                date = Date(System.currentTimeMillis() - 86400000),
                categoryIcon = Icons.Default.Star,
                categoryColor = Color(0xFF2196F3)
            ),
            CallHistory(
                id = "2",
                providerName = "Airtel",
                category = "Network",
                duration = "3:15",
                date = Date(System.currentTimeMillis() - 172800000),
                categoryIcon = Icons.Default.Star,
                categoryColor = Color(0xFF9C27B0)
            ),
            CallHistory(
                id = "3",
                providerName = "Police",
                category = "Security",
                duration = "12:30",
                date = Date(System.currentTimeMillis() - 259200000),
                categoryIcon = Icons.Default.Security,
                categoryColor = Color(0xFFF44336)
            ),
            CallHistory(
                id = "4",
                providerName = "TNM",
                category = "Network",
                duration = "2:48",
                date = Date(System.currentTimeMillis() - 345600000),
                categoryIcon = Icons.Default.Star,
                categoryColor = Color(0xFF9C27B0)
            ),
            CallHistory(
                id = "5",
                providerName = "Central Hospital",
                category = "Health",
                duration = "8:20",
                date = Date(System.currentTimeMillis() - 432000000),
                categoryIcon = Icons.Default.Favorite,
                categoryColor = Color(0xFF4CAF50)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gent Caller") },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Menu"
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Personal Details") },
                            onClick = {
                                showMenu = false
                                // Navigate to personal details
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("App Details") },
                            onClick = {
                                showMenu = false
                                // Navigate to app details
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Info, contentDescription = null)
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                showMenu = false
                                onLogout()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.ExitToApp, contentDescription = null)
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Navigate to chat */ },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Box {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Open Chat"
                    )
                    // AI Badge
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 8.dp, y = (-8).dp)
                            .size(20.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondary,
                        shadowElevation = 4.dp
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "AI",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Sliding CTA Cards with Images
            item {
                SlidingCTACards()
            }

            // Quick Actions Card with Grid Layout
            item {
                QuickActionsCard(onNavigateToCalling = onNavigateToCalling)
            }

            // Call History Section
            item {
                Text(
                    text = "Recent Calls",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }

            items(callHistoryList) { callHistory ->
                CallHistoryItem(callHistory = callHistory)
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // Extra space for FAB
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlidingCTACards() {
    val ctaCards = remember {
        listOf(
            CTACard(
                imageUrl = "https://images.unsplash.com/photo-1557804506-669a67965ba0?w=800&q=80",
                contentDescription = "Team collaboration"
            ),
            CTACard(
                imageUrl = "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=800&q=80",
                contentDescription = "Customer support"
            ),
            CTACard(
                imageUrl = "https://images.unsplash.com/photo-1551434678-e076c223a692?w=800&q=80",
                contentDescription = "Business communication"
            )
        )
    }

    val pagerState = rememberPagerState(pageCount = { ctaCards.size })

    // Auto-scroll effect
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            CTACardItem(card = ctaCards[page])
        }

        // Page indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(ctaCards.size) { index ->
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                )
                if (index != ctaCards.size - 1) {
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }
    }
}

@Composable
fun CTACardItem(card: CTACard) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(12.dp), // Smaller rounded edges
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        AsyncImage(
            model = card.imageUrl,
            contentDescription = card.contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp)), // Clip image to match card shape
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun QuickActionsCard(onNavigateToCalling: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quick Actions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { /* TODO: Handle See More */ }) {
                    Text("See more")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Top
            ) {
                QuickActionItem(
                    icon = Icons.Outlined.Call,
                    label = "Live Call",
                    onClick = onNavigateToCalling
                )
                QuickActionItem(
                    icon = Icons.Outlined.Chat,
                    label = "AI Chat",
                    onClick = { /* TODO */ }
                )
                QuickActionItem(
                    icon = Icons.Outlined.Group,
                    label = "Providers",
                    onClick = { /* TODO */ }
                )
                QuickActionItem(
                    icon = Icons.Outlined.History,
                    label = "History",
                    onClick = { /* TODO */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Top
            ) {
                QuickActionItem(
                    icon = Icons.Outlined.Category,
                    label = "Categories",
                    onClick = { /* TODO */ }
                )
                QuickActionItem(
                    icon = Icons.Outlined.AccountBalanceWallet,
                    label = "Wallet",
                    onClick = { /* TODO */ }
                )
                QuickActionItem(
                    icon = Icons.Outlined.Settings,
                    label = "Settings",
                    onClick = { /* TODO */ }
                )
                QuickActionItem(
                    icon = Icons.Outlined.HelpOutline,
                    label = "Help",
                    onClick = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
fun QuickActionItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .width(64.dp) // Constrain width
    ) {
        Box(
            modifier = Modifier
                .size(48.dp) // Smaller icon background
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp) // Smaller icon
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CallHistoryItem(callHistory: CallHistory) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Category Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(callHistory.categoryColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = callHistory.categoryIcon,
                        contentDescription = callHistory.category,
                        tint = callHistory.categoryColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Call Details
                Column {
                    Text(
                        text = callHistory.providerName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = callHistory.category,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Duration and Date
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = callHistory.duration,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = SimpleDateFormat("MMM d", Locale.getDefault()).format(callHistory.date),
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
    }
}