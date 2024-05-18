package com.doorxii.ludus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.actions.combatactions.CombatActions
import com.doorxii.ludus.combat.Combat
import com.doorxii.ludus.ui.cards.ActionCards
import com.doorxii.ludus.ui.cards.GladiatorCards
import com.doorxii.ludus.ui.theme.LudusTheme
import com.doorxii.ludus.utils.EnumToAction.combatEnumListToActionList
import kotlinx.serialization.json.Json
import java.io.File
import android.util.Log
import androidx.compose.ui.tooling.preview.Preview

class CombatActivity() : ComponentActivity() {

    var combat: Combat? = null
//    var combatState: Combat by mutableStateOf(combat)

//    val gladiatorList = intent.getParcelableExtra("gladiatorList", List<Gladiator>::class.java)
//    val combat = Combat.init(gladiatorList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        combat = readCombatFromJson()
        Log.d(TAG, "combat null?: " + combat!!.gladiatorList)

        enableEdgeToEdge()
        setContent {
            LudusTheme {
                CombatLayout()
            }
        }
    }

    fun readCombatFromJson(): Combat {
//        val combatFile = filesDir.resolve("combat.json")
        val uri = intent.data
        val combatFile = File(uri!!.path!!)
        val combatJson = combatFile.readText()
        val combat = Json.decodeFromString<Combat>(combatJson)
        return combat
    }

    override fun finishActivity(requestCode: Int) {
        super.finishActivity(requestCode)
    }


    @Preview
    @Composable
    fun CombatLayout() {

        var battleText: String by remember { mutableStateOf("") }

        var expanded by remember { mutableStateOf(false) }

        val combatActions =
            listOf(CombatActions.BASIC_ATTACK, CombatActions.TIRED_ATTACK, CombatActions.WAIT)
        var choice: CombatActions? by remember {
            mutableStateOf(null)
        }

        var buttonText: String by remember { mutableStateOf("Action Choice") }
        val scrollState = rememberScrollState()

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
//            TopAppBar(title = { Text(combat.combatName) })

            val dropZone = Column (
                modifier = Modifier.height(screenHeight * 0.65f)
            ) {
                // player card
                GladiatorCards.CombatGladiatorCard(combat!!.gladiatorList[0])
                // enemy card
                GladiatorCards.CombatGladiatorCard(combat!!.gladiatorList[1])


                TextField(
                    value = battleText,
                    onValueChange = {},
                    modifier = Modifier
//                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .aspectRatio(16f / 9f)
                        .height(screenHeight * 0.2f)
                )
            }
            val state = rememberDraggableState {

            }
            val actionCardModifier =
                Modifier
                    .draggable(state, Orientation.Vertical)
                    .height(screenHeight * 0.3f)

            ActionCards.CardRow(combatEnumListToActionList(combatActions), actionCardModifier)
        }
    }

    companion object {
        private const val TAG = "CombatActivity"
    }
}


