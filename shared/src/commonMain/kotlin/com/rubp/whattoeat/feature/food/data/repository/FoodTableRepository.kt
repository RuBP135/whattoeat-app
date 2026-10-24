package com.rubp.whattoeat.feature.food.data.repository

import com.rubp.whattoeat.core.data.exception.DataAccessException
import com.rubp.whattoeat.core.data.exception.withDataAccess
import com.rubp.whattoeat.core.model.ReadState
import com.rubp.whattoeat.core.model.asReadState
import com.rubp.whattoeat.feature.food.data.dao.FoodTableDao
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class FoodTableRepository(
    private val dao: FoodTableDao
) {
    fun findById(id: Long): Flow<ReadState<FoodTable?>> =
        dao.findById(id)
            .catch{ throw DataAccessException.wrapOrThrowCancellationException(it) }
            .asReadState()

    fun findAll(): Flow<ReadState<List<FoodTable>>> =
        dao.findAll()
            .catch { throw DataAccessException.wrapOrThrowCancellationException(it) }
            .asReadState()

    suspend fun updateName(id: Long, name: String): Int =
        withDataAccess { dao.updateName(id, name) }

    suspend fun insert(table: FoodTable): Long =
        withDataAccess { dao.insert(table) }

    suspend fun update(table: FoodTable): Int =
        withDataAccess { dao.update(table) }

    suspend fun deleteById(id: Long): Int =
        withDataAccess { dao.deleteById(id) }

}
