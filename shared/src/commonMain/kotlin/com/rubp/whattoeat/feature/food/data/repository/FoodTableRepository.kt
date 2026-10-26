package com.rubp.whattoeat.feature.food.data.repository

import androidx.room.immediateTransaction
import androidx.room.useWriterConnection
import com.rubp.whattoeat.app.database.AppDatabase
import com.rubp.whattoeat.core.data.exception.DataAccessException
import com.rubp.whattoeat.core.data.exception.withDataAccess
import com.rubp.whattoeat.core.model.ReadState
import com.rubp.whattoeat.core.model.asReadState
import com.rubp.whattoeat.feature.food.data.dao.FoodDao
import com.rubp.whattoeat.feature.food.data.dao.FoodTableDao
import com.rubp.whattoeat.feature.food.data.entity.Food
import com.rubp.whattoeat.feature.food.data.entity.FoodTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class FoodTableRepository(
    private val dao: FoodTableDao,
    private val foodDao: FoodDao,
    private val database: AppDatabase
) {
    fun findById(id: Long): Flow<ReadState<FoodTable?>> =
        dao.findById(id)
            .catch{ throw DataAccessException.wrapOrThrowCancellationException(it) }
            .asReadState()

    /** viewModel需要的一次性表格查询 */
    suspend fun findByIdOnce(id: Long): FoodTable? =
        withDataAccess { dao.findByIdOnce(id) }

    fun findAll(): Flow<ReadState<List<FoodTable>>> =
        dao.findAll()
            .catch { throw DataAccessException.wrapOrThrowCancellationException(it) }
            .asReadState()

    /** viewModel需要的一次性表格列表查询 */
    suspend fun findAllOnce(): List<FoodTable> =
        withDataAccess { dao.findAllOnce() }

    suspend fun updateName(id: Long, name: String): Int =
        withDataAccess { dao.updateName(id, name) }

    suspend fun insert(table: FoodTable): Long =
        withDataAccess { dao.insert(table) }

    suspend fun deleteById(id: Long): Int =
        withDataAccess { dao.deleteById(id) }

    suspend fun insertWithFood(
        table: FoodTable,
        foods: List<Food>
    ): Long = withDataAccess {
        database.useWriterConnection { transactor ->
            transactor.immediateTransaction {
                val id = dao.insert(table)
                foodDao.insertAll(foods.map { it.copy(tableId = id) })
                id
            }
        }
    }
}
