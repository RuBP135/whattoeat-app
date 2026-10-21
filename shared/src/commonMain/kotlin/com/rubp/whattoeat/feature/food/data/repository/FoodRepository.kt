package com.rubp.whattoeat.feature.food.data.repository

import com.rubp.whattoeat.core.data.exception.DataAccessException
import com.rubp.whattoeat.core.data.exception.withDataAccess
import com.rubp.whattoeat.feature.food.data.dao.FoodDao
import com.rubp.whattoeat.feature.food.data.entity.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class FoodRepository(
    private val dao: FoodDao
) {
    fun findByTableId(tableId: Long): Flow<List<Food>> =
        dao.findByTableId(tableId).catch { throw DataAccessException.wrapOrThrowCancellationException(it) }


    suspend fun updateName(id: Long, name: String): Int =
        withDataAccess { dao.updateName(id, name) }

    suspend fun updateWeight(id: Long, weight: Int): Int =
        withDataAccess { dao.updateWeight(id, weight) }

    suspend fun updateMarked(id: Long, marked: Boolean): Int =
        withDataAccess { dao.updateMarked(id, marked) }

    suspend fun insert(food: Food): Long =
        withDataAccess { dao.insert(food) }

    suspend fun insertAll(foods: List<Food>): List<Long> =
        withDataAccess { dao.insertAll(foods) }

    suspend fun deleteById(id: Long): Int =
        withDataAccess { dao.deleteById(id) }

    suspend fun deleteByTableId(tableId: Long): Int =
        withDataAccess { dao.deleteByTableId(tableId) }

    suspend fun updateAllMarked(tableId: Long, marked: Boolean): Int =
        withDataAccess { dao.updateAllMarked(tableId, marked) }
}
