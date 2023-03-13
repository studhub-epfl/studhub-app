package com.studhub.app.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(

    background = stud_hub_black ,
    onBackground = stud_hub_white ,
    surface = stud_hub_black,
    onSurface = stud_hub_white,
    primary = stud_hub_light_pink,
    onPrimary = stud_hub_dark_blue,
    primaryContainer = stud_hub_violet,
    onPrimaryContainer = stud_hub_white_variant ,
    secondary = stud_hub_pink_variant,
    onSecondary = stud_hub_dark_violet ,
    secondaryContainer = stud_hub_dark_gray,
    onSecondaryContainer= stud_hub_white_variant_2,
    tertiary = stud_hub_pink,
    onTertiary = stud_hub_burgundy,
    tertiaryContainer = stud_hub_pink_brown,
    onTertiaryContainer = stud_hub_pink_variant_2
)


private val LightColorPalette = lightColorScheme(
    background = stud_hub_white_v ,
    onBackground = stud_hub_black,
    surface = stud_hub_white_v ,
    onSurface = stud_hub_black,
    primary = stud_hub_violet_v,
    onPrimary = stud_hub_pure_white,
    primaryContainer = stud_hub_white_variant,
    onPrimaryContainer = stud_hub_flashy_blue,
    secondary = stud_hub_light_gray_v ,
    onSecondary = stud_hub_pure_white ,
    secondaryContainer =  stud_hub_burgundy_v,
    onSecondaryContainer= stud_hub_burgundy_v,
    tertiary = stud_hub_burgundy_v,
    onTertiary = stud_hub_burgundy_v,
    tertiaryContainer = stud_hub_pink_variant_2,
    onTertiaryContainer = stud_hub_dark_burgundy
)



@Composable
fun StudHubTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorsScheme = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
        colorScheme = colorsScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
