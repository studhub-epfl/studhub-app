package com.studhub.app.presentation.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studhub.app.annotations.ExcludeFromGeneratedTestCoverage
import com.studhub.app.presentation.nav.NavBar
import com.studhub.app.presentation.ui.common.button.BasicFilledButton
import com.studhub.app.presentation.ui.common.text.BigLabel

private val DarkColorPalette = darkColorScheme(

    background = studhub_dark_grey,
    onBackground = studhub_off_white,
    //E.g Navbar background
    surface = studhub_washed_crimson_red,
    onSurface = studhub_pure_black,
    primary = studhub_bright_red,
    onPrimary = studhub_pure_white,
    primaryContainer = studhub_dark_grey_pink,
    onPrimaryContainer = studhub_pure_white,
    secondary = studhub_crimson_red,
    onSecondary = studhub_pure_white,
    secondaryContainer = studhub_pastel_red,
    onSecondaryContainer = studhub_pure_white,
    tertiary = studhub_pastel_red,
    onTertiary = studhub_pure_black,
    tertiaryContainer = studhub_gray,
    onTertiaryContainer = studhub_pure_black
)


private val LightColorPalette = lightColorScheme(
    background = studhub_off_white,
    onBackground = studhub_off_black,
    //E.g Navbar background
    surface = studhub_washed_crimson_red,
    onSurface = studhub_pure_black,
    primary = studhub_bright_red,
    onPrimary = studhub_pure_white,
    primaryContainer = studhub_light_grey_pink,
    onPrimaryContainer = studhub_pure_white,
    secondary = studhub_crimson_red,
    onSecondary = studhub_pure_white,
    secondaryContainer = studhub_pastel_red,
    onSecondaryContainer = studhub_pure_white,
    tertiary = studhub_pastel_red,
    onTertiary = studhub_pure_black,
    tertiaryContainer = studhub_gray,
    onTertiaryContainer = studhub_pure_black
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun ThemePreview() {
    StudHubTheme {
        Scaffold(
            bottomBar = {
                NavBar()
            },
            content = {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ThemePreviewContent()
                }
            })
    }
}

@ExcludeFromGeneratedTestCoverage
@Composable
fun ThemePreviewContent() {
    Column (modifier = Modifier.padding(5.dp)){
        Box {
            BigLabel("Big Label")
        }
        BasicFilledButton(onClick = { }, label = "Basic Filled Button")
        Text(text = "this is normal text")
    }
}
