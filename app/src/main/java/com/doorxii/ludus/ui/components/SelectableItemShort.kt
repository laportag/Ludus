package com.doorxii.ludus.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.data.models.animal.Gladiator

@Composable
fun SelectableItemShort(
    item: Gladiator,
    isSelected: Boolean,
    onSelected: (Gladiator) -> Unit
) {
    val backgroundColour = if (isSelected) Color.LightGray else Color.Transparent

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelected(item) },
        shape = MaterialTheme.shapes.small,
        tonalElevation = 4.dp,
        color = backgroundColour
    ) {
        Text(
            text = item.name,
            modifier = Modifier.padding(16.dp)
        )
    }
}