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
            if (isValidDatabaseName(databaseName)) {
                databases.add(databaseName)
            }
        }
        Log.d(TAG, "getAllDatabases: $databases")
        return databases
    }

    private fun isValidDatabaseName(name: String): Boolean {
        // Filter out Room's temporary files and anything that doesn't look like a valid db name
        return !name.endsWith("-journal") &&
                !name.endsWith("-wal") &&
                !name.endsWith("-shm") &&
                name.isNotBlank() &&
                name.matches(Regex("^[a-zA-Z0-9_]+$")) //only contains letters, numbers and underscores
    }

    suspend fun createDb(ludusName: String, context: Context): AppDatabase {
        Log.d(TAG, "createDb: $ludusName")

        val name = ludusName.replace(" ", "_")

        // Validate the ludusName parameter
        if (!isValidDatabaseName(name)) {
            Log.e(TAG, "Invalid database name: $name")
            throw IllegalArgumentException("Invalid database name: $name")
        }

        // Check if the database file already exists
        val dbFile = context.getDatabasePath(name)
        if (dbFile.exists()) {
            Log.d(TAG, "Database file already exists: $name")
            throw IllegalArgumentException("Database file already exists: $name")
        }

        // Create the database file
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            name
        )
            .enableMultiInstanceInvalidation()
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .setQueryExecutor(Executors.newSingleThreadExecutor())
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()

        Log.d(TAG, "Database file created: $name")
        val ludus = Ludus(name)
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