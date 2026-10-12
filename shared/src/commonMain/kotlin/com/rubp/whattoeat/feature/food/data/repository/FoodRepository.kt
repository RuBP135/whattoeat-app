package com.rubp.whattoeat.feature.food.data.repository

import com.rubp.whattoeat.feature.food.data.dao.FoodDao
import com.rubp.whattoeat.feature.food.data.entity.Food
import kotlinx.coroutines.flow.Flow

class FoodRepository(
    private val dao: FoodDao
) {
    fun findByTableId(tableId: Long): Flow<List<Food>> = dao.findByTableId(tableId)

    suspend fun updateName(id: Long, name: String): Int = dao.updateName(id, name)

    suspend fun updateWeight(id: Long, weight: Int): Int = dao.updateWeight(id, weight)

    suspend fun updateMarked(id: Long, marked: Boolean): Int = dao.updateMarked(id, marked)

    suspend fun insert(food: Food): Long = dao.insert(food)

    suspend fun insertAll(foods: List<Food>): List<Long> = dao.insertAll(foods)

    suspend fun deleteById(id: Long): Int = dao.deleteById(id)

    suspend fun deleteByTableId(tableId: Long): Int = dao.deleteByTableId(tableId)

    suspend fun updateAllMarked(tableId: Long, marked: Boolean): Int = dao.updateAllMarked(tableId, marked)
}