package com.doorxii.ludus.ui.components.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.data.models.animal.Gladiator

@Composable
fun GladiatorDisplay(
    title: String,
    gladiators: List<Gladiator>,
    selectedGladiators: List<Gladiator?> = emptyList(),
    onGladiatorSelected: (Gladiator) -> Unit,
) {
    Column() {
        Text(text = title, Modifier.padding(16.dp))
        GladiatorList(
            list = gladiators,
            onItemSelected = onGladiatorSelected,
            itemContent = { gladiator, onItemSelected ->
                GladiatorListItem(
                    gladiator = gladiator,
                    onItemSelected = onItemSelected,
                    isSelected = selectedGladiators.contains(gladiator),
                    content = { GladiatorItemContentShort(it) }
                )
            },
        )
    }
}