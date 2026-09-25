package com.rubp.whattoeat

import android.app.Application
import com.rubp.whattoeat.app.database.AppDatabase
import com.rubp.whattoeat.app.database.getDatabaseBuilder

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(getDatabaseBuilder(this))
    }
}