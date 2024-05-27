package com.doorxii.ludus

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.data.models.ludus.Ludus
import com.doorxii.ludus.ui.theme.LudusTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors

class StartActivity : ComponentActivity() {

    var newDialogueVisible = false
    var loadDialogueVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            LudusTheme {
                StartMenu()
            }
        }

        Log.d(TAG, "dbs: ${getAllDatabases()}")
    }

    fun getAllDatabases(): List<String> {

        val databases = mutableListOf<String>()
        applicationContext.databaseList().forEach { databaseName ->
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

    fun createDb(ludusName: String) {
        GlobalScope.launch(Dispatchers.IO) {
            Log.d(TAG, "createDb: $ludusName")

            // Validate the ludusName parameter
            if (ludusName.isEmpty() || ludusName.contains(Regex("[\\s\\W]"))) {
                Log.e(TAG, "Invalid database name: $ludusName")
                return@launch
            }

            // Check if the database file already exists
            val dbFile = applicationContext.getDatabasePath(ludusName)
            if (dbFile.exists()) {
                Log.d(TAG, "Database file already exists: $ludusName")
                return@launch
            }

            // Create the database file
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                ludusName
            )
                .enableMultiInstanceInvalidation()
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .setQueryExecutor(Executors.newSingleThreadExecutor())
                .setTransactionExecutor(Executors.newSingleThreadExecutor())
                .build()


//            db.ludusDao().insertLudus(Ludus(ludusName))

            Log.d(TAG, "Database file created: $ludusName")

            Log.d(TAG, db.ludusDao().getAllLudus().first().toString())
        }


    }

    fun loadDb(dbName: String): AppDatabase {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, dbName
        ).build()
        return db
    }

    fun populateDb(db: AppDatabase) {

    }

    fun launchLudusManagement(){

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteDb(dbName: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, dbName
            ).build()
            db.clearAllTables()
            db.close()
            val file = applicationContext.getDatabasePath(dbName)
            val jFile = File(file.parentFile, "${file.name}-journal")
            file.delete()
            jFile.delete()
        }

    }

    @Preview
    @Composable
    fun StartMenu() {

        val newDialogueState = remember { mutableStateOf(newDialogueVisible) }
        val loadDialogueState = remember { mutableStateOf(loadDialogueVisible) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { newDialogueState.value = true }) {
                Text("New Ludus")
            }
            Button(onClick = { loadDialogueState.value = true }) {
                Text("Load Ludus")
            }

        }
        if (newDialogueState.value) {
            NewLudusDialogue() {
                newDialogueState.value = it
            }
        }
        if (loadDialogueState.value) {
            LoadLudusDialogue() {
                loadDialogueState.value = it
                loadDialogueState.value = it
            }
        }
    }

    @Preview
    @Composable
    fun NewLudusDialogue(visibleCallback: (Boolean) -> Unit = { newDialogueVisible = it }) {
        var ludusName by remember { mutableStateOf("") }
        var databases = getAllDatabases()

        AlertDialog(
            onDismissRequest = {
                visibleCallback(false)
            },
            confirmButton = {
                Button(
                    onClick = {
                        Log.d(TAG, "New Ludus: $ludusName")
                        createDb(ludusName)
                        visibleCallback(false)
                    },
                    enabled = ludusName.isNotEmpty() && ludusName !in databases.map { it.toString() }
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                Button(onClick = {
                    visibleCallback(false)
                    Log.d(TAG, "Cancel button, state: $newDialogueVisible")
                }) {
                    Text("Cancel")

                }
            },
            title = {
                Text("Enter the name of your new Ludus:")
            },
            text = {
                OutlinedTextField(
                    value = ludusName,
                    onValueChange = { name: String ->
                        ludusName = name
                    },
                    label = { Text("Ludus Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }

    @Preview
    @Composable
    fun LoadLudusDialogue(
        visibleCallback: (Boolean) -> Unit = { loadDialogueVisible = it }
    ) {

        var databases = remember { mutableStateOf(getAllDatabases()) }
        val selectedDb = remember { mutableStateOf("") }
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { visibleCallback(false) },
            confirmButton = { },
            dismissButton = {
                Button(onClick = { visibleCallback(false) }) {
                    Text("Dismiss")
                }
            },
            title = { Text("Load Ludus") },
            text = {
                LazyColumn {
                    items(databases.value) { database ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedDb.value = database.toString() },
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = MaterialTheme.shapes.small,
                                    tonalElevation = 4.dp,
                                    color = if (selectedDb.value == database.toString()) {
                                        Color.LightGray
                                    } else {
                                        Color.Transparent
                                    }
                                ) {
                                    Text(database.toString())
                                }
                                Column {
                                    Row {
                                        Button(onClick = {
                                            loadDb(database.toString())
                                            visibleCallback(false)
                                        }) { Text("Load") }
                                        Button(onClick = {
                                            deleteDb(database.toString())
                                            databases.value = getAllDatabases()
                                        }) { Text("Delete") }
                                    }
                                }

                            }

                        }
                    }
                }
            }
        )

    }


    companion object {
        private const val TAG = "StartActivity"
    }

}