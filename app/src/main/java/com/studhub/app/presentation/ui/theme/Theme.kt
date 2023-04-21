package com.studhub.app.presentation.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    primaryContainer = studhub_pastel_red,
    onPrimaryContainer = studhub_pure_white,
    secondary = studhub_crimson_red,
    onSecondary = studhub_pure_white,
    secondaryContainer = studhub_dark_grey_pink,
    onSecondaryContainer = studhub_pure_white,
    tertiary = studhub_pastel_red,
    onTertiary = studhub_pure_black,
    tertiaryContainer = studhub_gray,
    onTertiaryContainer = studhub_pure_white
)


private val LightColorPalette = lightColorScheme(
    background = studhub_off_white,
    onBackground = studhub_off_black,
    //E.g Navbar background
    surface = studhub_washed_crimson_red,
    onSurface = studhub_pure_black,
    primary = studhub_bright_red,
    onPrimary = studhub_pure_white,
    primaryContainer = studhub_pastel_red,
    onPrimaryContainer = studhub_pure_white,
    secondary = studhub_crimson_red,
    onSecondary = studhub_pure_white,
    secondaryContainer = studhub_light_grey_pink,
    onSecondaryContainer = studhub_pure_white,
    tertiary = studhub_pastel_red,
    onTertiary = studhub_pure_black,
    tertiaryContainer = studhub_gray,
    onTertiaryContainer = studhub_pure_white
)

@ExcludeFromGeneratedTestCoverage
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

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun LightThemePreview() {
    StudHubTheme {
        ThemePreviewContent()
    }
}

@ExcludeFromGeneratedTestCoverage
@Preview(showBackground = true)
@Composable
fun DarkThemePreview() {
    StudHubTheme (darkTheme = true) {
        ThemePreviewContent()
    }
}

@ExcludeFromGeneratedTestCoverage
@Composable
fun SamplePrimaryContainer() {
    Card (
        modifier = Modifier
            .width(180.dp)
            .height(70.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ))
    {
        Box (
            modifier = Modifier.padding(5.dp))
        {
            Text("Primary Container")
        }
    }
}

@ExcludeFromGeneratedTestCoverage
@Composable
fun SampleSecondaryContainer() {
    Card (
        modifier = Modifier
            .width(180.dp)
            .height(70.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ))
    {
        Box (
            modifier = Modifier.padding(5.dp))
        {
            Text("Secondary Container")
        }
    }
}

@ExcludeFromGeneratedTestCoverage
@Composable
fun SampleTertiaryContainer() {
    Card (
        modifier = Modifier
            .width(180.dp)
            .height(70.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ))
    {
        Box (
            modifier = Modifier.padding(5.dp))
        {
            Text("Tertiary Container")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@ExcludeFromGeneratedTestCoverage
@Composable
fun ThemePreviewContent() {
    Scaffold(
        bottomBar = {
            NavBar()
        },
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column (modifier = Modifier.padding(5.dp)){
                    Box {
                        BigLabel("Big Label")
                    }
                    BasicFilledButton(onClick = { }, label = "Basic Filled Button")
                    Text(text = "this is normal text",
                        fontSize = 20.sp)
                    Text(text = "Primary color",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp)
                    Text(text = "Secondary color",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 20.sp)
                    Text(text = "Tertiary color",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    SamplePrimaryContainer()
                    Spacer(modifier = Modifier.height(10.dp))
                    SampleSecondaryContainer()
                    Spacer(modifier = Modifier.height(10.dp))
                    SampleTertiaryContainer()
                }
            }
        })
}
