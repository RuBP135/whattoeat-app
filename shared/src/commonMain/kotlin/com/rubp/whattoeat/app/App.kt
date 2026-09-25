package com.rubp.whattoeat.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.rubp.whattoeat.feature.settings.data.preferences.ConfigRepository
import com.rubp.whattoeat.app.navigation.MainScreen
import com.rubp.whattoeat.core.theme.ColorTheme
import com.rubp.whattoeat.core.theme.WhatToEatTheme

@Composable
@Preview
fun App() {
    val colorTheme by ConfigRepository.colorThemeFlow.collectAsState(initial = ColorTheme.Pink)
    WhatToEatTheme(colorTheme = colorTheme, darkTheme = isSystemInDarkTheme()) {
        MainScreen()
    }
}