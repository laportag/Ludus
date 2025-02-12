package com.doorxii.ludus.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.ludus.Ludus

@Composable
fun EnemyGladiatorsDisplay(
    selectedEnemyLudus: Ludus?,
    enemyGladiators: List<Gladiator>,
    selectedEnemyGladiators: List<Gladiator?>,
    onGladiatorSelected: (Gladiator) -> Unit
) {
    Column(
        Modifier
            .width(LocalConfiguration.current.screenWidthDp.dp / 2) // Assuming you have screenWidth available
    ) {
        Text(text = selectedEnemyLudus?.name ?: "Enemy")
        BarracksListShort(
            list = enemyGladiators,
            selectedGladiators = selectedEnemyGladiators,
            onItemSelected = onGladiatorSelected
        )
    }
}