package com.example.feature_auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.core.navigation.BaseNavigator
import com.example.feature_auth.screen.email.SignInWithEmailScreen
import com.example.feature_auth.screen.registration.RegistrationScreen

fun NavGraphBuilder.registerAuthGraph(
    navigator: BaseNavigator
) {

    navigation(
        startDestination = FeatureAuthRoute.EnterEmail.INSTANCE.routeName,
        route = FeatureAuthRoute.GRAPH_NAME
    ) {

        composable(
            route = FeatureAuthRoute.EnterEmail.INSTANCE.routeName,
            arguments = FeatureAuthRoute.EnterEmail.INSTANCE.argsName
        ) {
            SignInWithEmailScreen(navigator)
        }
        composable(
            route = FeatureAuthRoute.Registration.INSTANCE.routeName,
            arguments = FeatureAuthRoute.Registration.INSTANCE.argsName
        ) {
            RegistrationScreen(navigator)
        }
    }
}