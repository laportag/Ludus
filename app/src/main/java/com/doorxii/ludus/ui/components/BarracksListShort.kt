package com.doorxii.ludus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.data.models.animal.Gladiator

@Composable
fun BarracksListShort(
    list: List<Gladiator>,
    selectedGladiators: List<Gladiator?>, // Add selectedGladiators parameter
    onItemSelected: (Gladiator) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(list) { gladiator ->
            val isSelected = selectedGladiators.contains(gladiator) // Check if selected
            SelectableItemShort(
                gladiator,
                isSelected, // Pass isSelected state
                onItemSelected
            )
        }
    }
}