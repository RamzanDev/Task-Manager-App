package com.example.feature_auth.navigation

import androidx.navigation.NamedNavArgument
import com.example.core.navigation.NavigationRoute
import com.example.core.navigation.stringArgument
import com.example.core.util.insertArgs

interface FeatureAuthRoute : NavigationRoute {

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

    class Otp : FeatureAuthRoute {
        companion object {
            val INSTANCE = Otp()

            fun create(phoneNumber: String): Otp {
                return Otp().apply {
                    routeNameWithArgs = routeName.insertArgs("phoneNumber", phoneNumber)
                }
            }
        }

        override val routeName: String
            get() = "login/{phoneNumber}"

        override val argsName: List<NamedNavArgument>
            get() = listOf(stringArgument("phoneNumber"))

        override var routeNameWithArgs: String = routeName

    }
}