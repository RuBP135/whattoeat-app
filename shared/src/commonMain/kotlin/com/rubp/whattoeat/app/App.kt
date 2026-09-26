package com.rubp.whattoeat.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rubp.whattoeat.app.di.AppContainer.appViewModelFactory
import com.rubp.whattoeat.app.navigation.MainScreen
import com.rubp.whattoeat.core.theme.ColorTheme
import com.rubp.whattoeat.core.theme.WhatToEatTheme
import com.rubp.whattoeat.feature.settings.viewmodel.SettingsViewModel

@Composable
@Preview
fun App() {
    val settingsViewModel: SettingsViewModel = viewModel(factory = appViewModelFactory)

    val colorTheme: ColorTheme by settingsViewModel.colorTheme.collectAsState()

    WhatToEatTheme(colorTheme = colorTheme, darkTheme = isSystemInDarkTheme()) {
        MainScreen()
    }
}