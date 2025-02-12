package com.doorxii.ludus.ui.components.bars

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun InitiateCombatBar(
    onStartCombat: () -> Unit,
    onSimCombat: () -> Unit,
    isEnabled: Boolean
) {
    Row {
        Button(onClick = onStartCombat, enabled = isEnabled) {
            Text("Start Combat")
        }
        Button(onClick = onSimCombat, enabled = isEnabled) {
            Text("Sim Combat")
        }
    }
}