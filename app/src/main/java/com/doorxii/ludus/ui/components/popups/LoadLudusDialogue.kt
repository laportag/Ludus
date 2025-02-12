package com.doorxii.ludus.ui.components.popups

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.ui.activities.StartActivityViewModel
import com.doorxii.ludus.utils.DatabaseManagement

@Composable
fun LoadLudusDialogue(
    viewModel: StartActivityViewModel,
    launchLudusManagement: (String, AppDatabase) -> Unit,
    visibleCallback: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val TAG = "LoadLudusDialogue"
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
                items(DatabaseManagement.getAllDatabases(context)) { database ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedDb.value = database },
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
                                color = if (selectedDb.value == database) {
                                    Color.LightGray
                                } else {
                                    Color.Transparent
                                }
                            ) {
                                Text(database)
                            }
                            Column {
                                Row {
                                    Button(onClick = {
                                        val db = DatabaseManagement.returnDb(
                                            database,
                                            context
                                        )
                                        launchLudusManagement(database, db)
                                        visibleCallback(false)
                                    }) { Text("Load") }
                                    Button(onClick = {
                                        Log.d(TAG, "deleting: $database")
                                        DatabaseManagement.deleteDb(database, context) {
                                            if(it){
                                                Log.d(TAG, "deleted: $database")
                                                viewModel.setDatabases(
                                                    DatabaseManagement.getAllDatabases(
                                                        context
                                                    )
                                                )
                                            }
                                        }
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