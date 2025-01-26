package com.example.feature_auth.navigation

import androidx.navigation.NamedNavArgument
import com.example.core.navigation.NavigationRoute

interface FeatureAuthRoute : NavigationRoute {

    companion object {
        const val GRAPH_NAME = "feature_auth"
    }

    class EnterEmail : FeatureAuthRoute {

        companion object {
            val INSTANCE = EnterEmail()
        }

        override val routeName: String
            get() = "login"
        override val argsName: List<NamedNavArgument>
            get() = emptyList()
        override var routeNameWithArgs: String = routeName
    }

    class Registration : FeatureAuthRoute {

        companion object {
            val INSTANCE = Registration()
        }

        override val routeName: String
            get() = "registration"
        override val argsName: List<NamedNavArgument>
            get() = emptyList()
        override var routeNameWithArgs: String = routeName
    }
}