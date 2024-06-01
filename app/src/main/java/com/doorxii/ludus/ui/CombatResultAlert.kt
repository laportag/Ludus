package com.doorxii.ludus.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.combat.Combat

class CombatResultAlert {

    @Composable
    fun CombatResultAlertDialog(
        showPopup: Boolean,
        combat: Combat,
        onDismissRequest: () -> Unit
    ) {
        if (showPopup) {
            AlertDialog(
                onDismissRequest = onDismissRequest,
                title = { Text("Combat Report") },
                text = { CombatResultAlertContent(combat) },
                confirmButton = {
                    Button(onClick = onDismissRequest) {
                        Text("Close")
                    }
                }
            )
        }
    }

    @Composable
    fun CombatResultAlertContent(combat: Combat) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Combat Report",
                style = MaterialTheme.typography.headlineSmall
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(1) { // Only one item for the report text
                    Text(
                        text = combat.combatReport,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

        }
    }

}