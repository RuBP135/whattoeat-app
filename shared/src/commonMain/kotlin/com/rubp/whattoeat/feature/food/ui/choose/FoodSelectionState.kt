package com.rubp.whattoeat.feature.food.ui.choose

sealed interface FoodSelectionState {
    object Idle: FoodSelectionState
    class Chosen(id: Long): FoodSelectionState
    object NoAvailableFood: FoodSelectionState
}