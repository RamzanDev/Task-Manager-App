package com.example.main.navigation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Note
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material.icons.outlined.Notes
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.core.navigation.NavigationRoute
import com.example.main.navigation.FeatureMainRoute

sealed class NavigationItem(
    val titleResId: Int,
    val icon: ImageVector,
    val route: NavigationRoute
) {

    data object Home: NavigationItem(
        titleResId = com.example.locale.R.string.navigation_item_main,
        icon = Icons.Outlined.Home,
        route = FeatureMainRoute.Main.INSTANCE
    )

    data object Note: NavigationItem(
        titleResId = com.example.locale.R.string.navigation_item_note,
        icon = Icons.Outlined.NoteAlt,
        route = FeatureMainRoute.Notes
    )

    data object Profile: NavigationItem(
        titleResId = com.example.locale.R.string.navigation_item_profile,
        icon = Icons.Filled.Person,
        route = FeatureMainRoute.Profile
    )
}