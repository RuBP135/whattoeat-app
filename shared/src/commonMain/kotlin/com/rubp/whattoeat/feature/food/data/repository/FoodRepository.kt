package com.rubp.whattoeat.feature.food.data.repository

import com.rubp.whattoeat.feature.food.data.dao.FoodDao
import com.rubp.whattoeat.feature.food.data.entity.Food
import kotlinx.coroutines.flow.Flow

class FoodRepository(
    private val dao: FoodDao
) {
    fun findByTableId(tableId: Long): Flow<List<Food>> = dao.findByTableId(tableId)

    suspend fun update(food: Food): Int = dao.update(food)

    suspend fun insert(food: Food): Long = dao.insert(food)

    suspend fun insertAll(foods: List<Food>): List<Long> = dao.insertAll(foods)

    suspend fun delete(food: Food): Int = dao.delete(food)

    suspend fun deleteByTableId(tableId: Long): Int = dao.deleteByTableId(tableId)

    suspend fun updateAllMarked(tableId: Long, marked: Boolean): Int = dao.updateAllMarked(tableId, marked)
}