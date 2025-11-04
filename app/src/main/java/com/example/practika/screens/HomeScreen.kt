package com.example.practika.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.practika.data.CallHistory
import com.example.practika.data.allProviders
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class CTACard(
    val imageUrl: String,
    val contentDescription: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onNavigateToProviderList: () -> Unit,
    onNavigateToCategoryList: () -> Unit,
    onNavigateToGentTalk: () -> Unit
) {
    val callHistoryList = remember {
        allProviders.map { provider ->
            CallHistory(
                id = provider.tollFreeNumber,
                providerName = provider.name,
                category = provider.category,
                duration = "${(1..15).random()}:${String.format("%02d", (0..59).random())}",
                date = Date(System.currentTimeMillis() - (Math.random() * 1000000000).toLong()),
                providerLogo = provider.logo
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            SlidingCTACards()
        }

        item {
            QuickActionsCard(
                onNavigateToProviderList = onNavigateToProviderList,
                onNavigateToCategoryList = onNavigateToCategoryList,
                onNavigateToGentTalk = onNavigateToGentTalk
            )
        }

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
            Spacer(modifier = Modifier.height(80.dp))
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
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        AsyncImage(
            model = card.imageUrl,
            contentDescription = card.contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun QuickActionsCard(
    onNavigateToProviderList: () -> Unit,
    onNavigateToCategoryList: () -> Unit,
    onNavigateToGentTalk: () -> Unit
) {
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
            Text(
                text = "Quick Actions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Top
            ) {
                QuickActionItem(
                    icon = Icons.Outlined.Call,
                    label = "Live Call",
                    onClick = onNavigateToProviderList
                )
                QuickActionItem(
                    icon = Icons.Outlined.Chat,
                    label = "GentTalk",
                    onClick = onNavigateToGentTalk
                )
                QuickActionItem(
                    icon = Icons.Outlined.Group,
                    label = "Providers",
                    onClick = onNavigateToProviderList
                )
                QuickActionItem(
                    icon = Icons.Outlined.Category,
                    label = "Categories",
                    onClick = onNavigateToCategoryList
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
            .width(64.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
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
        modifier = Modifier.fillMaxWidth(),
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
                when (callHistory.providerLogo) {
                    is Int -> {
                        Image(
                            painter = painterResource(id = callHistory.providerLogo as Int),
                            contentDescription = callHistory.providerName,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Fit
                        )
                    }
                    is ImageVector -> {
                        Icon(
                            imageVector = callHistory.providerLogo as ImageVector,
                            contentDescription = callHistory.providerName,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

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
