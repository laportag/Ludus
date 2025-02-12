package com.doorxii.ludus.ui.components.cards

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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

    private val decimalFormat = DecimalFormat("0.##")

    @Composable
    fun CombatGladiatorCard(gladiator: Gladiator){
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp

        Card(
            modifier = Modifier
                .aspectRatio(16f / 7f)
                .background(color = Color(0xFFD3D3D3), RoundedCornerShape(8.dp))
                .wrapContentSize()
                .padding(2.dp),
            shape = RoundedCornerShape(8.dp)
        ){
            Column(
                modifier = Modifier.padding(1.dp)
            ){
                Text(text = "${gladiator.name} : #${gladiator.id}")
                Row {
                    Column {
                        Text("H: ${gladiator.health}/100")
                        Text("S: ${gladiator.stamina}/100")
                        Text("M: ${gladiator.morale}/100")
                        Text("At: ${decimalFormat.format(gladiator.attack)}")
                        Text("De: ${decimalFormat.format(gladiator.defence)}")
                    }
                    Column {
                        Text("St: ${gladiator.strength}/100")
                        Text("Sp: ${gladiator.speed}/100")
                        Text("Te: ${gladiator.technique}/100")
                        Text("Bl: ${gladiator.bloodlust}/100")
                    }
                    Text("pic")

                }
            }
        }
    }
}