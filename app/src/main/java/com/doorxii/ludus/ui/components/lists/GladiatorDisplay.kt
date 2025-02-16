package com.doorxii.ludus.ui.components.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.components.items.GladiatorItemContentShort
import com.doorxii.ludus.ui.components.items.GladiatorListItem

@Composable
fun GladiatorDisplay(
    title: @Composable () -> Unit,
    gladiators: List<Gladiator>,
    selectedGladiators: List<Gladiator?> = emptyList(),
    onGladiatorSelected: (Gladiator) -> Unit,
) {
    Column() {
        title()
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