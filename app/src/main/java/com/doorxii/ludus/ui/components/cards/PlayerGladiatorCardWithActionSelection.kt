package com.doorxii.ludus.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.activities.CombatActivityViewModel
import com.doorxii.ludus.ui.components.cards.GladiatorCards

@Composable
fun PlayerGladiatorCardWithActionSelection(
    playerGladiator: Gladiator,
    isCurrentTurn: Boolean,
    viewModel: CombatActivityViewModel,
    onGladiatorClicked: (Gladiator) -> Unit
) {
    val gladiatorBgColor = if (isCurrentTurn) {
        Color.Green // Highlight current player gladiator
    } else {
        Color.White
    }
    val hadTurn = viewModel.hasGladiatorHadATurn(playerGladiator)
    Box(modifier = Modifier
        .background(gladiatorBgColor)
        .graphicsLayer {
            alpha = if (hadTurn) 0.5f else 1f
        }
        .clickable (enabled = !hadTurn) {
            onGladiatorClicked(playerGladiator)
        }
    ) {
        GladiatorCards.CombatGladiatorCard(playerGladiator)
    }
}