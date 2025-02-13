package com.doorxii.ludus.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.utils.actions.combatactions.CombatAction

@Composable
fun CombatActionCard(
    combatAction: CombatAction,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    DragTarget(
        modifier = modifier,
        dataToDrop = combatAction.actionEnum,
        enabled = enabled
    ) {
        Card(
            modifier = modifier
                .aspectRatio(9f / 16f)
                .background(color = Color(0xFFD3D3D3), RoundedCornerShape(8.dp))
                .padding(4.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {

                Text(
                    combatAction.name,
                    modifier = Modifier.padding(2.dp),
                    color = Color.Black
                )
                Text(
                    combatAction.description,
                    modifier = Modifier.padding(2.dp),
                    color = Color.Black
                )
                Text(
                    combatAction.staminaCost.toString(),
                    modifier = Modifier.padding(2.dp),
                    color = if (!enabled) {
                        Color.Red
                    } else {
                        Color.Black
                    }
                )
            }

        }
    }
}