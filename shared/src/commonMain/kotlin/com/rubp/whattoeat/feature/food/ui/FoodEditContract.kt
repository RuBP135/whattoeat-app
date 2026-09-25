package com.rubp.whattoeat.feature.food.ui

import com.rubp.whattoeat.feature.food.data.entity.Food
import com.rubp.whattoeat.domain.FoodTableDto

interface FoodEditActions {
    fun onReturnToEat()
    fun onTableSelected(id: Long)
    fun onRenameTable(tableId: Long, name: String)
    fun onDeleteTable(tableId: Long)
    fun onCreateTable(name: String)
    fun onImportFoodAndTable(dto: FoodTableDto)
    fun onAddFood()
    fun onDelFood(food: Food)
    fun onClickStar(food: Food)
    fun onInputName(food: Food, name: String)
    fun onInputWeight(food: Food, weight: Int)
}

sealed class EditDialogState {
    data object None : EditDialogState()
    data object CreateTable : EditDialogState()
    data object RenameTable : EditDialogState()
    data object DeleteTable : EditDialogState()
    data class DeleteFood(val food: Food) : EditDialogState()
    data object Help: EditDialogState()
}