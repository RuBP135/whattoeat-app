package com.rubp.whattoeat.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rubp.whattoeat.app.di.AppContainer
import com.rubp.whattoeat.app.navigation.MainScreen
import com.rubp.whattoeat.core.theme.ColorTheme
import com.rubp.whattoeat.core.theme.WhatToEatTheme
import com.rubp.whattoeat.feature.settings.viewmodel.SettingsViewModel

@Composable
fun App(appContainer: AppContainer) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = appContainer.appViewModelFactory)

    val colorTheme: ColorTheme by settingsViewModel.colorThemeStateFlow.collectAsState()

    WhatToEatTheme(colorTheme = colorTheme, darkTheme = isSystemInDarkTheme()) {
        MainScreen(
            appContainer.appViewModelFactory,
            settingsViewModel
        )
    }
}