package com.example.taskmanagerapp

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.example.core.keylock.KeylockManager
import com.example.feature_auth.navigation.FeatureAuthRoute
import com.example.feature_auth.navigation.registerAuthGraph

@Composable
fun AppGraph(
    navigator: AppNavigator,
    keyLockManager: KeylockManager,
    activity: Activity
) {

    NavHost(navController = navigator.navController, FeatureAuthRoute.GRAPH_NAME) {
        registerAuthGraph(navigator, activity)
    }
}