package com.rubp.whattoeat.feature.food.data.repository

import com.rubp.whattoeat.core.data.withDatabaseReadFlow
import com.rubp.whattoeat.core.data.withDatabaseWrite
import com.rubp.whattoeat.feature.food.data.dao.FoodTableDao
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import kotlinx.coroutines.flow.Flow

class FoodTableRepository(
    private val dao: FoodTableDao
) {
    fun findById(id: Long): Flow<FoodTable?> =
        withDatabaseReadFlow("FoodTableRepository.findById") {
            dao.findById(id)
        }

    fun findAll(): Flow<List<FoodTable>> =
        withDatabaseReadFlow("FoodTableRepository.findAll") {
            dao.findAll()
        }

    suspend fun insert(table: FoodTable): Long =
        withDatabaseWrite("FoodTableRepository.insert") {
            dao.insert(table)
        }

    suspend fun update(table: FoodTable): Int =
        withDatabaseWrite("FoodTableRepository.update") {
            dao.update(table)
        }

    suspend fun delete(table: FoodTable): Int =
        withDatabaseWrite("FoodTableRepository.delete") {
            dao.delete(table)
        }

    suspend fun deleteById(id: Long): Int =
        withDatabaseWrite("FoodTableRepository.deleteById") {
            dao.deleteById(id)
        }
}
