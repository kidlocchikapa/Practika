package com.example.practika

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.practika.data.allProviders
import com.example.practika.navigation.NavGraph
import com.example.practika.navigation.Screen
import com.example.practika.theme.PractikaTheme
import com.example.practika.theme.PrimaryLight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Register phone account for in-app calling
        CallManager.registerPhoneAccount(this)
        
        setContent {
            PractikaTheme {
                MainScreen()
            }
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem("GentTalk", Screen.Chat.route, Icons.Filled.Chat, Icons.Outlined.Chat),
        BottomNavItem("More", Screen.More.route, Icons.Filled.MoreHoriz, Icons.Outlined.MoreHoriz)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showNavBars = currentDestination?.route in bottomNavItems.map { it.route }

    Scaffold(
        topBar = {
            if (showNavBars) {
                AppTopAppBar(navController = navController)
            }
        },
        bottomBar = {
            if (showNavBars) {
                AppBottomNavBar(currentDestination, bottomNavItems) {
                    navController.navigate(it) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<com.example.practika.data.Provider>()) }
    var isSearchDropdownExpanded by remember { mutableStateOf(false) }
    val isDark = ThemeManager.isDarkTheme.value
    val colorScheme = MaterialTheme.colorScheme

    TopAppBar(
        title = { 
            Text(
                "Gentcaller",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Color.White
                )
            )
        },
        actions = {
            Box {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        searchResults = if (it.isNotBlank()) {
                            allProviders.filter { provider -> provider.name.contains(it, ignoreCase = true) }
                        } else {
                            emptyList()
                        }
                        isSearchDropdownExpanded = searchResults.isNotEmpty()
                    },
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .width(180.dp)
                        .background(
                            if (isDark) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.15f),
                            CircleShape
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    singleLine = true,
                    textStyle = TextStyle(color = if (isDark) Color.White else Color.White),
                    decorationBox = { innerTextField ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Search, 
                                contentDescription = "Search", 
                                tint = if (isDark) Color.White.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(Modifier.width(8.dp))
                            Box {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        "search provider", 
                                        color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.8f),
                                        fontSize = 14.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    }
                )
                DropdownMenu(
                    expanded = isSearchDropdownExpanded,
                    onDismissRequest = { isSearchDropdownExpanded = false },
                    modifier = Modifier.width(220.dp)
                ) {
                    searchResults.forEach { provider ->
                        DropdownMenuItem(
                            text = { Text(provider.name) },
                            onClick = {
                                isSearchDropdownExpanded = false
                                searchQuery = ""
                                navController.navigate(Screen.LiveCall.createRoute(provider.name))
                            }
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isDark) BackgroundDark else PrimaryLight
        )
    )
}

@Composable
fun AppBottomNavBar(
    currentDestination: androidx.navigation.NavDestination?,
    items: List<BottomNavItem>,
    onNavigate: (String) -> Unit
) {
    val isDark = ThemeManager.isDarkTheme.value
    
    NavigationBar(
        containerColor = if (isDark) BackgroundDark else PrimaryLight
    ) {
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                label = { 
                    Text(
                        item.label, 
                        color = if (isDark) {
                            if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
                        } else {
                            if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
                        }
                    ) 
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = if (isDark) {
                            if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
                        } else {
                            if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
                        }
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = if (isDark) {
                        Color.White.copy(alpha = 0.08f)
                    } else {
                        Color.White.copy(alpha = 0.15f)
                    },
                    selectedIconColor = if (isDark) Color.White else Color.White,
                    unselectedIconColor = if (isDark) Color.White.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.7f)
                )
            )
        }
    }
}
