package com.example.uikit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uikit.theme.ColorTheme

@Preview
@Composable
fun IndeterminateCircularIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier
            .width(24.dp)
            .height(24.dp),
        color = ColorTheme.brandPrimary,
        trackColor = ColorTheme.staticInverse,
    )
}

@Composable
fun FullScreenProgressBar(isVisible: Boolean = true) {
    if (!isVisible) {
        return
    }
    Box(modifier = Modifier.fillMaxSize().background(ColorTheme.inverse)) {
        IndeterminateCircularIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun SmallProgressBar(isVisible: Boolean = true) {
    if (!isVisible) {
        return
    }
    Box(modifier = Modifier.size(32.dp).background(ColorTheme.inverse)) {
        IndeterminateCircularIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
