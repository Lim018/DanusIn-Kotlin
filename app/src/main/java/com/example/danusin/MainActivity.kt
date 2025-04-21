package com.example.danusin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.danusin.models.Product
import com.example.danusin.ui.theme.DanusInTheme
import com.example.danusin.viewmodels.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DanusInTheme {
                DanusInApp()
            }
        }
    }
}

@Composable
fun DanusInApp() {
    val authViewModel: AuthViewModel = viewModel()
    var showSplash by remember { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    when (currentScreen) {
        Screen.Splash -> {
            SplashScreen(onExploreClick = {
                currentScreen = Screen.Login
            })
        }
        Screen.Login -> {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = { currentScreen = Screen.Register },
                onLoginSuccess = { currentScreen = Screen.Home }
            )
        }
        Screen.Register -> {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { currentScreen = Screen.Login },
                onRegisterSuccess = { currentScreen = Screen.Home }
            )
        }
        Screen.Home -> {
            HomeScreen(
                authViewModel = authViewModel,
                onProductClick = { product ->
                    selectedProduct = product
                    currentScreen = Screen.ProductDetail
                },
                onProfileClick = {
                    authViewModel.logout()
                    currentScreen = Screen.Login
                }
            )
        }
        Screen.ProductDetail -> {
            selectedProduct?.let { product ->
                ProductDetailScreen(
                    product = product,
                    onBackClick = { currentScreen = Screen.Home }
                )
            }
        }
    }
}

sealed class Screen {
    object Splash : Screen()
    object Login : Screen()
    object Register : Screen()
    object Home : Screen()
    object ProductDetail : Screen()
}
