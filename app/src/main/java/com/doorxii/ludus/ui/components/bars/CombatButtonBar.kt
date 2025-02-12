package com.doorxii.ludus.ui.components.bars

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CombatButtonBar(showDialog: () -> Unit) {
    Row() {
        Button(onClick = { showDialog() }) {
            Text(text = "Show Battle Report")
        }
    }
}