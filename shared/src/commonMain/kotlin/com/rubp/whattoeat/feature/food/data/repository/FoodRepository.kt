package com.rubp.whattoeat.feature.food.data.repository

import com.rubp.whattoeat.feature.food.data.dao.FoodDao
import com.rubp.whattoeat.feature.food.data.entity.Food
import kotlinx.coroutines.flow.Flow

class FoodRepository(
    private val dao: FoodDao
) {
    fun findByTableId(tableId: Long): Flow<List<Food>> = dao.findByTableId(tableId)

    fun findAll(): Flow<List<Food>> = dao.findAll()

    fun findById(): Flow<Food> = dao.find

    suspend fun update(food: Food) = dao.update(food)

    suspend fun insert(food: Food) = dao.insert(food)

    suspend fun insertAll(foods: List<Food>) = dao.insertAll(foods)

    suspend fun delete(food: Food) = dao.delete(food)

    suspend fun deleteByTableId(tableId: Long) = dao.deleteByTableId(tableId)

    suspend fun updateAllMarked(tableId: Long, marked: Boolean) = dao.updateAllMarked(tableId, marked)
}