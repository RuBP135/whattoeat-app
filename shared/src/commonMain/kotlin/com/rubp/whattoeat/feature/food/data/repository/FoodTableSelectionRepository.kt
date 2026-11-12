package com.rubp.whattoeat.feature.food.data.repository

import com.rubp.whattoeat.core.data.exception.DataAccessException
import com.rubp.whattoeat.core.data.exception.withDataAccess
import com.rubp.whattoeat.feature.food.data.preferences.FoodPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch


class FoodTableSelectionRepository(
    private val preferences: FoodPreferences
) {
    val foodTableIdFlow: Flow<Long?> = preferences.foodTableIdFlow
        .catch { throw DataAccessException.wrapOrThrowCancellationException(it) }

    suspend fun getFoodTableId(): Long? =
        withDataAccess { preferences.getFoodTableId() }

    suspend fun saveFoodTableId(id: Long?) =
        withDataAccess { preferences.saveFoodTableId(id) }
}
