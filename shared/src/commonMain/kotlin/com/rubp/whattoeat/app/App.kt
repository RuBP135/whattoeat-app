package com.rubp.whattoeat.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rubp.whattoeat.app.di.AppContainer
import com.rubp.whattoeat.app.navigation.MainScreen
import com.rubp.whattoeat.core.i18n.applyAppLanguage
import com.rubp.whattoeat.core.theme.WhatToEatTheme
import com.rubp.whattoeat.feature.settings.viewmodel.SettingsViewModel

@Composable
fun App(appContainer: AppContainer) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = appContainer.appViewModelFactory)

    val themeMode by settingsViewModel.themeModeStateFlow.collectAsState()
    val appLanguage by settingsViewModel.appLanguageStateFlow.collectAsState()

    remember(appLanguage) { applyAppLanguage(appLanguage) }

    WhatToEatTheme(themeMode) {
        MainScreen(
            appContainer.appViewModelFactory,
            settingsViewModel
        )
    }
}