package com.rubp.whattoeat.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubp.whattoeat.core.theme.ThemeMode
import com.rubp.whattoeat.core.ui.feedback.UiFeedback
import com.rubp.whattoeat.core.ui.async.UiFlowHandler
import com.rubp.whattoeat.core.ui.async.UiTaskRunner
import com.rubp.whattoeat.feature.settings.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    feedback: UiFeedback
): ViewModel() {

    private val tag = "SettingsViewModel"

    private val handler = UiFlowHandler(
        tag = tag,
        onMessage = feedback::showMessage
    )
    private val runner = UiTaskRunner(
        scope = viewModelScope,
        tag = tag,
        onMessage = feedback::showMessage,
        onError = feedback::showError
    )

    val themeModeStateFlow: StateFlow<ThemeMode> = handler.asReadValue(
        flow = settingsRepository.themeModeFlow,
        errorMessage = "主题设置不可用",
        errorLogMessage = "Failed to observe theme mode"
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ThemeMode.System
    )

    fun saveThemeMode(themeMode: ThemeMode) = runner.launch(
        errorLogMessage = "Failed to save theme mode"
    ) {
        settingsRepository.saveThemeMode(themeMode)
    }
}
