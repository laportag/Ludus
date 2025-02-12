package com.doorxii.ludus.ui.components.cards

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.data.models.animal.Gladiator

@Composable
fun CombatGladiatorCard(gladiator: Gladiator){
    val decimalFormat = DecimalFormat("0.##")

    Card(
        modifier = Modifier
//            .aspectRatio(7f / 16f)
            .background(color = Color(0xFFD3D3D3), RoundedCornerShape(8.dp))
            .fillMaxSize()
//            .wrapContentSize()
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp)
    ){
        Column(
            modifier = Modifier.padding(1.dp)
        ){
            Text(text = "${gladiator.name} : #${gladiator.id}")
            Row {
                Column {
                    Text("H: ${gladiator.health}")
                    Text("S: ${gladiator.stamina}")
                    Text("M: ${gladiator.morale}")
//                    Text("At: ${decimalFormat.format(gladiator.attack)}")
//                    Text("De: ${decimalFormat.format(gladiator.defence)}")
                }
                Column {
                    Text("St: ${gladiator.strength}")
                    Text("Sp: ${gladiator.speed}")
                    Text("Te: ${gladiator.technique}")
//                    Text("Bl: ${gladiator.bloodlust}")
                }
                Text("pic")

            }
        }
    }
}