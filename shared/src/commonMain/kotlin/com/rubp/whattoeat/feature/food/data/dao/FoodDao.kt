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
    fun getByTableId(tableId: Long): Flow<List<Food>>

    @Query("SELECT * FROM food ORDER BY created_at ASC")
    fun getAll(): Flow<List<Food>>

    @Update
    suspend fun update(food: Food)

    @Insert
    suspend fun insert(food: Food)

    @Insert
    suspend fun insertAll(foods: List<Food>)

    @Delete
    suspend fun delete(food: Food)

    @Query("DELETE FROM food WHERE table_id = :tableId")
    suspend fun deleteByTableId(tableId: Long)

    @Query("UPDATE food SET marked = :marked WHERE table_id = :tableId")
    suspend fun updateAllMarked(tableId: Long, marked: Boolean)
}
