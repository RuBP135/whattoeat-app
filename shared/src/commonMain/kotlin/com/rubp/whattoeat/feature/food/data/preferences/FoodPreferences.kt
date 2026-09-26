package com.rubp.whattoeat.feature.food.data.preferences

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getLongOrNullFlow

const val FOOD_TABLE_ID = "food_table_id"

class FoodPreferences(
    private val settings: ObservableSettings
) {
    @OptIn(ExperimentalSettingsApi::class)
    val foodTableIdFlow = settings.getLongOrNullFlow(
        FOOD_TABLE_ID
    )

    fun saveFoodTableId(id: Long){
        settings.putLong(FOOD_TABLE_ID, id)
    }
}


