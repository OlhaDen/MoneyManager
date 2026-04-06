package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.ui.screens.auth.AuthViewModel
import com.example.myapplication.ui.screens.auth.PinScreen
import com.example.myapplication.ui.screens.auth.SignInScreen
import com.example.myapplication.ui.screens.auth.SignUpScreen
import com.example.myapplication.ui.screens.home.HomeScreen
import com.example.myapplication.ui.screens.home.HomeViewModel
import com.example.myapplication.ui.screens.info.InfoHelpScreen
import com.example.myapplication.ui.screens.scheduled.ScheduledPaymentsScreen
import com.example.myapplication.ui.screens.scheduled.ScheduledPaymentsViewModel
import com.example.myapplication.ui.screens.splash.SplashScreen

@Composable
fun AppNavGraphWithViewModels(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    scheduledPaymentsViewModel: ScheduledPaymentsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                isLoggedIn = authViewModel.isUserLoggedIn(),
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToSignIn = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SignIn.route) {
            SignInScreen(
                viewModel = authViewModel,
                onNextClick = { email ->
                    navController.navigate("pin/$email")
                },
                onSignUpClick = {
                    authViewModel.clearState()
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                viewModel = authViewModel,
                onSignUpSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    authViewModel.clearState()
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "pin/{email}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email").orEmpty()

            PinScreen(
                email = email,
                viewModel = authViewModel,
                onBackClick = {
                    authViewModel.clearState()
                    navController.popBackStack()
                },
                onPinComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onScheduledClick = { navController.navigate(Screen.Scheduled.route) },
                onInfoClick = { navController.navigate(Screen.InfoHelp.route) },
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Screen.Scheduled.route) {
            ScheduledPaymentsScreen(
                viewModel = scheduledPaymentsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.InfoHelp.route) {
            InfoHelpScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}