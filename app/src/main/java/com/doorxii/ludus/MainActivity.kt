package com.doorxii.ludus

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.doorxii.ludus.actions.combatactions.CombatActions
import com.doorxii.ludus.combat.Combat
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.equipment.Equipment
import com.doorxii.ludus.data.models.equipment.weapon.Gladius
import com.doorxii.ludus.ui.theme.LudusTheme

class MainActivity : ComponentActivity() {

    private var combat: Combat? = null

    private var isStartGameUIEnabled: Boolean = true
    private var isActionUIEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            LudusTheme {
                HomeScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun HomeScreen() {
        var battleText: String by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }
        val combatActions =
            listOf(CombatActions.BASIC_ATTACK, CombatActions.TIRED_ATTACK, CombatActions.WAIT)
        var choice: CombatActions? by remember {
            mutableStateOf(null)
        }
        var buttonText: String by remember { mutableStateOf("Action Choice") }
        val scrollState = rememberScrollState()
        var isActionUIEnabledRemember: Boolean by remember { mutableStateOf(isActionUIEnabled) }
        var isStartGameUIEnabledRemember: Boolean by remember { mutableStateOf(isStartGameUIEnabled) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            TopAppBar(title = { Text("Ludus") })
            Text("Titus vs Joseph")

            Row {
                Button(
                    onClick = {
                        combat = startGame()
                        isActionUIEnabledRemember = true
                        isStartGameUIEnabledRemember = false
                    },
                    enabled = isStartGameUIEnabledRemember
                ) {
                    Text("Play")
                }

                Button(
                    onClick = {
                        isActionUIEnabledRemember = false
                        isStartGameUIEnabledRemember = false
                        battleText = testSim()!!
                    },
                    enabled = isStartGameUIEnabledRemember
                ) { Text("Sim") }

                Button(onClick = {
                    battleText = ""
                    isActionUIEnabledRemember = false
                    isStartGameUIEnabledRemember = true
                    testReset()
                }) { Text("Reset") }
            }
            Row {
                Button(onClick = { expanded = true }, enabled = isActionUIEnabledRemember) {
                    Text(buttonText)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (action in combatActions) {
                        DropdownMenuItem(text = { Text(action.name) }, onClick = {
                            choice = action
                            buttonText = action.name
                            expanded = false
                        })
                    }
                }

                Button(
                    onClick = { battleText = submitCombatAction(choice!!)!! },
                    enabled = isActionUIEnabledRemember
                ) {
                    Text("Submit")
                }
            }
            TextField(
                value = battleText,
                onValueChange = {},
                minLines = 15,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            )
        }
    }

    private fun testSim(): String? {

        val marcus = Gladiator(
            name = "Marcus",
            age = 21.0,
            strength = 75.0,
            speed = 60.0,
            technique = 90.0,
            morale = 100.0,
            health = 100.0,
            stamina = 100.0,
            equipment = Equipment(Gladius()),
            bloodlust = 60.0,
            height = 160.0,
            humanControlled = true,
            id =1
        )

        val joseph = Gladiator(
            name = "Joshua",
            age = 34.0,
            strength = 75.0,
            speed = 60.0,
            technique = 40.0,
            morale = 800.0,
            health = 100.0,
            stamina = 100.0,
            equipment = Equipment(),
            bloodlust = 60.0,
            height = 160.0,
            id = 2
        )

        combat = Combat.init(listOf(marcus, joseph))
        return combat?.simCombat()
    }

    private fun testReset() {
        combat = null
    }


    private fun startGame(): Combat {
        combat = Combat.init(listOf(titus, joseph))
        Log.d(TAG, "startGame: ${combat?.combatReport}")
        return combat!!
    }

    private fun submitCombatAction(choice: CombatActions): String? {
        if (combat!!.isComplete) {
            Log.d(TAG, "Combat over")
            isActionUIEnabled = false
        } else {
            val report = combat?.playCombatRound(choice)
            if (report!!.isComplete) {
                Log.d(TAG, "Combat over")
                isActionUIEnabled = false
            }
        }
        return combat?.combatReport
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
            equipment = Equipment(Gladius()),
            bloodlust = 60.0,
            height = 160.0,
            humanControlled = true,
            id = 1
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
            equipment = Equipment(),
            bloodlust = 60.0,
            height = 160.0,
            id = 2
        )
    }

}
