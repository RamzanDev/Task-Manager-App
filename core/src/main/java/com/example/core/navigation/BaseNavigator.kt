package com.example.core.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.navArgument


interface BaseNavigator {
    fun navigate(route: NavigationRoute, navOptions: NavOptions? = null)
    fun navigate(route: String, navOptions: NavOptions? = null)
    fun popBackStack()
    fun getCurrentDestination(): String
    fun getArg(key: String): String
}

interface NavigationRoute {
    val routeName: String
    val argsName: List<NamedNavArgument>
    var routeNameWithArgs: String
}

object MockNavigator : BaseNavigator {
    override fun navigate(route: NavigationRoute, navOptions: NavOptions?) = Unit
    override fun popBackStack() = Unit
    override fun getCurrentDestination(): String = "stub"
    override fun getArg(key: String): String = ""
    override fun navigate(route: String, navOptions: NavOptions?) = Unit
}

fun stringArgument(
    name: String,
): NamedNavArgument {
    return navArgument(name) {
        type = NavType.StringType
    }
}

