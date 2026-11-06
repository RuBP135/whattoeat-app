package com.rubp.whattoeat.feature.food.model

import com.rubp.whattoeat.feature.food.data.entity.Food

sealed interface FoodSelectionIdState {
    object Idle: FoodSelectionIdState
    data class Chosen(val id: Long): FoodSelectionIdState
    object NoAvailableFood: FoodSelectionIdState
}