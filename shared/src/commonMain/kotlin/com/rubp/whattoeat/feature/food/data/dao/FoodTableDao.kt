package com.rubp.whattoeat.feature.food.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.rubp.whattoeat.feature.food.data.entity.FoodTable

@Dao
interface FoodTableDao {
    @Query("SELECT * FROM food_table WHERE id = :id")
    fun findById(id: Long): Flow<FoodTable?>

    @Query("SELECT * FROM food_table WHERE id = :id")
    suspend fun findByIdOnce(id: Long): FoodTable?

    @Query("SELECT * FROM food_table ORDER BY created_at ASC")
    fun findAll(): Flow<List<FoodTable>>

    @Query("SELECT * FROM food_table ORDER BY created_at ASC")
    suspend fun findAllOnce(): List<FoodTable>

    @Query("UPDATE food_table SET name = :name WHERE id = :id")
    suspend fun updateName(id: Long, name: String): Int

    @Insert
    suspend fun insert(table: FoodTable): Long

    @Update
    suspend fun update(table: FoodTable): Int

    @Delete
    suspend fun delete(table: FoodTable): Int

    @Query("DELETE FROM food_table WHERE id = :id")
    suspend fun deleteById(id: Long): Int
}
