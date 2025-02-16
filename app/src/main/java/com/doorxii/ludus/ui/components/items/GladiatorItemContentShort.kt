package com.doorxii.ludus.ui.components.items

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.doorxii.ludus.data.models.animal.Gladiator

@Composable
fun GladiatorItemContentShort(gladiator: Gladiator) {
    Text(text = gladiator.name)
}