package com.example.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.core.navigation.BaseNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(navigator: BaseNavigator) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Notes") })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("This is the Notes screen")
        }
    }
}