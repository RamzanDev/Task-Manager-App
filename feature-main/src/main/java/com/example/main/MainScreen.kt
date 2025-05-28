package com.example.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.navigation.BaseNavigator
import com.example.core.navigation.MockNavigator
import com.example.domain.model.User
import com.example.main.navigation.FeatureMainRoute
import com.example.main.navigation.model.NavigationItem
import com.example.uikit.theme.ColorTheme

@SuppressLint("UnrememberedMutableState")
@Composable
fun MainScreen(
    navigator: BaseNavigator = MockNavigator
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Scaffold(
        floatingActionButton = {
            if (currentRoute == FeatureMainRoute.Main.INSTANCE.routeName) { // Показываем FAB только на главном экране
                FloatingActionButton(
                    onClick = {
                        navigator.navigate(FeatureMainRoute.NoteCategories.INSTANCE.routeName)
                    }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = ColorTheme.inverse
            ) {

                val items = listOf(
                    NavigationItem.Home, NavigationItem.Note, NavigationItem.Profile
                )
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route.routeName,
                        onClick = {
                            if (currentRoute != item.route.routeName) {
                                navController.navigate(item.route.routeName) {
                                    popUpTo(FeatureMainRoute.Main.INSTANCE.routeName) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = {
                            Icon(item.icon, contentDescription = null)
                        },
                        label = {
                            Text(text = stringResource(item.titleResId))
                        }
                    )
                }
            }
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = FeatureMainRoute.Main.INSTANCE.routeName,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(FeatureMainRoute.Main.INSTANCE.routeName) {
                    Text("It is main screen, welcome!", modifier = Modifier.align(Alignment.Center))
                }
                composable(FeatureMainRoute.Notes.routeName) {
                    NotesScreen(navigator)
                }
                composable(FeatureMainRoute.Profile.routeName) {
                    ProfileScreen(
                        User(
                        id = "110495319787127627089",
                        email = "khasanovramzan1995@gmail.com",
                        name = "Рамзан Хасанов",
                        photoUrl = "https://lh3.googleusercontent.com/a/ACg8ocJGRe80CEXNZ19sifflHTFJd7sbciX1Xj-UXEgH36m6PIouW5U=s96-c"
                    ), navigator)
                }
            }
        }
    }

}