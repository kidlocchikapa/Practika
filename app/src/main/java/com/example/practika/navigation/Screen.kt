
// navigation/Screen.kt
package com.example.practika.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Login : Screen("login")
    object Otp : Screen("otp/{phoneNumber}") { // Route with argument
        fun createRoute(phoneNumber: String) = "otp/$phoneNumber"
    }
    object Home : Screen("home")
    object Chat : Screen("chat")
    object LiveCall : Screen("live_call")
    object Selection : Screen("selection")
    object Provider : Screen("provider")
    object Call : Screen("call")
    object Calling : Screen("calling")
    object InCall : Screen("incall")
}