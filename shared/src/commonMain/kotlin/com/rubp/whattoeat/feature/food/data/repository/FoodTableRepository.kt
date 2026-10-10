package com.rubp.whattoeat.feature.food.data.repository

import com.rubp.whattoeat.feature.food.data.dao.FoodTableDao
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import kotlinx.coroutines.flow.Flow

class FoodTableRepository(
    private val dao: FoodTableDao
) {
    fun findById(id: Long): Flow<FoodTable?> = dao.findById(id)

    fun findAll(): Flow<List<FoodTable>> = dao.findAll()

    suspend fun insert(table: FoodTable): Long = dao.insert(table)

    suspend fun update(table: FoodTable): Int = dao.update(table)

    suspend fun delete(table: FoodTable): Int = dao.delete(table)

    suspend fun deleteById(id: Long): Int = dao.deleteById(id)
}