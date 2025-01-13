package com.example.feature_auth

import androidx.navigation.NamedNavArgument
import com.example.core.navigation.NavigationRoute

interface FeatureAuthRoute: NavigationRoute {

    companion object {
        const val GRAPH_NAME = "feature_auth"
    }

    class EnterPhoneNumber : FeatureAuthRoute {

        companion object {
            val INSTANCE = EnterPhoneNumber()
        }

        override val routeName: String
            get() = "login"
        override val argsName: List<NamedNavArgument>
            get() = emptyList()
        override var routeNameWithArgs: String = routeName
    }
}