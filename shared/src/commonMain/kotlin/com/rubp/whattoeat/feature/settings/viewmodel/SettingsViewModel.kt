package com.rubp.whattoeat.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubp.whattoeat.core.theme.ColorTheme
import com.rubp.whattoeat.feature.settings.data.preferences.ConfigRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel(
    private val repository: ConfigRepository = ConfigRepository
): ViewModel() {

    val colorTheme: StateFlow<ColorTheme> = repository.colorThemeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.Eagerly,
        initialValue = ColorTheme.Pink
    )
    fun saveColorTheme(colorTheme: ColorTheme){
        repository.saveColorTheme(colorTheme)
    }

}