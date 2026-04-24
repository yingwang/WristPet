package com.wristpet.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

private val WristPetColors = Colors(
    primary = Color(0xFF66BB6A),
    primaryVariant = Color(0xFF338A3E),
    secondary = Color(0xFFFFCA28),
    background = Color(0xFF0D0D0D),
    surface = Color(0xFF1A1A2E),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    error = Color(0xFFEF5350),
    onError = Color.White,
)

@Composable
fun WristPetTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = WristPetColors,
        content = content,
    )
}
