package com.doorxii.ludus.ui.components.items

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.doorxii.ludus.data.models.animal.Gladiator

@Composable
fun GladiatorItemContentMarket(gladiator: Gladiator) {
    Text(text = gladiator.name)
    Text(text = "Height: ${gladiator.height}")
    Text(text = "Age: ${gladiator.age}")
    Text(text = "Strength: ${gladiator.strength}")
    Text(text = "Speed: ${gladiator.speed}")
    Text(text = "Technique: ${gladiator.technique}")
}