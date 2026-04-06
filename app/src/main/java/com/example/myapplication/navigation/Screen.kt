package com.example.myapplication.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object SignIn : Screen("sign_in")
    data object SignUp : Screen("sign_up")
    data object Pin : Screen("pin")
    data object Home : Screen("home")
    data object Scheduled : Screen("scheduled")
    data object InfoHelp : Screen("info_help")
}