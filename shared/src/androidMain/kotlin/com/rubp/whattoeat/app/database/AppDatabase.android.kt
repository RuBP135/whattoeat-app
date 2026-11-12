package com.rubp.whattoeat.app.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun createDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("room.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
