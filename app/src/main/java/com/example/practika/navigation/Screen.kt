package com.example.practika.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Login : Screen("login")
    object Otp : Screen("otp/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "otp/$phoneNumber"
    }
    object Home : Screen("home")
    object Chat : Screen("chat")
    object More : Screen("more")
    object ProviderList : Screen("provider_list")
    object CategoryList : Screen("category_list")
    object LiveCall : Screen("live_call/{providerName}") {
        fun createRoute(providerName: String) = "live_call/$providerName"
    }
    object InCall : Screen("in_call/{providerName}") {
        fun createRoute(providerName: String) = "in_call/$providerName"
    }
}