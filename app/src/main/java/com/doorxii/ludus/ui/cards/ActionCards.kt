package com.doorxii.ludus.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.actions.Action
import com.doorxii.ludus.actions.combatactions.BasicAttack
import com.doorxii.ludus.actions.combatactions.CombatAction
import com.doorxii.ludus.actions.combatactions.CombatActions
import com.doorxii.ludus.actions.combatactions.TiredAttack
import com.doorxii.ludus.actions.combatactions.Wait

object ActionCard {



    @Composable
    fun CombatActionCard(combatAction: CombatAction){
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        Card(
            modifier = Modifier
                .aspectRatio(9f / 16f)
                .background(color = Color(0xFFD3D3D3), RoundedCornerShape(8.dp))
                .height(screenHeight / 4)
                .padding(4.dp),
            shape = RoundedCornerShape(8.dp)
        ){
            Column(
                modifier = Modifier.padding(8.dp)
            ){
                Text(combatAction.name)
                Text(combatAction.description)
                Text(combatAction.staminaCost.toString())
            }
        }
    }

    @Composable
    fun CardRow(cardList: List<CombatAction>) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        LazyRow(
            modifier = Modifier.heightIn(max = screenHeight / 4)
        ) {
            items(cardList) { card ->
                CombatActionCard(combatAction = card)
            }
        }
    }

}