package com.doorxii.ludus

import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import com.doorxii.ludus.ui.DropTarget
import com.doorxii.ludus.ui.LongPressDraggable
import com.doorxii.ludus.utils.CombatSerialization.returnCombatFile
import com.doorxii.ludus.utils.CombatSerialization.saveCombatJson

class CombatActivity() : ComponentActivity() {

    var combat: Combat? = null
    var choice: CombatActions? = null
    var text = mutableStateOf("")
    lateinit var combatFile: File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        combat = readCombatFromJson()
        Log.d(TAG, "combat null?: " + combat!!.gladiatorList)

        enableEdgeToEdge()
        setContent {
            LudusTheme {
                LongPressDraggable(modifier = Modifier.fillMaxSize()) {
                    CombatLayout()
                }
            }
        }
    }

    fun readCombatFromJson(): Combat {
        val uri = intent.data
        combatFile = File(uri!!.path!!)
        val combatJson = combatFile.readText()
        val combat = Json.decodeFromString<Combat>(combatJson)
        return combat
    }

    override fun finishActivity(requestCode: Int) {
        Log.d(TAG, "finish combat")
        super.finishActivity(requestCode)
    }


    @OptIn(ExperimentalFoundationApi::class)
    @Preview
    @Composable
    fun CombatLayout() {

        var battleText: String by remember { text }

        var expanded by remember { mutableStateOf(false) }

        val combatActions =
            listOf(CombatActions.BASIC_ATTACK, CombatActions.TIRED_ATTACK, CombatActions.WAIT)

        var droppedChoice: CombatActions? by remember {
            mutableStateOf(choice)
        }

        val scrollState = rememberScrollState()

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
//            TopAppBar(title = { Text(combat.combatName) })

            DropTarget<CombatActions>(modifier = Modifier.height(screenHeight * 0.65f)) { isInBound, combatAction ->
                // when card dropped
                val bgColor = if (isInBound) {
                    Color.Red
                } else {
                    Color.White
                }
                if (combatAction != null) {
                    choice = combatAction
                    Log.d(TAG, "choice dropped: " + droppedChoice)
                }
                playCardonDrop(combatAction)
//                var roundResult = combat!!.playCombatRound(choice)
//                battleText += roundResult.combatReport

                Column(
                    modifier = Modifier
                        .height(screenHeight * 0.65f)

                ) {
                    for (gladiator in combat!!.gladiatorList){
                        GladiatorCards.CombatGladiatorCard(gladiator)
                    }
                    if (combat!!.gladiatorList.size < 2){
                        Text("Combat over")
                        Button(onClick = { combatCompleted() }) {
                            Text("Finish Combat")
                        }
                    }



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
            }
            val actionCardModifier =
                Modifier
                    .height(screenHeight * 0.3f)
            ActionCards.CardRow(combatEnumListToActionList(combatActions), actionCardModifier)
        }
    }

    fun playCardonDrop(combatAction: CombatActions?) {

        if (choice != null) {
            Log.d(TAG, "choice: " + choice)
            if (combatAction != null) {
                val roundResult = combat!!.playCombatRound(combatAction)
                Log.d(TAG, "roundResult: " + roundResult.combatReport)
                Log.d(TAG, "combat: " + combat.toString())
                text.value = roundResult.combatReport
                if (combat!!.isComplete) {
                    combatCompleted()
                }
            }
        }
        choice = null
    }

    fun combatCompleted(){
        Log.d(TAG, "combat completed")
        val report = combat?.combatReport
        saveCombatJson(combat!!, combatFile)
        val data = Intent().apply {
            addFlags(FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(combatFile.toUri(), contentResolver.getType(combatFile.toUri()))
        }
        setResult(RESULT_OK, data)
        finish()
    }

    companion object {
        private const val TAG = "CombatActivity"
    }
}


