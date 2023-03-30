package com.studhub.app.presentation.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.studhub.app.R
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage

data class Route(val name: String, val destination: String, val icon: ImageVector)

@Composable
fun NavBar(navController: NavHostController = rememberNavController()) {
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf(
        Route(stringResource(R.string.nav_home_button), "Home", Icons.Filled.Home),
        Route(stringResource(R.string.nav_browse_button), "Browse",  Icons.Filled.Search),
        Route(stringResource(R.string.nav_sell_button), "AddListing",  Icons.Filled.AddCircle),
        Route(stringResource(R.string.nav_cart_button), "Cart",  Icons.Filled.ShoppingCart),
        Route(stringResource(R.string.nav_profile_button), "Profile",  Icons.Filled.AccountBox))

    NavigationBar (modifier = Modifier.testTag("NavBar")) {
        items.forEachIndexed  { index, route ->
            NavigationBarItem(
                icon = { Icon(route.icon, contentDescription = route.name) },
                label = { Text(route.name) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(route.destination)
                }
            )
        }
    }
}
@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun NavBarPreview() {
    NavBar()
}
