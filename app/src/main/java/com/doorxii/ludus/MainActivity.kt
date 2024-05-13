package com.doorxii.ludus

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.doorxii.ludus.actions.combatactions.CombatActions
import com.doorxii.ludus.combat.Combat
import com.doorxii.ludus.data.models.beings.Gladiator
import com.doorxii.ludus.data.models.equipment.Equipment
import com.doorxii.ludus.data.models.equipment.Gladius
import com.doorxii.ludus.ui.theme.LudusTheme

class MainActivity : ComponentActivity() {

    private var combat: Combat? = null

    private var isStartGameUIVisible: Boolean = true
    private var isActionUIVisible: Boolean = false

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            LudusTheme {
//                Scaffold(
//                    topBar = { TopAppBar(title = { Text("Ludus") }) },
//                    modifier = Modifier.padding(5.dp),
//                    bottomBar = {
//                        BottomAppBar {
//                            // Bottom app bar content
//                        }
//                    },
//
//                    ) {
                    HomeScreen()
//                }
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

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            TopAppBar(title = { Text("Ludus") })
            Text("Titus vs Joseph")

            Row {
                Button(onClick = { testCombat() }, enabled = isStartGameUIVisible) { Text("Play") }
                Button(
                    onClick = { battleText = testSim()!! },
                    enabled = isStartGameUIVisible
                ) { Text("Sim") }
                Button(onClick = {
                    battleText = ""
                    testReset()
                }) { Text("Reset") }
            }
            Row {
                Button(onClick = { expanded = true }, enabled = isActionUIVisible) {
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
                    enabled = isActionUIVisible
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
            equipment = listOf<Equipment>(Gladius()),
            bloodlust = 60.0,
            height = 160.0,
            humanControlled = true
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
            equipment = listOf<Equipment>(Gladius()),
            bloodlust = 60.0,
            height = 160.0,
        )

        combat = Combat.init(listOf(marcus, joseph))
        isStartGameUIVisible = false
        return combat?.simCombat()
    }

    private fun testReset() {
        combat = null
        isStartGameUIVisible = true
        isActionUIVisible = false
    }


    private fun testCombat() {

        Log.d(TAG, "Titus attack: ${titus.attack} defence: ${titus.defence}")
        Log.d(TAG, "Joseph attack: ${joseph.attack} defence: ${joseph.defence}")

        combat = Combat.init(listOf(titus, joseph))
        isStartGameUIVisible = false
        isActionUIVisible = true
    }

    private fun submitCombatAction(choice: CombatActions): String? {
        combat?.playCombatRound(choice)
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
            equipment = listOf<Equipment>(Gladius()),
            bloodlust = 60.0,
            height = 160.0,
            humanControlled = true
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
