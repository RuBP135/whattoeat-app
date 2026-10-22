package com.rubp.whattoeat.feature.food.model

import com.rubp.whattoeat.feature.food.data.entity.Food
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import com.rubp.whattoeat.feature.food.ui.choose.FoodSelectionState

sealed interface FoodDataState {
    data object Loading : FoodDataState

    data class Error(val exception: Exception) : FoodDataState

    data class NoTableSelected(val tables: List<FoodTable>) : FoodDataState

    data class Selected(
        val tables: List<FoodTable>,
        val currentTable: FoodTable,
        val foods: List<Food>,
        val selectionState: FoodSelectionState
    ) : FoodDataState {
        val chosenFood: Food?
            get() = when (val state = selectionState) {
                is FoodSelectionState.Chosen -> foods.find { it.id == state.id }
                else -> null
            }
    }
}
