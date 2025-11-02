// navigation/NavGraph.kt
package com.example.practika.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.practika.screens.CallingScreen
import com.example.practika.screens.HomeScreen
import com.example.practika.screens.InCallScreen
import com.example.practika.screens.LandingScreen
import com.example.practika.screens.LoginScreen
import com.example.practika.screens.OtpScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Landing.route
    ) {
        composable(route = Screen.Landing.route) {
            LandingScreen(
                onTimeout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Landing.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToOtp = {
                    navController.navigate(Screen.Otp.createRoute(it))
                }
            )
        }

        composable(
            route = Screen.Otp.route,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            OtpScreen(
                phoneNumber = phoneNumber,
                onOtpVerified = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToCalling = {
                    navController.navigate(Screen.Calling.route)
                }
            )
        }

        composable(route = Screen.Calling.route) {
            CallingScreen()
        }

        composable(route = Screen.InCall.route) {
            InCallScreen()
        }

        // Add other screens here as you develop them
    }
}
