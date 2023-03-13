package com.studhub.app.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(

    background = Color(0XFF1C1B1F),
    onBackground = Color(0XFFE6E1E5),
    surface = Color(0XFF1C1B1F) ,
    onSurface = Color(0XFFE6E1E5),
    primary = Color(0XFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xffEADDFF),
    secondary = Color(0XFFCCC2DC) ,
    onSecondary = Color(0xFF332D41) ,
    secondaryContainer = Color (0XFF4A4458),
    onSecondaryContainer= Color(0xFFE8DEF8),
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0Xff492532),
    tertiaryContainer = Color(0XFF633B48),
    onTertiaryContainer = Color(0XFFFFD8E4),
)


private val LightColorPalette = lightColorScheme(
    background = Color(0xffFFFBFE),
    onBackground = Color(0xff1C1B1F),
    surface = Color(0xffFFFBFE) ,
    onSurface = Color(0xff1C1B1F),
    primary = Color(0xff6750A4),
    onPrimary = Color(0xffFFFFFF),
    primaryContainer = Color(0xffEADDFF),
    onPrimaryContainer = Color(0xff21005D),
    secondary = Color(0xff625B71) ,
    onSecondary = Color(0xffFFFFFF) ,
    secondaryContainer = Color (0xff7D5260),
    onSecondaryContainer= Color(0xff7D5260),
    tertiary = Color(0xff7D5260),
    onTertiary = Color(0xffFFFFFF),
    tertiaryContainer = Color(0xffFFD8E4),
    onTertiaryContainer = Color(0xff31111D)
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
