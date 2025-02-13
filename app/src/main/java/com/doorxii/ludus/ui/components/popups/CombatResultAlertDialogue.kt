package com.doorxii.ludus.ui.components.popups

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CombatResultAlertDialogue(
    showPopup: Boolean,
    combatReport: String,
    onDismissRequest: () -> Unit
) {
    if (showPopup) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text("Combat Report") },
            text = { CombatResultAlertContent(combatReport) },
            confirmButton = {
                Button(onClick = onDismissRequest) {
                    Text("Close")
                }
            }
        )
    }
}