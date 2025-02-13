package com.doorxii.ludus.ui.components.cards

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.doorxii.ludus.utils.actions.combatactions.CombatAction

@Composable
fun CardRow(
    cardList: List<CombatAction>,
    modifier: Modifier = Modifier,
    playerStamina: Double
) {
    LazyRow(
        modifier = modifier
    ) {
        items(cardList) { card ->
            CombatActionCard(
                combatAction = card,
                modifier,
                enabled = card.staminaCost <= playerStamina
            )
        }
    }
}