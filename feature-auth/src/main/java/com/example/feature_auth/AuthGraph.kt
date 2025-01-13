package com.example.feature_auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.core.navigation.BaseNavigator
import com.example.feature_auth.phonenumber.PhoneNumberScreen

fun NavGraphBuilder.registerAuthGraph(
    navigator: BaseNavigator
) {

    navigation(
        startDestination = FeatureAuthRoute.EnterPhoneNumber.INSTANCE.routeName,
        route = FeatureAuthRoute.GRAPH_NAME
    ) {

        composable(
            route = FeatureAuthRoute.EnterPhoneNumber.INSTANCE.routeName,
            arguments = FeatureAuthRoute.EnterPhoneNumber.INSTANCE.argsName
        ) {
            PhoneNumberScreen()
        }
    }
}