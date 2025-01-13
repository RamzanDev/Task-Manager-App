package com.example.taskmanagerapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.example.core.navigation.BaseNavigator
import com.example.core.navigation.NavigationRoute
import timber.log.Timber

@Composable
fun rememberAppNavigator(
    navController: NavHostController = rememberNavController()
): AppNavigator = remember(navController) {
    AppNavigator(navController)
}

class AppNavigator(val navController: NavHostController) : BaseNavigator {

    override fun navigate(route: NavigationRoute, navOptions: NavOptions?) {
        navController.navigate(route.routeNameWithArgs)
        val currentDest = navController.currentBackStackEntry
        val navRoute = currentDest?.destination?.route

        val arguments = route.argsName.joinToString("\n") {
            it.name + " = " + currentDest?.arguments?.getString(it.name)
        }
        Timber.tag("Navigator").d("New destination = $navRoute\nArguments:\n$arguments")
    }

    override fun navigate(route: String, navOptions: NavOptions?) {
        navController.navigate(route, navOptions)
    }

    override fun popBackStack() {
        navController.popBackStack()
    }

    override fun getCurrentDestination(): String {
        return navController.currentBackStackEntry?.destination.toString()
    }

    override fun getArg(key: String): String {
        return navController.currentBackStackEntry?.arguments?.getString(key).orEmpty()
    }
}