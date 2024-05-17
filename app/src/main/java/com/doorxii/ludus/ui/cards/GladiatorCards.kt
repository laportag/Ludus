package com.doorxii.ludus.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.data.models.animal.Gladiator

object GladiatorCards {

    @Composable
    fun CombatGladiatorCard(gladiator: Gladiator){
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp

        Card(
            modifier = Modifier
                .aspectRatio(16f / 9f)
                .background(color = Color(0xFFD3D3D3), RoundedCornerShape(8.dp))
                .height(screenHeight / 5)
                .padding(4.dp),
            shape = RoundedCornerShape(8.dp)
        ){
            Column{
                Text(text = gladiator.name)
                Row {
                    Column {
                        Text("H: ${gladiator.health}/100")
                        Text("S: ${gladiator.stamina}/100")
                        Text("M: ${gladiator.morale}/100")
                        Text("A: ${gladiator.attack}")
                        Text("D: ${gladiator.defence}")
                    }
                    Column {
                        Text("S: ${gladiator.strength}/100")
                        Text("Sp: ${gladiator.speed}/100")
                        Text("T: ${gladiator.technique}/100")
                        Text("B: ${gladiator.bloodlust}/100")
                    }
                    Text("pic")

                }
            }
        }
    }
}