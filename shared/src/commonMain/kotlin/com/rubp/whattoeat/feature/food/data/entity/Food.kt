package com.rubp.whattoeat.feature.food.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Clock

@Entity(
    tableName = "food",
    foreignKeys = [
        ForeignKey(
            entity = FoodTable::class,
            parentColumns = ["id"],
            childColumns = ["table_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["table_id"])]
)
data class Food(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L, // 让autoGenerate自动分配唯一id
    @ColumnInfo(name = "created_at") val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "marked") val marked: Boolean,
    @ColumnInfo(name = "table_id") val tableId: Long = 1L
)