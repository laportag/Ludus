package com.doorxii.ludus.ui.cards

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
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
    fun CombatActionCard(combatAction: CombatAction, modifier: Modifier = Modifier){
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp


        Card(
            modifier = modifier
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
    fun CardRow(cardList: List<CombatAction>, modifier: Modifier = Modifier) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        LazyRow(
            modifier = Modifier
            .heightIn(max = screenHeight / 4)
        ) {
            items(cardList) { card ->
                CombatActionCard(combatAction = card, modifier)
            }
        }
    }


    private const val TAG = "ActionCards"
}