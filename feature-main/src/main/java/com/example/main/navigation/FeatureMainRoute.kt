package com.example.main.navigation

import androidx.navigation.NamedNavArgument
import com.example.core.navigation.NavigationRoute

interface FeatureMainRoute : NavigationRoute {

    companion object {
        const val GRAPH_NAME = "feature_main"
    }

    class Main : FeatureMainRoute {
        companion object {
            val INSTANCE = Main()
        }

        override val routeName: String
            get() = "main"

        override val argsName: List<NamedNavArgument>
            get() = emptyList()

        override var routeNameWithArgs: String = routeName

    }
}