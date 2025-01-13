package com.example.taskmanagerapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.example.core.keylock.KeylockManager
import com.example.feature_auth.FeatureAuthRoute
import com.example.feature_auth.registerAuthGraph

@Composable
fun AppGraph(
    navigator: AppNavigator,
    keyLockManager: KeylockManager
) {
    
    NavHost(navController = navigator.navController, FeatureAuthRoute.GRAPH_NAME) {
        registerAuthGraph(navigator)
    }
}