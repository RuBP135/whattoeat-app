package com.rubp.whattoeat.app.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rubp.whattoeat.app.database.AppDatabase
import com.rubp.whattoeat.app.logging.LoggingInitializationException
import com.rubp.whattoeat.core.ui.feedback.UiFeedback
import com.rubp.whattoeat.feature.food.data.preferences.FoodPreferences
import com.rubp.whattoeat.feature.food.data.repository.FoodRepository
import com.rubp.whattoeat.feature.food.data.repository.FoodTableRepository
import com.rubp.whattoeat.feature.food.data.repository.FoodTableSelectionRepository
import com.rubp.whattoeat.feature.food.viewmodel.FoodViewModel
import com.rubp.whattoeat.feature.settings.data.preferences.SettingsPreferences
import com.rubp.whattoeat.feature.settings.data.repository.SettingsRepository
import com.rubp.whattoeat.feature.settings.viewmodel.SettingsViewModel
import com.russhwolf.settings.ObservableSettings

class AppContainer(
    database: AppDatabase,
    settings: ObservableSettings,
    val logInitException: LoggingInitializationException?
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

    // viewModelFactory

    val appViewModelFactory = viewModelFactory {

        initializer {
            FoodViewModel(
                foodRepository,
                foodTableRepository,
                foodTableSelectionRepository,
                uiFeedback
            )
        }

        initializer {
            SettingsViewModel(
                settingsRepository,
                uiFeedback
            )
        }
    }

}
