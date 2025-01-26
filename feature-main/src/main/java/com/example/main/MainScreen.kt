package com.example.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.core.navigation.BaseNavigator
import com.example.core.navigation.MockNavigator

@Composable
fun MainScreen(
    navigator: BaseNavigator = MockNavigator
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "It is main screen, welcome!"
        )
    }

}