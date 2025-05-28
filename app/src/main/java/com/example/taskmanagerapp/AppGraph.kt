package com.example.taskmanagerapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.example.core.keylock.KeylockManager
import com.example.domain.UserStorageContract
import com.example.feature_auth.navigation.FeatureAuthRoute
import com.example.feature_auth.navigation.registerAuthGraph
import com.example.main.navigation.FeatureMainRoute
import com.example.main.navigation.registerMainGraph
import timber.log.Timber

@Composable
fun AppGraph(
    navigator: AppNavigator,
    keyLockManager: KeylockManager,
    userStorageContract: UserStorageContract
) {

    val token = userStorageContract.getString(UserStorageContract.Key.KEY_ACCESS_TOKEN)

    // Начинаем с экрана, в зависимости от наличия токена
    Timber.tag("AppGraph").i("$token")
    val startDestination = if (token?.isNotEmpty() == true) {
        FeatureMainRoute.GRAPH_NAME  // Если токен есть, направляем на главный экран
    } else {
        FeatureAuthRoute.GRAPH_NAME  // Если токен отсутствует, направляем на авторизацию
    }
    Timber.tag("AppGraph").i("$startDestination")

    NavHost(
        navController = navigator.navController,
        startDestination = startDestination
    ) {
        registerAuthGraph(navigator)
        registerMainGraph(navigator)
    }
}