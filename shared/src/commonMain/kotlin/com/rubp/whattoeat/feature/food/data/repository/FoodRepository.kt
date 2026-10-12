package com.rubp.whattoeat.feature.food.data.repository

import com.rubp.whattoeat.core.data.withDatabaseReadFlow
import com.rubp.whattoeat.core.data.withDatabaseWrite
import com.rubp.whattoeat.feature.food.data.dao.FoodDao
import com.rubp.whattoeat.feature.food.data.entity.Food
import kotlinx.coroutines.flow.Flow

class FoodRepository(
    private val dao: FoodDao
) {
    fun findByTableId(tableId: Long): Flow<List<Food>> =
        withDatabaseReadFlow("FoodRepository.findByTableId") {
            dao.findByTableId(tableId)
        }

    suspend fun updateName(id: Long, name: String): Int =
        withDatabaseWrite("FoodRepository.updateName") {
            dao.updateName(id, name)
        }

    suspend fun updateWeight(id: Long, weight: Int): Int =
        withDatabaseWrite("FoodRepository.updateWeight") {
            dao.updateWeight(id, weight)
        }

    suspend fun updateMarked(id: Long, marked: Boolean): Int =
        withDatabaseWrite("FoodRepository.updateMarked") {
            dao.updateMarked(id, marked)
        }

    suspend fun insert(food: Food): Long =
        withDatabaseWrite("FoodRepository.insert") {
            dao.insert(food)
        }

    suspend fun insertAll(foods: List<Food>): List<Long> =
        withDatabaseWrite("FoodRepository.insertAll") {
            dao.insertAll(foods)
        }

    suspend fun deleteById(id: Long): Int =
        withDatabaseWrite("FoodRepository.deleteById") {
            dao.deleteById(id)
        }

    suspend fun deleteByTableId(tableId: Long): Int =
        withDatabaseWrite("FoodRepository.deleteByTableId") {
            dao.deleteByTableId(tableId)
        }

    suspend fun updateAllMarked(tableId: Long, marked: Boolean): Int =
        withDatabaseWrite("FoodRepository.updateAllMarked") {
            dao.updateAllMarked(tableId, marked)
        }
}
