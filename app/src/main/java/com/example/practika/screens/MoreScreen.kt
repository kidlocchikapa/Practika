package com.example.practika.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practika.data.ThemeManager
import com.example.practika.data.UserData

@Composable
fun MoreScreen(onLogout: () -> Unit) {
    var isDarkTheme by remember { ThemeManager.isDarkTheme }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ProfileCard(phoneNumber = UserData.phoneNumber ?: "+1 234 567 890")
        Spacer(modifier = Modifier.height(24.dp))

        MoreListItem(icon = Icons.Outlined.Notifications, text = "Notifications", onClick = { /* TODO */ })
        Divider(modifier = Modifier.padding(horizontal = 16.dp))

        // Theme Toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Icon(imageVector = Icons.Outlined.BrightnessMedium, contentDescription = "Theme", modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Dark Mode", fontSize = 18.sp)
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = isDarkTheme, onCheckedChange = { isDarkTheme = it })
        }

        Divider(modifier = Modifier.padding(horizontal = 16.dp))
        MoreListItem(icon = Icons.Outlined.Logout, text = "Logout", onClick = onLogout)
        Divider(modifier = Modifier.padding(horizontal = 16.dp))
        MoreListItem(icon = Icons.Outlined.Delete, text = "Delete Account", onClick = { /* TODO */ })
    }
}

@Composable
fun ProfileCard(phoneNumber: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "Profile",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = "User", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = phoneNumber, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun MoreListItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Icon(imageVector = icon, contentDescription = text, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Outlined.ChevronRight, contentDescription = null)
    }
}
