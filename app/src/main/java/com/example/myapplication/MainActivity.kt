package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.AppContainer
import com.example.myapplication.navigation.AppNavGraphWithViewModels
import com.example.myapplication.ui.screens.auth.AuthViewModel
import com.example.myapplication.ui.screens.auth.AuthViewModelFactory
import com.example.myapplication.ui.screens.home.HomeViewModel
import com.example.myapplication.ui.screens.home.HomeViewModelFactory
import com.example.myapplication.ui.screens.scheduled.ScheduledPaymentsViewModel
import com.example.myapplication.ui.screens.scheduled.ScheduledPaymentsViewModelFactory
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContainer = AppContainer(applicationContext)

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()

                val homeViewModel: HomeViewModel = viewModel(
                    factory = HomeViewModelFactory(appContainer.transactionRepository)
                )

                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(
                        authRepository = appContainer.authRepository,
                        userSessionManager = appContainer.userSessionManager
                    )
                )

                val scheduledPaymentsViewModel: ScheduledPaymentsViewModel = viewModel(
                    factory = ScheduledPaymentsViewModelFactory(
                        scheduledPaymentRepository = appContainer.scheduledPaymentRepository,
                        transactionRepository = appContainer.transactionRepository
                    )
                )

                AppNavGraphWithViewModels(
                    navController = navController,
                    homeViewModel = homeViewModel,
                    authViewModel = authViewModel,
                    scheduledPaymentsViewModel = scheduledPaymentsViewModel
                )
            }
        }
    }
}