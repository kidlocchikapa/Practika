// navigation/NavGraph.kt
package com.example.practika.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.practika.screens.CallingScreen
import com.example.practika.screens.HomeScreen
import com.example.practika.screens.InCallScreen
import com.example.practika.screens.LandingScreen
import com.example.practika.screens.LoginScreen
import com.example.practika.screens.RegistrationScreen

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
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Registration.route)
                }
            )
        }

        composable(route = Screen.Registration.route) {
            RegistrationScreen(
                onRegistrationSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Registration.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
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
