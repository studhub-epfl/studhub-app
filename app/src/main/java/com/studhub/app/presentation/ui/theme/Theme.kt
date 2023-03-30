package com.studhub.app.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(

    background = studhub_black,
    onBackground = studhub_white,
    surface = studhub_black,
    onSurface = studhub_white,
    primary = studhub_light_pink,
    onPrimary = studhub_dark_blue,
    primaryContainer = studhub_violet,
    onPrimaryContainer = studhub_white_variant,
    secondary = studhub_pink_variant,
    onSecondary = studhub_dark_violet,
    secondaryContainer = studhub_dark_gray,
    onSecondaryContainer = studhub_white_variant_2,
    tertiary = studhub_pink,
    onTertiary = studhub_burgundy,
    tertiaryContainer = studhub_pink_brown,
    onTertiaryContainer = studhub_pink_variant_2
)


private val LightColorPalette = lightColorScheme(
    background = studhub_white_v,
    onBackground = studhub_black,
    surface = studhub_white_v,
    onSurface = studhub_black,
    primary = studhub_violet_v,
    onPrimary = studhub_pure_white,
    primaryContainer = studhub_white_variant,
    onPrimaryContainer = studhub_flashy_blue,
    secondary = studhub_light_gray_v,
    onSecondary = studhub_pure_white,
    secondaryContainer = studhub_burgundy_v,
    onSecondaryContainer = studhub_white_variant,
    tertiary = studhub_burgundy_v,
    onTertiary = studhub_burgundy_v,
    tertiaryContainer = studhub_pink_variant_2,
    onTertiaryContainer = studhub_dark_burgundy
)


@Composable
fun StudHubTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorsScheme = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colorsScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
