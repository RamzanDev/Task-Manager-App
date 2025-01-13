package com.example.core.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavOptions


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

