package com.rubp.whattoeat.app.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rubp.whattoeat.app.database.AppDatabase
import com.rubp.whattoeat.feature.food.data.preferences.FoodPreferences
import com.rubp.whattoeat.feature.food.data.repository.FoodRepository
import com.rubp.whattoeat.feature.food.data.repository.FoodTableRepository
import com.rubp.whattoeat.feature.food.viewmodel.FoodViewModel
import com.rubp.whattoeat.feature.settings.data.preferences.SettingsPreferences
import com.rubp.whattoeat.feature.settings.viewmodel.SettingsViewModel
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings

object AppContainer {

    private val database: AppDatabase = AppDatabase.database
    private val settings: ObservableSettings = Settings() as ObservableSettings


    // food
    private val foodDao = database.foodDao()
    private val foodTableDao = database.foodTableDao()

    private val foodRepository = FoodRepository(foodDao)
    private val foodTableRepository = FoodTableRepository(foodTableDao)
    private val foodPreferences = FoodPreferences(settings)


    // settings
    private val settingsPreferences = SettingsPreferences(settings)


    // viewModelFactory

    val appViewModelFactory = viewModelFactory {

        initializer {
            FoodViewModel(
                foodRepository,
                foodTableRepository,
                foodPreferences
            )
        }

        initializer {
            SettingsViewModel(
                settingsPreferences
            )
        }
    }

}