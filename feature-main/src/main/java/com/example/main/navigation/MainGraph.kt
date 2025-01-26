package com.example.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.core.navigation.BaseNavigator
import com.example.main.MainScreen

fun NavGraphBuilder.registerMainGraph(
    navigator: BaseNavigator
) {
    navigation(
        startDestination = FeatureMainRoute.Main.INSTANCE.routeName,
        route = FeatureMainRoute.GRAPH_NAME
    ) {
        composable(
            route = FeatureMainRoute.Main.INSTANCE.routeName,
            arguments = FeatureMainRoute.Main.INSTANCE.argsName
        ) {
            MainScreen(navigator)
        }
    }
}