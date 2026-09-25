package com.rubp.whattoeat.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

private val PurpleDarkScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)

private val PurpleLightScheme = lightColorScheme(
    primary = Purple40,
    secondary = lerp(Purple40, Color.White, 0.8f),
    tertiary = lerp(Purple40, Color.White, 0.3f),
    onSecondary = Color.Black
)

private val BlueLightScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = lerp(BluePrimary, Color.White, 0.8f),
    tertiary = lerp(BluePrimary, Color.White, 0.3f),
    onSecondary = Color.Black
)

private val YellowLightScheme = lightColorScheme(
    primary = YellowPrimary,
    secondary = lerp(YellowPrimary, Color.White, 0.8f),
    tertiary = lerp(YellowPrimary, Color.White, 0.3f),
    onSecondary = Color.Black
)

private val GreenLightScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = lerp(GreenPrimary, Color.White, 0.8f),
    tertiary = lerp(GreenPrimary, Color.White, 0.3f),
    onSecondary = Color.Black
)

private val PinkLightScheme = lightColorScheme(
    primary = PinkPrimary,
    secondary = lerp(PinkPrimary, Color.White, 0.8f),
    tertiary = lerp(PinkPrimary, Color.White, 0.3f),
    onSecondary = Color.Black
)


enum class ColorTheme{
    Blue,
    Yellow,
    Green,
    Pink,
    Purple;

    fun toColorScheme(darkTheme: Boolean): ColorScheme {
        return when (this) {
            Blue -> BlueLightScheme
            Yellow -> YellowLightScheme
            Green -> GreenLightScheme
            Pink -> PinkLightScheme
            Purple -> if (darkTheme) PurpleDarkScheme else PurpleLightScheme
        }
    }

}



@Composable
fun WhatToEatTheme(
    colorTheme: ColorTheme,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = colorTheme.toColorScheme(darkTheme)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}