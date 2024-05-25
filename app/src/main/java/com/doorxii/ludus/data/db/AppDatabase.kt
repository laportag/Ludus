package com.doorxii.ludus.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.doorxii.ludus.data.models.ludus.Ludus

@Database(entities= [Ludus::class], version=1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun ludusDao(): LudusDao
}