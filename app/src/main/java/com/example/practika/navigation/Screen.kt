
// navigation/Screen.kt
package com.example.practika.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Login : Screen("login")
    object Registration : Screen("registration")
    object Home : Screen("home")
    object Chat : Screen("chat")
    object LiveCall : Screen("live_call")
    object Selection : Screen("selection")
    object Provider : Screen("provider")
    object Call : Screen("call")
    object Calling : Screen("calling")
    object InCall : Screen("incall")
}