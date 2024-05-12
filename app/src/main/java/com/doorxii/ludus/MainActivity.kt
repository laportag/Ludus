package com.doorxii.ludus

import CombatDropDownAndButton
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.doorxii.ludus.combat.Combat
import com.doorxii.ludus.data.models.beings.Gladiator
import com.doorxii.ludus.data.models.equipment.Equipment
import com.doorxii.ludus.data.models.equipment.Gladius
import com.doorxii.ludus.ui.theme.LudusTheme

class MainActivity : ComponentActivity() {

    var combat: Combat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LudusTheme {
                HomeScreen()
            }
        }
    }

    @Preview
    @Composable
    fun HomeScreen() {
        var battleText: String by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Text("Titus vs Joseph")

            Row {
                Button(onClick = { testCombat() }) { Text("Play")}
                Button(onClick = { battleText = testSim()!! }) { Text("Sim")}
                Button(onClick = { testReset() }) { Text("Reset")}
            }
            CombatDropDownAndButton()
        }


        Text(battleText, minLines = 4)

    }

    fun testSim(): String? {
        combat = Combat.init(listOf(titus, joseph))
        return combat?.simCombat()
    }

    fun testReset(){
        combat = null
    }


    fun testCombat() {


        Log.d(TAG, "Titus attack: ${titus.attack} defence: ${titus.defence}")
        Log.d(TAG, "Joseph attack: ${joseph.attack} defence: ${joseph.defence}")

        combat = Combat.init(listOf(titus, joseph))

    }


    companion object {
        private const val TAG = "MainActivity"

        var titus = Gladiator(
            name = "Titus",
            age = 21.0,
            strength = 75.0,
            speed = 60.0,
            technique = 90.0,
            morale = 100.0,
            health = 100.0,
            stamina = 100.0,
            equipment = listOf<Equipment>(Gladius()),
            bloodlust = 60.0,
            height = 160.0,
        )

        var joseph = Gladiator(
            name = "Joseph",
            age = 34.0,
            strength = 75.0,
            speed = 60.0,
            technique = 40.0,
            morale = 800.0,
            health = 100.0,
            stamina = 100.0,
            equipment = listOf<Equipment>(Gladius()),
            bloodlust = 60.0,
            height = 160.0,
        )
    }

}
