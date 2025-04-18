package com.example.danusin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.danusin.models.Product
import com.example.danusin.ui.theme.DanusInTheme

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
    var showSplash by remember { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    when {
        showSplash -> {
            SplashScreen(onExploreClick = { showSplash = false })
        }
        currentScreen is Screen.Home -> {
            HomeScreen(onProductClick = { product ->
                selectedProduct = product
                currentScreen = Screen.ProductDetail
            })
        }
        currentScreen is Screen.ProductDetail && selectedProduct != null -> {
            ProductDetailScreen(
                product = selectedProduct!!,
                onBackClick = { currentScreen = Screen.Home }
            )
        }
    }
}

sealed class Screen {
    object Home : Screen()
    object ProductDetail : Screen()
}
