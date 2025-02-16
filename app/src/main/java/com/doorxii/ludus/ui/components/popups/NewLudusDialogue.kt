package com.doorxii.ludus.ui.components.popups

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.utils.data.DatabaseManagement
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun NewLudusDialogue(
    visibleCallback: (Boolean) -> Unit,
    launchLudusManagement: (String, AppDatabase) -> Unit
) {
    var ludusName by remember { mutableStateOf("") }
    val context = LocalContext.current
    val TAG = "NewLudusDialogue"

    AlertDialog(
        onDismissRequest = {
            visibleCallback(false)
        },
        confirmButton = {
            Button(
                onClick = {
                    Log.d(TAG, "New Ludus: $ludusName")
                    GlobalScope.launch(Dispatchers.IO) {
                        val db = DatabaseManagement.createDb(ludusName, context)
                        launchLudusManagement(ludusName, db)
                    }
//                    visibleCallback(false)
                },
                enabled = ludusName.isNotEmpty() && ludusName !in DatabaseManagement.getAllDatabases(context)
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(onClick = {
                visibleCallback(false)
                Log.d(TAG, "Cancel button")
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