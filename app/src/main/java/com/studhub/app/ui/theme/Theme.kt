package com.studhub.app.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = StudHubDarkGray,
    primaryVariant = StudHubDarkerGray,
    secondary = StudHubLightPink ,
    secondaryVariant = StudHubBrightPink,
    background = StudHubDarkerGray,
    surface = StudHubMediumGray,
    onPrimary = StudHubWhite,
    onSecondary = StudHubBlack,
    onBackground = StudHubWhite,
    onSurface = StudHubWhite,
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = StudHubWhite,
    primaryVariant = StudHubLightGray,
    secondary = SutdHubBrightOrange ,
    secondaryVariant = StudHubSoftOrange,
    background = StudHubGray,
    surface = StudHubWhite,
    onPrimary = StudHubDarkGray,
    onSecondary = StudHubWhite,
    onBackground = StudHubDarkGray,
    onSurface = StudHubDarkGray,
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
