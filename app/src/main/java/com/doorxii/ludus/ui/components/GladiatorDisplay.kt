package com.doorxii.ludus.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.data.models.animal.Gladiator

@Composable
fun GladiatorDisplay(
    title: String,
    gladiators: List<Gladiator>,
    selectedGladiators: List<Gladiator?> = emptyList(),
    onGladiatorSelected: (Gladiator) -> Unit,
) {
    Column(
        Modifier
            .width(LocalConfiguration.current.screenWidthDp.dp / 2)
    ) {
        Text(text = title)
        // Use LazyVerticalGrid here
        BarracksListShort(
            list = gladiators,
            selectedGladiators = selectedGladiators, // Pass the Set here
            onItemSelected = onGladiatorSelected
        )
    }
}