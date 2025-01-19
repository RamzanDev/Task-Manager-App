package com.example.feature_auth.navigation

import android.app.Activity
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.core.navigation.BaseNavigator
import com.example.feature_auth.screen.phonenumber.OTPInputField
import com.example.feature_auth.screen.phonenumber.PhoneNumberScreen

fun NavGraphBuilder.registerAuthGraph(
    navigator: BaseNavigator,
    activity: Activity
) {

    navigation(
        startDestination = FeatureAuthRoute.EnterPhoneNumber.INSTANCE.routeName,
        route = FeatureAuthRoute.GRAPH_NAME
    ) {

        composable(
            route = FeatureAuthRoute.EnterPhoneNumber.INSTANCE.routeName,
            arguments = FeatureAuthRoute.EnterPhoneNumber.INSTANCE.argsName
        ) {
            PhoneNumberScreen(navigator, activity = activity)
        }
        composable(
            route = FeatureAuthRoute.Otp.INSTANCE.routeName,
            arguments = FeatureAuthRoute.Otp.INSTANCE.argsName
        ) {
            OTPInputField(navigator)
        }
    }
}