package com.rubp.whattoeat.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubp.whattoeat.core.theme.ThemeMode
import com.rubp.whattoeat.feature.settings.data.preferences.SettingsPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel(
    private val settingsPreferences: SettingsPreferences
): ViewModel() {

    val themeModeStateFlow: StateFlow<ThemeMode> = settingsPreferences.themeModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ThemeMode.System
    )

    fun saveThemeMode(themeMode: ThemeMode){
        settingsPreferences.saveThemeMode(themeMode)
    }
}