package com.example.practika.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.practika.data.UserData
import com.example.practika.screens.*
import kotlinx.coroutines.delay

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Landing.route
    ) {
        composable(route = Screen.Landing.route) {
            LandingScreen(onTimeout = { navController.navigate(Screen.Login.route) })
        }

        composable(route = Screen.Login.route) {
            LoginScreen(onNavigateToOtp = { phoneNumber ->
                navController.navigate(Screen.Otp.createRoute(phoneNumber))
            })
        }

        composable(
            route = Screen.Otp.route,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            OtpScreen(
                phoneNumber = phoneNumber,
                onOtpVerified = {
                    UserData.phoneNumber = phoneNumber
                    navController.navigate(Screen.Home.route) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToProviderList = { navController.navigate(Screen.ProviderList.route) },
                onNavigateToCategoryList = { navController.navigate(Screen.CategoryList.route) },
                onNavigateToGentTalk = { navController.navigate(Screen.Chat.route) }
            )
        }

        composable(route = Screen.ProviderList.route) {
            ProviderListScreen(
                onNavigateToCall = { providerName ->
                    navController.navigate(Screen.LiveCall.createRoute(providerName))
                }
            )
        }

        composable(route = Screen.CategoryList.route) {
            CategoryListScreen(onNavigateToCall = { providerName ->
                navController.navigate(Screen.LiveCall.createRoute(providerName))
            })
        }

        composable(
            route = Screen.LiveCall.route,
            arguments = listOf(navArgument("providerName") { type = NavType.StringType })
        ) { backStackEntry ->
            val providerName = backStackEntry.arguments?.getString("providerName") ?: ""
            LaunchedEffect(key1 = providerName) {
                delay(3000) // Simulate call connecting
                navController.navigate(Screen.InCall.createRoute(providerName)) {
                    popUpTo(Screen.LiveCall.route) { inclusive = true } // Replace connecting screen
                }
            }
            LiveCallScreen(
                providerName = providerName,
                onHangup = { navController.popBackStack() },
                onChatWithGentTalk = { navController.navigate(Screen.Chat.route) }
            )
        }

        composable(
            route = Screen.InCall.route,
            arguments = listOf(navArgument("providerName") { type = NavType.StringType })
        ) { backStackEntry ->
            val providerName = backStackEntry.arguments?.getString("providerName") ?: ""
            InCallScreen(
                providerName = providerName,
                onHangup = { navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                } }
            )
        }

        composable(route = Screen.Chat.route) {
            ChatScreen()
        }

        composable(route = Screen.More.route) {
            MoreScreen(onLogout = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                }
            })
        }
    }
}
