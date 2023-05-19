package com.studhub.app.presentation.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.studhub.app.R
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.presentation.ui.theme.StudHubTheme

data class Route(val name: String, val destination: String, val icon: ImageVector)

@Composable
fun NavBar(navController: NavHostController = rememberNavController()) {
    val currBackStackEntry = navController.currentBackStackEntryAsState()

    //each route will be used for a navbar button
    val items = listOf(
        Route(stringResource(R.string.nav_home_button), "Home", Icons.Filled.Home),
        Route(stringResource(R.string.nav_browse_button), "Listing", Icons.Filled.Search),
        Route(stringResource(R.string.nav_sell_button), "Listing/Add", Icons.Filled.AddCircle),
        Route(stringResource(R.string.nav_chat_button), "Conversations", Icons.Filled.MailOutline),
        Route(stringResource(R.string.nav_profile_button), "Profile", Icons.Filled.AccountBox)
    )

    // fill nav bar by creating a NavigationBarItem for each of the routes in the list
    NavigationBar {
        items.forEach { route ->
            NavigationBarItem(
                //tag to make testing easier
                modifier = Modifier.testTag("NavBar${route.name}"),
                icon = { Icon(route.icon, contentDescription = route.name) },
                label = { Text(route.name) },
                //selected based on checkDest return value to see if to which current view belongs
                selected = checkDest(currBackStackEntry.value?.destination?.route, route.destination),
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    indicatorColor = MaterialTheme.colorScheme.secondary
                ),
                onClick = {
                    navController.navigate(route.destination)
                }
            )
        }
    }
}

/**
 * Used to check if the child route is a child of the parent route (or the parent itself)
 *
 * @param child the route to be compared with the parent one, it will be split to get the topmost
 * parent name
 * @param parent the route that will be compared with the child, should only be a parent page of the
 * navigation
 * @return True if child is indeed a sub route of the parent route, else false.
 */
fun checkDest(child: String?, parent: String) : Boolean {
    if (child != null) {
        val extractedParent = child.split("/")[0]
        return extractedParent == parent
    }
    return false
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun NavBarPreview() {
    StudHubTheme() {
        NavBar()
    }
}
