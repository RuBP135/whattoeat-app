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
    fun getById(id: Long): Flow<FoodTable?>

    @Query("SELECT * FROM food_table ORDER BY created_at ASC")
    fun getAll(): Flow<List<FoodTable>>

    @Insert
    suspend fun insert(table: FoodTable): Long

    @Update
    suspend fun update(table: FoodTable)

    @Delete
    suspend fun delete(table: FoodTable)

    @Query("DELETE FROM food_table WHERE id = :id")
    suspend fun deleteById(id: Long)
}
