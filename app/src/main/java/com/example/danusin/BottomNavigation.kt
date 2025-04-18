package com.example.danusin

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

data class BottomNavItem(
    val name: Int,
    val route: String,
    val icon: Int
)

@Composable
fun BottomNavigation() {
    val navItems = listOf(
        BottomNavItem(
            name = R.string.nav_home,
            route = "home",
            icon = R.drawable.ic_home
        ),
        BottomNavItem(
            name = R.string.nav_search,
            route = "search",
            icon = R.drawable.ic_search
        ),
        BottomNavItem(
            name = R.string.nav_profile,
            route = "profile",
            icon = R.drawable.ic_profile
        ),
        BottomNavItem(
            name = R.string.nav_orders,
            route = "orders",
            icon = R.drawable.ic_orders
        )
    )

    var selectedItem by remember { mutableStateOf(0) }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.name)
                    )
                },
                label = { Text(text = stringResource(id = item.name)) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}
