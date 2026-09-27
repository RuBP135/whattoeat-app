package com.rubp.whattoeat.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = WteOrange,
    onPrimary = WteCocoa,
    primaryContainer = WtePeach,
    onPrimaryContainer = WteCocoa,
    inversePrimary = WteOrangeSoft,
    secondary = WteCaramel,
    onSecondary = WteCream,
    secondaryContainer = WteLightSurfaceVariant,
    onSecondaryContainer = WteCocoa,
    tertiary = WteApricot,
    onTertiary = WteCocoa,
    tertiaryContainer = WteApricot,
    onTertiaryContainer = WteCocoa,
    background = WteLightBackground,
    onBackground = WteCocoa,
    surface = WteCream,
    onSurface = WteCocoa,
    surfaceVariant = WteLightSurfaceVariant,
    onSurfaceVariant = WteCaramel,
    surfaceTint = WteOrange,
    inverseSurface = WteCocoa,
    inverseOnSurface = WteCream,
    outline = WteCaramel,
    outlineVariant = WteMutedPeach,
    scrim = Color.Black,
    surfaceBright = WteCream,
    surfaceDim = WteMutedPeach,
    surfaceContainerLowest = WteCream,
    surfaceContainerLow = WteCream,
    surfaceContainer = WteLightBackground,
    surfaceContainerHigh = WteLightSurfaceVariant,
    surfaceContainerHighest = WtePeach,
    primaryFixed = WtePeach,
    primaryFixedDim = WteOrangeSoft,
    onPrimaryFixed = WteCocoa,
    onPrimaryFixedVariant = WteCaramel,
    secondaryFixed = WteLightSurfaceVariant,
    secondaryFixedDim = WteMutedPeach,
    onSecondaryFixed = WteCocoa,
    onSecondaryFixedVariant = WteCaramel,
    tertiaryFixed = WteApricot,
    tertiaryFixedDim = WteApricot,
    onTertiaryFixed = WteCocoa,
    onTertiaryFixedVariant = WteCaramel
)

private val DarkColorScheme = darkColorScheme(
    primary = WteOrangeSoft,
    onPrimary = WteCocoa,
    primaryContainer = WteDarkPrimaryContainer,
    onPrimaryContainer = WteCream,
    inversePrimary = WteOrange,
    secondary = WteDarkTextMuted,
    onSecondary = WteCocoa,
    secondaryContainer = WteDarkSurfaceHigh,
    onSecondaryContainer = WteDarkText,
    tertiary = WteApricot,
    onTertiary = WteCocoa,
    tertiaryContainer = WteCaramel,
    onTertiaryContainer = WteCream,
    background = WteDarkBackground,
    onBackground = WteDarkText,
    surface = WteDarkSurface,
    onSurface = WteDarkText,
    surfaceVariant = WteDarkSurfaceHigh,
    onSurfaceVariant = WteDarkTextMuted,
    surfaceTint = WteOrangeSoft,
    inverseSurface = WteCream,
    inverseOnSurface = WteCocoa,
    outline = WteDarkOutline,
    outlineVariant = WteCaramel,
    scrim = Color.Black,
    surfaceBright = WteDarkSurfaceHigh,
    surfaceDim = WteDarkBackground,
    surfaceContainerLowest = WteDarkBackground,
    surfaceContainerLow = WteDarkSurface,
    surfaceContainer = WteDarkSurface,
    surfaceContainerHigh = WteDarkSurfaceHigh,
    surfaceContainerHighest = WteDarkSurfaceHigh,
    primaryFixed = WtePeach,
    primaryFixedDim = WteOrangeSoft,
    onPrimaryFixed = WteCocoa,
    onPrimaryFixedVariant = WteCaramel,
    secondaryFixed = WteLightSurfaceVariant,
    secondaryFixedDim = WteMutedPeach,
    onSecondaryFixed = WteCocoa,
    onSecondaryFixedVariant = WteCaramel,
    tertiaryFixed = WteApricot,
    tertiaryFixedDim = WteApricot,
    onTertiaryFixed = WteCocoa,
    onTertiaryFixedVariant = WteCaramel
)

enum class ThemeMode {
    System,
    Light,
    Dark;

    fun isDark(systemInDarkTheme: Boolean): Boolean = when (this) {
        System -> systemInDarkTheme
        Light -> false
        Dark -> true
    }
}

@Immutable
data class WteExtendedColors(
    val brandBackdrop: Color,
    val paper: Color,
    val paperBorder: Color,
    val paperInnerLine: Color,
    val patternLine: Color,
    val offsetShadow: Color,
    val decorativeCoral: Color
)

private val LightExtendedColors = WteExtendedColors(
    brandBackdrop = WtePeach,
    paper = WteCream,
    paperBorder = WteCaramel.copy(alpha = 0.55f),
    paperInnerLine = WteMutedPeach,
    patternLine = WteMutedPeach,
    offsetShadow = WteCaramel,
    decorativeCoral = WteCoral
)

private val DarkExtendedColors = WteExtendedColors(
    brandBackdrop = WteDarkSurfaceHigh,
    paper = WteDarkSurface,
    paperBorder = WteDarkOutline.copy(alpha = 0.55f),
    paperInnerLine = WteCaramel,
    patternLine = WteCaramel,
    offsetShadow = WteDarkShadow,
    decorativeCoral = WteOrangeSoft
)

private val LocalWteExtendedColors = staticCompositionLocalOf { LightExtendedColors }

object WteTheme {
    val extendedColors: WteExtendedColors
        @Composable get() = LocalWteExtendedColors.current
}

@Composable
fun WhatToEatTheme(
    themeMode: ThemeMode = ThemeMode.System,
    systemInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val isDarkTheme = themeMode.isDark(systemInDarkTheme)
    val colorScheme: ColorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (isDarkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(LocalWteExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

@Composable
fun WhatToEatPreviewTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    WhatToEatTheme(
        themeMode = if(darkTheme) ThemeMode.Dark else ThemeMode.Light,
        systemInDarkTheme = false,
        content = content
    )
}
