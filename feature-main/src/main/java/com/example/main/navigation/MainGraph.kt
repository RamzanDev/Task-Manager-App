package com.example.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.core.navigation.BaseNavigator
import com.example.domain.model.User
import com.example.main.MainScreen
import com.example.main.NotesScreen
import com.example.main.ProfileScreen
import com.example.main.categories.NoteCategoriesScreen

fun NavGraphBuilder.registerMainGraph(
    navigator: BaseNavigator
) {
    navigation(
        startDestination = FeatureMainRoute.Main.INSTANCE.routeName,
        route = FeatureMainRoute.GRAPH_NAME
    ) {
        composable(
            route = FeatureMainRoute.Main.INSTANCE.routeName,
            arguments = FeatureMainRoute.Main.INSTANCE.argsName
        ) {
            MainScreen(navigator)
        }
        composable(
            route = FeatureMainRoute.NoteCategories.INSTANCE.routeName,
            arguments = FeatureMainRoute.NoteCategories.INSTANCE.argsName
        ) {
            NoteCategoriesScreen(navigator)
        }

//        composable(
//            FeatureMainRoute.Notes.routeName
//        ) {
//            NotesScreen(navigator)
//        }
//        composable(
//            FeatureMainRoute.Profile.routeName
//        ) {
//            ProfileScreen(User(
//                id = "110495319787127627089",
//                email = "khasanovramzan1995@gmail.com",
//                name = "Рамзан Хасанов",
//                photoUrl = "https://lh3.googleusercontent.com/a/ACg8ocJGRe80CEXNZ19sifflHTFJd7sbciX1Xj-UXEgH36m6PIouW5U=s96-c"
//            ),navigator)
//        }
    }
}