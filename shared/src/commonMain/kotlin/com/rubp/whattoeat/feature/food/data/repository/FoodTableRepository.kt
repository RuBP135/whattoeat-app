package com.rubp.whattoeat.feature.food.data.repository

import com.rubp.whattoeat.core.data.exception.DataAccessException
import com.rubp.whattoeat.core.data.exception.withDataAccess
import com.rubp.whattoeat.feature.food.data.dao.FoodTableDao
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class FoodTableRepository(
    private val dao: FoodTableDao
) {
    fun findById(id: Long): Flow<FoodTable?> =
        dao.findById(id).catch{ throw DataAccessException.wrapOrThrowCancellationException(it) }

    fun findAll(): Flow<List<FoodTable>> =
        dao.findAll().catch { throw DataAccessException.wrapOrThrowCancellationException(it) }


    suspend fun insert(table: FoodTable): Long =
        withDataAccess { dao.insert(table) }

    suspend fun update(table: FoodTable): Int =
        withDataAccess { dao.update(table) }

    suspend fun delete(table: FoodTable): Int =
        withDataAccess { dao.delete(table) }

    suspend fun deleteById(id: Long): Int =
        withDataAccess { dao.deleteById(id) }

}
