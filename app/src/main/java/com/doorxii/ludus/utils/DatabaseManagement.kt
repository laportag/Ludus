package com.doorxii.ludus.utils

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.data.models.ludus.Ludus
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors

object DatabaseManagement {

    fun getAllDatabases(context: Context): List<String> {

        val databases = mutableListOf<String>()
        context.databaseList().forEach { databaseName ->
            if (!databaseName.endsWith("-journal") && !databaseName.endsWith("-wal") && !databaseName.endsWith(
                    "-shm"
                )
            ) {
                databases.add(databaseName)
            }
        }
        Log.d(TAG, "getAllDatabases: $databases")
        return databases
    }

    suspend fun createDb(ludusName: String, context: Context): AppDatabase {
        Log.d(TAG, "createDb: $ludusName")

        val ludusName = ludusName.replace(" ", "_")

        // Validate the ludusName parameter
        if (ludusName.isEmpty() || ludusName.contains(Regex("[\\s\\W]"))) {
            Log.e(TAG, "Invalid database name: $ludusName")
            throw IllegalArgumentException("Invalid database name: $ludusName")
        }

        // Check if the database file already exists
        val dbFile = context.getDatabasePath(ludusName)
        if (dbFile.exists()) {
            Log.d(TAG, "Database file already exists: $ludusName")
            throw IllegalArgumentException("Database file already exists: $ludusName")
        }

        // Create the database file
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            ludusName
        )
            .enableMultiInstanceInvalidation()
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .setQueryExecutor(Executors.newSingleThreadExecutor())
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()

        Log.d(TAG, "Database file created: $ludusName")
        val ludus = Ludus(ludusName)
        ludus.playerLudus = true
        db.ludusDao().insertLudus(ludus)

        val id = db.ludusDao().getLudusByName(ludus.name).first().ludusId
        val newGladiators = GladiatorGenerator.newGladiatorListWithLudusId(7, id)
        for (gladiator in newGladiators) {
            db.gladiatorDao().insertGladiator(gladiator)
        }
        populateDb(db)

        return db


    }

    fun returnDb(dbName: String, context: Context): AppDatabase {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            dbName
        )   .enableMultiInstanceInvalidation()
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .setQueryExecutor(Executors.newSingleThreadExecutor())
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
        return db
    }

    private suspend fun populateDb(db: AppDatabase) {
        val ludi = listOf("Ludus Magnus", "Capua", "Pergamum", "Alexandria", "Praeneste")
        for (ludusName in ludi) {
            val newLudus = Ludus(ludusName)
            db.ludusDao().insertLudus(newLudus)
            val id = db.ludusDao().getLudusByName(ludusName).first().ludusId

            val newGladiators = GladiatorGenerator.newGladiatorListWithLudusId(7, id)
            for (gladiator in newGladiators) {
                db.gladiatorDao().insertGladiator(gladiator)
            }
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteDb(dbName: String, context: Context, callback: (Boolean) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, dbName
            ).build()
            db.clearAllTables()
            db.close()
            val file = context.getDatabasePath(dbName)
            val jFile = File(file.parentFile, "${file.name}-journal")
            file.delete()
            jFile.delete()
            callback(true)
        }

    }
}

const val TAG = "DatabaseManagement"