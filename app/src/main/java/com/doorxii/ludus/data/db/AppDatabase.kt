package com.doorxii.ludus.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.ludus.Ludus

@Database(entities= [Ludus::class, Gladiator::class], version=1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun ludusDao(): LudusDao
    abstract fun gladiatorDao(): GladiatorDao

}