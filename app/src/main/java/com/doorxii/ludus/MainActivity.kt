package com.doorxii.ludus

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.core.net.toUri
import com.doorxii.ludus.combat.Combat
import com.doorxii.ludus.combat.CombatActivity
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.equipment.Equipment
import com.doorxii.ludus.data.models.equipment.weapon.Gladius
import com.doorxii.ludus.ui.theme.LudusTheme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class MainActivity : androidx.activity.ComponentActivity() {

    private var combat: Combat? = null
    var gladiatorList = mutableListOf<Gladiator>()

    private var isStartGameUIEnabled: Boolean = true
    private var isActionUIEnabled: Boolean = false

    lateinit var combatFile: File

    var text = ""

    val combatResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d(TAG, "Combat result: ${result.toString()}")
                val resultUri: Uri = result.data?.data!!
                val inputStream = contentResolver.openInputStream(resultUri)
                text = inputStream.toString()
            }
        }

    fun returnCombatFile(): File {
        val context = applicationContext
        val path = File(context.filesDir, "combat/combat.json")
        val exists = path.exists()
        if (!exists) {
            path.mkdirs()
            path.createNewFile()
        }
        return path
    }


    fun startCombatActivity(gladiatorList: List<Gladiator>) {
        val uri = combatFile.toUri()
        val intent = Intent(this, CombatActivity::class.java)
        intent.putExtra("combatFileUri", uri)
        combatResultLauncher.launch(intent)
        startActivity(intent)
    }

    fun saveCombatJson(combat: Combat) {
        Log.d(TAG, "saving combat to file...")
        val jsonString = Json.encodeToString(combat)
        combatFile.writeText(jsonString)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        combatFile = returnCombatFile()

        enableEdgeToEdge()
        setContent {

            LudusTheme {
                HomeScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
//    @Preview
    fun HomeScreen() {
        var text by remember { mutableStateOf(text) }
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TopAppBar(title = { Text("Ludus") })
            Button(
                onClick = {
                    startCombatActivity(listOf(titus, joseph))
                },
                enabled = isStartGameUIEnabled
            ) {
                Text("Start")
            }
            TextField(
                value = text,
                onValueChange = {},
                minLines = 15,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)

            )
        }
    }

//    @OptIn(ExperimentalMaterial3Api::class)
//    @Preview
//    @Composable
//    fun HomeScreen() {
//        var battleText: String by remember { mutableStateOf("") }
//        var expanded by remember { mutableStateOf(false) }
//        val combatActions =
//            listOf(CombatActions.BASIC_ATTACK, CombatActions.TIRED_ATTACK, CombatActions.WAIT)
//        var choice: CombatActions? by remember {
//            mutableStateOf(null)
//        }
//        var buttonText: String by remember { mutableStateOf("Action Choice") }
//        val scrollState = rememberScrollState()
//        var isActionUIEnabledRemember: Boolean by remember { mutableStateOf(isActionUIEnabled) }
//        var isStartGameUIEnabledRemember: Boolean by remember { mutableStateOf(isStartGameUIEnabled) }
//
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//
//        ) {
//            TopAppBar(title = { Text("Ludus") })
//            Text("Titus vs Joseph")
//
//            Row {
//                Button(
//                    onClick = {
//                        combat = startGame()
//                        isActionUIEnabledRemember = true
//                        isStartGameUIEnabledRemember = false
//                    },
//                    enabled = isStartGameUIEnabledRemember
//                ) {
//                    Text("Play")
//                }
//
//                Button(
//                    onClick = {
//                        isActionUIEnabledRemember = false
//                        isStartGameUIEnabledRemember = false
//                        battleText = testSim()!!
//                    },
//                    enabled = isStartGameUIEnabledRemember
//                ) { Text("Sim") }
//
//                Button(onClick = {
//                    battleText = ""
//                    isActionUIEnabledRemember = false
//                    isStartGameUIEnabledRemember = true
//                    testReset()
//                }) { Text("Reset") }
//            }
//            Row {
//                Button(onClick = { expanded = true }, enabled = isActionUIEnabledRemember) {
//                    Text(buttonText)
//                }
//                DropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    for (action in combatActions) {
//                        DropdownMenuItem(text = { Text(action.name) }, onClick = {
//                            choice = action
//                            buttonText = action.name
//                            expanded = false
//                        })
//                    }
//                }
//
//                Button(
//                    onClick = { battleText = submitCombatAction(choice!!)!! },
//                    enabled = isActionUIEnabledRemember
//                ) {
//                    Text("Submit")
//                }
//            }
//            val combatActions = listOf<CombatAction>(BasicAttack(), TiredAttack(), Wait())
//            CardRow(combatActions)
//            TextField(
//                value = battleText,
//                onValueChange = {},
//                minLines = 15,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(scrollState)
//
//            )
//        }
//    }

//    private fun testSim(): String? {
//
//        val marcus = Gladiator(
//            name = "Marcus",
//            age = 21.0,
//            strength = 75.0,
//            speed = 60.0,
//            technique = 90.0,
//            morale = 100.0,
//            health = 100.0,
//            stamina = 100.0,
//            equipment = Equipment(Gladius(id = 1)),
//            bloodlust = 60.0,
//            height = 160.0,
//            humanControlled = true,
//            id = 1
//        )
//
//        val joseph = Gladiator(
//            name = "Joshua",
//            age = 34.0,
//            strength = 75.0,
//            speed = 60.0,
//            technique = 40.0,
//            morale = 800.0,
//            health = 100.0,
//            stamina = 100.0,
//            equipment = Equipment(),
//            bloodlust = 60.0,
//            height = 160.0,
//            id = 2
//        )
//
//        combat = Combat.init(listOf(marcus, joseph))
//        return combat?.simCombat()
//    }
//
//    private fun testReset() {
//        combat = null
//    }


//    private fun startGame(): Combat {
//        gladiatorList = mutableListOf(titus, joseph)
//        Log.d(TAG, "startGame: ${combat?.combatReport}")
//        CombatActivity(combat!!).startActivities()
//        return combat!!
//    }

//    private fun submitCombatAction(choice: CombatActions): String? {
//        if (combat!!.isComplete) {
//            Log.d(TAG, "Combat over")
//            isActionUIEnabled = false
//        } else {
//            val report = combat?.playCombatRound(choice)
//            if (report!!.isComplete) {
//                Log.d(TAG, "Combat over")
//                isActionUIEnabled = false
//            }
//        }
//        return combat?.combatReport
//    }

//    fun startCombat() {
//
//    }


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
            equipment = Equipment(Gladius(id = 1)),
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
