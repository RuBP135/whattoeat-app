package com.rubp.whattoeat.app.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rubp.whattoeat.app.database.AppDatabase
import com.rubp.whattoeat.app.logging.DeduplicatingExceptionLogRecorder
import com.rubp.whattoeat.app.logging.LoggingInitializationException
import com.rubp.whattoeat.core.ui.feedback.UiFeedback
import com.rubp.whattoeat.feature.food.data.preferences.FoodPreferences
import com.rubp.whattoeat.feature.food.data.repository.FoodRepository
import com.rubp.whattoeat.feature.food.data.repository.FoodTableRepository
import com.rubp.whattoeat.feature.food.data.repository.FoodTableSelectionRepository
import com.rubp.whattoeat.feature.food.viewmodel.FoodViewModel
import com.rubp.whattoeat.feature.logs.data.repository.LogRepository
import com.rubp.whattoeat.feature.logs.viewmodel.LogViewModel
import com.rubp.whattoeat.feature.settings.data.preferences.SettingsPreferences
import com.rubp.whattoeat.feature.settings.data.repository.SettingsRepository
import com.rubp.whattoeat.feature.settings.viewmodel.SettingsViewModel
import com.russhwolf.settings.ObservableSettings
import kotlinx.io.files.Path


class AppContainer(
    database: AppDatabase,
    settings: ObservableSettings,
    val logInitException: LoggingInitializationException?,
    logDirectory: Path?,
    val deduplicatingExceptionLogRecorder: DeduplicatingExceptionLogRecorder
) {

    // 共享 UI 操作反馈
    val uiFeedback = UiFeedback()

    // food
    private val foodDao = database.foodDao()
    private val foodTableDao = database.foodTableDao()

    private val foodRepository = FoodRepository(foodDao)
    private val foodTableRepository = FoodTableRepository(
        dao = foodTableDao,
        foodDao = foodDao,
        database = database
    )
    private val foodPreferences = FoodPreferences(settings)
    private val foodTableSelectionRepository = FoodTableSelectionRepository(foodPreferences)

    // settings
    private val settingsPreferences = SettingsPreferences(settings)
    private val settingsRepository = SettingsRepository(settingsPreferences)

    // logs
    private val logRepository = LogRepository(logDirectory)

    // viewModelFactory

    val appViewModelFactory = viewModelFactory {

        initializer {
            LogViewModel(logRepository, uiFeedback, deduplicatingExceptionLogRecorder)
        }

        initializer {
            FoodViewModel(
                foodRepository,
                foodTableRepository,
                foodTableSelectionRepository,
                uiFeedback,
                deduplicatingExceptionLogRecorder
            )
        }

        initializer {
            SettingsViewModel(
                settingsRepository,
                uiFeedback,
                deduplicatingExceptionLogRecorder
            )
        }
    }

}
