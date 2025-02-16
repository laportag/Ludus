package com.doorxii.ludus.ui.components.items

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.doorxii.ludus.data.models.animal.Gladiator

@Composable
fun GladiatorItemContentBarracks(gladiator: Gladiator) {
    Text(text = gladiator.name)
    Text(text = "H: ${gladiator.health}")
    Text(text = "S: ${gladiator.stamina}")
    Text(text = "M: ${gladiator.morale}")
}