package com.rubp.whattoeat.feature.food.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock

@Entity("food")
data class Food(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L, // = 0表示未分配，autoGenerate会自动分配唯一id
    @ColumnInfo(name = "created_at") val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "marked") val marked: Boolean,
    @ColumnInfo(name = "table_id") val tableId: Long = 1L
)