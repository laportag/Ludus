package com.doorxii.ludus.activities

import android.content.Intent
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
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.ui.theme.LudusTheme
import com.doorxii.ludus.utils.DatabaseManagement.createDb
import com.doorxii.ludus.utils.DatabaseManagement.deleteDb
import com.doorxii.ludus.utils.DatabaseManagement.getAllDatabases
import com.doorxii.ludus.utils.DatabaseManagement.returnDb
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

        Log.d(TAG, "dbs: ${getAllDatabases(applicationContext)}")
    }

    fun launchLudusManagement(name: String, db: AppDatabase) {
        val intent = Intent(this, LudusManagementActivity::class.java)
        intent.putExtra("db_name", name)
        startActivity(intent)
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

    @OptIn(DelicateCoroutinesApi::class)
    @Preview
    @Composable
    fun NewLudusDialogue(visibleCallback: (Boolean) -> Unit = { newDialogueVisible = it }) {
        var ludusName by remember { mutableStateOf("") }
        var databases = getAllDatabases(applicationContext)

        AlertDialog(
            onDismissRequest = {
                visibleCallback(false)
            },
            confirmButton = {
                Button(
                    onClick = {
                        Log.d(TAG, "New Ludus: $ludusName")
                        GlobalScope.launch(Dispatchers.IO) {
                            val db = createDb(ludusName, applicationContext)
                            launchLudusManagement(ludusName, db)
                        }
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

        var databases = remember { mutableStateOf(getAllDatabases(applicationContext)) }
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
                                            val db = returnDb(database, applicationContext)
                                            launchLudusManagement(database, db)
                                            visibleCallback(false)
                                        }) { Text("Load") }
                                        Button(onClick = {
                                            deleteDb(database, applicationContext)
                                            databases.value = getAllDatabases(applicationContext)
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