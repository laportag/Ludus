package com.doorxii.ludus.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.data.models.animal.Gladiator

@Composable
fun SelectableItemMarketGladiator(
    item: Gladiator,
    onSelected: (Gladiator) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelected(item) },
        shape = MaterialTheme.shapes.small,
        tonalElevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(16.dp)) { // Apply padding to the Row
            Column(modifier = Modifier.weight(1f)) { // Make the first Column take available space
                Text(text = item.name)
                Text(text = "Height: ${item.height}")
                Text(text = "Age: ${item.age}")
            }
            Column(modifier = Modifier.weight(1f)) { // Make the second Column take available space
                Text(text = "Strength: ${item.strength}")
                Text(text = "Speed: ${item.speed}")
                Text(text = "Technique: ${item.technique}")
            }
            Button(onClick = { /* buyGladiator(item) */ }) { // Assuming buyGladiator is defined elsewhere
                Text("Buy")
            }
        }
    }
}