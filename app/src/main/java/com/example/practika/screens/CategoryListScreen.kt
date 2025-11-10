package com.example.practika.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practika.data.Provider
import com.example.practika.data.allCategories
import com.example.practika.data.getProvidersForCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(onNavigateToCall: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("All Categories") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(allCategories) { category ->
                ExpandableCategory(
                    categoryName = category.name,
                    providers = getProvidersForCategory(category.name),
                    onNavigateToCall = onNavigateToCall
                )
            }
        }
    }
}

@Composable
fun ExpandableCategory(
    categoryName: String,
    providers: List<Provider>,
    onNavigateToCall: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Category Icon
                    val category = allCategories.find { it.name == categoryName }
                    category?.let { 
                        Icon(
                            imageVector = it.icon,
                            contentDescription = "$categoryName Icon",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(end = 16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(text = categoryName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Icon(
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    modifier = Modifier.rotate(if (isExpanded) 180f else 0f)
                )
            }
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)) {
                providers.forEach { provider ->
                    SubProviderListItem(provider = provider, onClick = { onNavigateToCall(provider.name) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun SubProviderListItem(provider: Provider, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (provider.logo) {
            is Int -> {
                Image(
                    painter = painterResource(id = provider.logo as Int),
                    contentDescription = "${provider.name} Logo",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
            }
            is ImageVector -> {
                Icon(
                    imageVector = provider.logo as ImageVector,
                    contentDescription = "${provider.name} Logo",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = provider.name, fontSize = 16.sp)
    }
}
