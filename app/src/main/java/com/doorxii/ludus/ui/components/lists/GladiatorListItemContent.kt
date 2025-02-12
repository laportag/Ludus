package com.doorxii.ludus.ui.components.lists

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

@Composable
fun GladiatorItemContentMarket(gladiator: Gladiator) {
    Text(text = gladiator.name)
    Text(text = "Height: ${gladiator.height}")
    Text(text = "Age: ${gladiator.age}")
    Text(text = "Strength: ${gladiator.strength}")
    Text(text = "Speed: ${gladiator.speed}")
    Text(text = "Technique: ${gladiator.technique}")
}

@Composable
fun GladiatorItemContentShort(gladiator: Gladiator) {
    Text(text = gladiator.name)
}