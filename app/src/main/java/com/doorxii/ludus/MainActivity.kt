package com.doorxii.ludus

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
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.equipment.Equipment
import com.doorxii.ludus.data.models.equipment.armour.LightArmour
import com.doorxii.ludus.data.models.equipment.weapon.Gladius
import com.doorxii.ludus.ui.theme.LudusTheme
import com.doorxii.ludus.utils.CombatSerialization.returnCombatFile
import com.doorxii.ludus.utils.CombatSerialization.saveCombatJson
import kotlinx.serialization.json.Json
import java.io.File

class MainActivity : androidx.activity.ComponentActivity() {

    private var combat: Combat? = null
    var gladiatorList = mutableListOf<Gladiator>()

    private var isStartGameUIEnabled: Boolean = true
    private var isActionUIEnabled: Boolean = false

    lateinit var combatFile: File

    var text = mutableStateOf("TEST TEXT")
    var titus = Gladiator()
    var joseph = Gladiator()

    val combatResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d(TAG, "Combat result: ${result.toString()}")
                val resultUri: Uri = result.data?.data!!
                val resFile = File(resultUri.path!!)
                val resCombat = Json.decodeFromString<Combat>(resFile.readText())
                Log.d(TAG, "Combat complete?: ${resCombat.isComplete.toString()}")
                val winner = resCombat.gladiatorList[0]
                Log.d(TAG, "Winner: ${winner.name}")
                text.value = "Winner: ${winner.name}"
            }
        }


    fun setGladiators() {
        titus.name = "Titus"
        titus.age = 21.0
        titus.strength = 75.0
        titus.speed = 60.0
        titus.technique = 90.0
        titus.morale = 100.0
        titus.health = 100.0
        titus.stamina = 100.0
        titus.equipment = Equipment()
            .apply {
                weapon = Gladius()
                armour = LightArmour()
            }
        titus.bloodlust = 60.0
        titus.height = 160.0
        titus.humanControlled = true
        titus.id = 1

        joseph.name = "Joseph"
        joseph.age = 34.0
        joseph.strength = 75.0
        joseph.speed = 60.0
        joseph.technique = 40.0
        joseph.morale = 100.0
        joseph.health = 100.0
        joseph.stamina = 100.0
        joseph.equipment = Equipment()
        joseph.bloodlust = 60.0
        joseph.height = 160.0
        joseph.id = 2

        gladiatorList = listOf(titus, joseph).toMutableList()
    }

    fun startCombatActivity(gladiatorList: List<Gladiator>) {
        combatFile = returnCombatFile(applicationContext)
        combat = Combat.init(gladiatorList)
        saveCombatJson(combat!!, combatFile)
        Log.d(TAG, "combatfile: "+ combatFile.readText())
        val uri = combatFile.toUri()
        val intent = Intent(this, CombatActivity::class.java)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        intent.putExtra("combatFileUri", uri)
        intent.setDataAndType(uri, contentResolver.getType(uri))
        combatResultLauncher.launch(intent)
        startActivity(intent)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setGladiators()

        combatFile = returnCombatFile(context = applicationContext)

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
                    startCombatActivity(gladiatorList)
                },
                enabled = isStartGameUIEnabled
            ) {
                Text("Start")
            }
            TextField(
                value = text.value,
                onValueChange = {},
                minLines = 15,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            )
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}
