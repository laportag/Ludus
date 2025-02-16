package com.doorxii.ludus.ui.components.bars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.doorxii.ludus.data.models.ludus.Ludus

@Composable
fun LudusSelectionBar(
    selectedEnemyLudus: Ludus?,
    ludiExcludingPlayer: List<Ludus>,
    onLudusSelected: (Ludus) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
//            .fillMaxSize()
            .clickable { expanded = true }
    ) {
        Text(
            text = selectedEnemyLudus?.name ?: "Select Enemy Ludus",
            modifier = Modifier.align(Alignment.Center)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.align(Alignment.Center)
        ) {
            ludiExcludingPlayer.forEach { ludus ->
                DropdownMenuItem(
                    text = { Text(text = ludus.name) },
                    onClick = {
                        expanded = false
                        onLudusSelected(ludus)
                    }
                )
            }
        }

    }
}