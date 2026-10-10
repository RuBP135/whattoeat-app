package com.rubp.whattoeat.feature.food.ui.choose

sealed interface FoodSelectionState {
    object Idle: FoodSelectionState
    data class Chosen(val id: Long): FoodSelectionState
    object NoAvailableFood: FoodSelectionState
}