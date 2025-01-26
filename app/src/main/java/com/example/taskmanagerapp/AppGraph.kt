package com.example.taskmanagerapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.example.core.keylock.KeylockManager
import com.example.feature_auth.navigation.FeatureAuthRoute
import com.example.feature_auth.navigation.registerAuthGraph
import com.example.main.navigation.registerMainGraph

@Composable
fun AppGraph(
    navigator: AppNavigator,
    keyLockManager: KeylockManager
) {

    NavHost(
        navController = navigator.navController,
        startDestination = FeatureAuthRoute.GRAPH_NAME
    ) {
        registerAuthGraph(navigator)
        registerMainGraph(navigator)
    }
}