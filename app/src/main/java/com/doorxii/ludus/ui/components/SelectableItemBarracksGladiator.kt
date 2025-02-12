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
fun SelectableItemBarracksGladiator(
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
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.name)
                Text(text = "H: ${item.health}")
                Text(text = "S: ${item.stamina}")
                Text(text = "M: ${item.morale}")
            }
            Column {
                Button(onClick = { /*TODO*/ }) {
                    Text("Check Stats")
                }
                Button(onClick = { /* healAliveGladiators(listOf(item)) */ }) {
                    Text("Heal")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text("Sell")
                }
            }
        }
    }
}