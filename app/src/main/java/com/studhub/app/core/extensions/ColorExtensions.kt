package com.studhub.app.core.extensions

import android.graphics.Color.HSVToColor
import android.graphics.Color.colorToHSV
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun Color.darken(factor: Float): Color {
    // Make sure the factor is within the valid range of 0 to 1
    val safeFactor = factor.coerceIn(0f, 1f)

    // Convert the color to HSB
    val hsb = FloatArray(3)
    val colorInt = this.toArgb()
    colorToHSV(colorInt, hsb)

    // Decrease the brightness (B in HSB) by the given factor
    hsb[2] = hsb[2] * (1 - safeFactor)

    // Convert the HSB color back to an ARGB color
    val darkenedColorInt = HSVToColor(hsb)
    return Color(darkenedColorInt)
}
