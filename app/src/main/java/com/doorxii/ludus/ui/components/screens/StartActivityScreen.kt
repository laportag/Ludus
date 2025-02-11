package com.doorxii.ludus.ui.components.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.ui.activities.StartActivityViewModel
import com.doorxii.ludus.ui.components.LoadLudusDialogue
import com.doorxii.ludus.ui.components.NewLudusDialogue

@Composable
fun StartActivityScreen(
    newDialogueState: MutableState<Boolean>,
    loadDialogueState: MutableState<Boolean>,
    databases: List<String>,
    launchLudusManagement: (String, AppDatabase) -> Unit,
    viewModel: StartActivityViewModel
) {

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
        NewLudusDialogue(
            visibleCallback = { newDialogueState.value = it },
            launchLudusManagement = { ludus, db ->
                launchLudusManagement(ludus, db)
            }
        )
    }
    if (loadDialogueState.value) {

        LoadLudusDialogue(
            viewModel = viewModel,
            visibleCallback = { loadDialogueState.value = it },
            launchLudusManagement = { ludus, db ->
                launchLudusManagement(ludus, db)
            }
        )
    }
}