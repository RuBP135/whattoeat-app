package com.rubp.whattoeat.feature.food.model

import com.rubp.whattoeat.feature.food.data.entity.Food

sealed interface FoodSelectionState {
    object Idle: FoodSelectionState
    data class Chosen(val food: Food): FoodSelectionState
    object NoAvailableFood: FoodSelectionState
}