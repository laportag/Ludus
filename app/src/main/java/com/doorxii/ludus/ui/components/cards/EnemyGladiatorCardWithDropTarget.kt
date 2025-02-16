package com.doorxii.ludus.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.doorxii.ludus.data.models.actions.combatactions.CombatActions
import com.doorxii.ludus.data.models.animal.Gladiator

@Composable
fun EnemyGladiatorCardWithDropTarget(
    enemyGladiator: Gladiator,
    onActionDropped: (CombatActions, Gladiator) -> Unit
) {
    DropTarget<CombatActions>(modifier = Modifier.fillMaxWidth()) { gladiatorIsInBound, combatAction ->
        val gladiatorBgColor = if (gladiatorIsInBound) {
            Color.Yellow // Highlight when action card is over gladiator card
        } else {
            Color.White
        }
        // Callback to handle the dropped action
        if (combatAction != null) {
            onActionDropped(combatAction, enemyGladiator)
        }
        Box(modifier = Modifier.background(gladiatorBgColor)) {
            CombatGladiatorCard(enemyGladiator)
        }

    }
}