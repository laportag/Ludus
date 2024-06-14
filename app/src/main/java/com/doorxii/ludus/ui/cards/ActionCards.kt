package com.doorxii.ludus.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.actions.combatactions.CombatAction

object ActionCards {


    @Composable
    fun CombatActionCard(
        combatAction: CombatAction,
        modifier: Modifier = Modifier,
        enabled: Boolean = true
    ) {
//        val configuration = LocalConfiguration.current
//        val screenHeight = configuration.screenHeightDp.dp
        DragTarget(
            modifier = Modifier.fillMaxSize(),
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
                        color = Color.LightGray
                    )
                    Text(
                        combatAction.description,
                        modifier = Modifier.padding(2.dp),
                        color = Color.LightGray
                    )
                    Text(
                        combatAction.staminaCost.toString(),
                        modifier = Modifier.padding(2.dp),
                        color = if (!enabled) {
                            Color.Red
                        } else {
                            Color.LightGray
                        }
                    )
                }

            }
        }
    }

    @Composable
    fun CardRow(
        cardList: List<CombatAction>,
        modifier: Modifier = Modifier,
        playerStamina: Double
    ) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        LazyRow(
            modifier = modifier
                .heightIn(max = screenHeight * 0.30f)
//                .fillMaxSize()
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


    private const val TAG = "ActionCards"
}