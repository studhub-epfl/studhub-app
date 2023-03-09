package com.studhub.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = StudHubPurple,
    primaryVariant = StudHubPurple,
    secondaryVariant = StudHubPurple,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = StudHubPurple,
    primaryVariant = StudHubPurple,
    secondary = Teal200 ,
    secondaryVariant = StudHubPurple,
    background = StudHubWhite,
    surface = StudHubWhite,
    onPrimary = StudHubWhite,
    onSecondary = StudHubWhite,
    onBackground = StudHubWhite,
    onSurface = StudHubWhite,
)

@Composable
fun StudHubTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
