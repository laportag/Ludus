package com.doorxii.ludus.ui.components.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.data.models.animal.Gladiator

@Composable
fun GladiatorListItem(
    gladiator: Gladiator,
    onItemSelected: (Gladiator) -> Unit,
    isSelected: Boolean = false,
    buttons: @Composable () -> Unit = {},
    content: @Composable (Gladiator) -> Unit
) {
    val backgroundColour = if (isSelected) Color.LightGray else Color.Transparent
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemSelected(gladiator) },
        shape = MaterialTheme.shapes.small,
        tonalElevation = 4.dp,
        color = backgroundColour
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                content(gladiator)
            }
            buttons()
        }
    }
}