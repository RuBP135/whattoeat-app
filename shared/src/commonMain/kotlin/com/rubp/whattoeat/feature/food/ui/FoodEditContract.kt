package com.rubp.whattoeat.feature.food.ui

import com.rubp.whattoeat.feature.food.data.entity.Food

sealed class EditDialogState {
    data object None : EditDialogState()
    data object CreateTable : EditDialogState()
    data object RenameTable : EditDialogState()
    data object DeleteTable : EditDialogState()
    data class DeleteFood(val food: Food) : EditDialogState()
    data object Help: EditDialogState()
}