package com.rubp.whattoeat.feature.food.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.rubp.whattoeat.feature.food.data.entity.Food

@Dao
interface FoodDao {
    @Query("SELECT * FROM food WHERE table_id = :tableId ORDER BY created_at ASC")
    fun findByTableId(tableId: Long): Flow<List<Food>>

    @Query("SELECT * FROM food ORDER BY created_at ASC")
    fun findAll(): Flow<List<Food>>

    @Update
    suspend fun update(food: Food): Int

    @Insert
    suspend fun insert(food: Food): Long

    @Insert
    suspend fun insertAll(foods: List<Food>): List<Long>

    @Delete
    suspend fun delete(food: Food): Int

    @Query("DELETE FROM food WHERE table_id = :tableId")
    suspend fun deleteByTableId(tableId: Long): Int

    @Query("UPDATE food SET marked = :marked WHERE table_id = :tableId")
    suspend fun updateAllMarked(tableId: Long, marked: Boolean): Int
}
