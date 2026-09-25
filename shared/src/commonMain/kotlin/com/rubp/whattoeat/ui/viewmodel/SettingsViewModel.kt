package com.rubp.whattoeat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubp.whattoeat.data.repository.ConfigRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import com.rubp.whattoeat.core.theme.ColorTheme

class SettingsViewModel(
    private val repository: ConfigRepository = ConfigRepository
): ViewModel() {

    val colorTheme: StateFlow<ColorTheme> = repository.colorThemeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ColorTheme.Pink
    )
    fun saveColorTheme(colorTheme: ColorTheme){
        repository.saveColorTheme(colorTheme)
    }

}