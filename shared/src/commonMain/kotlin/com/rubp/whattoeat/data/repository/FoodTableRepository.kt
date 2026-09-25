package com.rubp.whattoeat.data.repository

import kotlinx.coroutines.flow.Flow
import com.rubp.whattoeat.feature.food.data.dao.FoodTableDao
import com.rubp.whattoeat.data.local.database.AppDatabase
import com.rubp.whattoeat.feature.food.data.entity.FoodTable

class FoodTableRepository(
    private val dao: FoodTableDao = AppDatabase.database.foodTableDao()
) {
    fun getById(id: Long): Flow<FoodTable?> = dao.getById(id)

    fun getAll(): Flow<List<FoodTable>> = dao.getAll()

    suspend fun insert(table: FoodTable): Long = dao.insert(table)

    suspend fun update(table: FoodTable) = dao.update(table)

    suspend fun delete(table: FoodTable) = dao.delete(table)

    suspend fun deleteById(id: Long) = dao.deleteById(id)
}
