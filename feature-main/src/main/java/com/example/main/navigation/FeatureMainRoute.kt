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

    class NoteCategories : FeatureMainRoute {

        companion object {
            val INSTANCE = NoteCategories()
        }

        override val routeName: String
            get() = "notesCategories"

        override val argsName: List<NamedNavArgument>
            get() = emptyList()

        override var routeNameWithArgs: String = routeName
    }


    object Notes : NavigationRoute {
        override val routeName = "notes"
        override val argsName = emptyList<NamedNavArgument>()
        override var routeNameWithArgs = routeName
    }

    object Profile : NavigationRoute {
        override val routeName = "profile"
        override val argsName = emptyList<NamedNavArgument>()
        override var routeNameWithArgs = routeName
    }
}