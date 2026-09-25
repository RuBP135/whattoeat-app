package com.rubp.whattoeat.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubp.whattoeat.core.theme.ColorTheme
import com.rubp.whattoeat.feature.settings.data.preferences.SettingsPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel(
    private val settingsPreferences: SettingsPreferences
): ViewModel() {

    val colorTheme: StateFlow<ColorTheme> = settingsPreferences.colorThemeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ColorTheme.Pink
    )
    fun saveColorTheme(colorTheme: ColorTheme){
        settingsPreferences.saveColorTheme(colorTheme)
    }

}