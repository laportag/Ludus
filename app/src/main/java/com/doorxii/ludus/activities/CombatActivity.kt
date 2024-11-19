package com.doorxii.ludus.activities

import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.doorxii.ludus.actions.combatactions.ChosenAction
import com.doorxii.ludus.actions.combatactions.CombatActions
import com.doorxii.ludus.combat.Combat
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.cards.ActionCards
import com.doorxii.ludus.ui.cards.DropTarget
import com.doorxii.ludus.ui.cards.GladiatorCards
import com.doorxii.ludus.ui.cards.LongPressDraggable
import com.doorxii.ludus.ui.theme.LudusTheme
import com.doorxii.ludus.utils.CombatSerialization.readCombatFromFile
import com.doorxii.ludus.utils.CombatSerialization.saveCombatJson
import com.doorxii.ludus.utils.enums.EnumToAction.combatEnumListToActionList
import java.io.File

class CombatActivity : ComponentActivity() {

    private var combat = mutableStateOf<Combat?>(null)

    //    private var choice: CombatActions? = null
    private var text = mutableStateOf("")
    private lateinit var combatFile: File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        combat.value = readCombatFromJson()
        Log.d(TAG, "combat null?: " + combat.value!!.combatName)

        enableEdgeToEdge()
        setContent {
            LudusTheme {
                LongPressDraggable(modifier = Modifier.fillMaxSize()) {
                    CombatLayout()
                }
            }
        }
    }

    private fun readCombatFromJson(): Combat {
        val uri = intent.data
        combatFile = File(uri!!.path!!)
//        val combatJson = combatFile.readText()
        val combat = readCombatFromFile(combatFile)
        return combat
    }

    override fun finishActivity(requestCode: Int) {
        Log.d(TAG, "finish combat")
        super.finishActivity(requestCode)
    }


    @Preview
    @Composable
    fun CombatLayout() {

        val battleText: String by remember { text }

        var expanded by remember { mutableStateOf(false) }

        val combatActions =
            listOf(CombatActions.BASIC_ATTACK, CombatActions.TIRED_ATTACK, CombatActions.WAIT)

//        val droppedChoice: CombatActions? by remember {
//            mutableStateOf(choice)
//        }

        val scrollState = rememberScrollState()

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp

        var actingGladiator by remember { mutableStateOf<Gladiator?>(null) }
        var chosenActions by remember { mutableStateOf<List<ChosenAction>>(emptyList()) }
        var currentPlayerIndex by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom

        ) {

            val actionCardModifier = Modifier.height(screenHeight * 0.3f)
            ActionCards.CardRow(combatEnumListToActionList(combatActions), actionCardModifier, actingGladiator?.stamina
                ?: 0.0)

            // Player Gladiator Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                for ((index, gladiator) in combat.value!!.playerGladiatorList.withIndex()) {
                    PlayerGladiatorCardWithActionSelection(gladiator, index == currentPlayerIndex, chosenActions) { action, target ->
                        // Update chosenActions when an action is selected
                        chosenActions = chosenActions + ChosenAction(
                            gladiator.gladiatorId,
                            target.gladiatorId,
                            action
                        )
                        if (chosenActions.size == combat.value!!.playerGladiatorList.size) {
                            // Trigger playCardOnDrop if actions selected for all player gladiators
                            playCardOnDrop(chosenActions)
                            chosenActions = emptyList() // Reset chosenActions
                            currentPlayerIndex = 0 // Reset currentPlayerIndex
                        } else {
                            currentPlayerIndex++ // Move to the next player gladiator
                        }
                    }
                }
            }
            // Enemy Gladiator Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                for (gladiator in combat.value!!.enemyGladiatorList) {
                    EnemyGladiatorCardWithDropTarget(gladiator) { action, target ->
                        // You might not need this lambda here if playCardOnDrop is triggered automatically
                    }
                }
            }







//            DropTarget<CombatActions>(modifier = Modifier.height(screenHeight * 0.65f)) { isInBound, combatAction ->
//                // when card dropped
//                val bgColor = if (isInBound) {
//                    Color.Red
//                } else {
//                    Color.White
//                }
//                if (combatAction != null) {
//                    choice = combatAction
//                    Log.d(TAG, "choice dropped: $droppedChoice")
//                }
////                playCardOnDrop(combatAction)
////                var roundResult = combat!!.playCombatRound(choice)
////                battleText += roundResult.combatReport
//
//                Column(
//                    modifier = Modifier
//                        .height(screenHeight * 0.65f)
//                        .background(bgColor)
//
//                ) {
//                    for (gladiator in combat.value!!.playerGladiatorList) {
//                        DropTarget<Gladiator>(modifier = Modifier.fillMaxWidth()) { gladiatorIsInBound, targetGladiator ->
//                            val gladiatorBgColor = if (gladiatorIsInBound) {
//                                Color.Yellow // Highlight when action card is over gladiator card
//                            } else {
//                                Color.White
//                            }
//                            if (combatAction != null && targetGladiator != null) {
//                                chosenActions = chosenActions + ChosenAction(
//                                    gladiator,
//                                    targetGladiator,
//                                    combatAction
//                                )
////                                playCardOnDrop(combatAction, targetGladiator)
//                            }
//                            Box(modifier = Modifier.background(gladiatorBgColor)) {
//                                GladiatorCards.CombatGladiatorCard(gladiator)
//                            }
//
//                        }
//
//                        GladiatorCards.CombatGladiatorCard(gladiator)
//                    }
//                    for (gladiator in combat.value!!.enemyGladiatorList) {
//                        DropTarget<CombatActions>(modifier = Modifier.fillMaxWidth()) { gladiatorIsInBound, targetGladiator ->
//
//                        }
//                        if (combat.value!!.gladiatorList.size < 2) {
//                            Text("Combat over")
//                            Button(onClick = { combatCompleted() }) {
//                                Text("Finish Combat")
//                            }
//                        }
//
//
//
//                        TextField(
//                            value = battleText,
//                            onValueChange = {},
//                            modifier = Modifier
////                        .fillMaxSize()
//                                .verticalScroll(scrollState)
//                                .aspectRatio(16f / 9f)
//                                .height(screenHeight * 0.2f)
//                        )
//                    }
//                }
//                val actionCardModifier =
//                    Modifier
//                        .height(screenHeight * 0.3f)
//                ActionCards.CardRow(
//                    combatEnumListToActionList(combatActions),
//                    actionCardModifier,
//                    combat.value!!.playerGladiatorList[0].stamina
//                )
//            }
        }
    }

    @Composable
    fun PlayerGladiatorCardWithActionSelection(
        gladiator: Gladiator,
        isCurrentTurn: Boolean,
        chosenActions: List<ChosenAction>,
        onActionSelected: (CombatActions, Gladiator) -> Unit
    ) {
        val gladiatorBgColor = if (isCurrentTurn) {
            Color.Yellow // Highlight current player gladiator
        } else {
            Color.White
        }

        // Apply background color to visually indicate the current player turn
        Box(modifier = Modifier.background(gladiatorBgColor)) {
            GladiatorCards.CombatGladiatorCard(gladiator)
        }
    }

    @Composable
    fun EnemyGladiatorCardWithDropTarget(
        gladiator: Gladiator,
        onActionDropped: (CombatActions, Gladiator) -> Unit
    ) {
        DropTarget<CombatActions>(modifier = Modifier.fillMaxWidth()) { gladiatorIsInBound, combatAction ->
            val gladiatorBgColor = if (gladiatorIsInBound) {
                Color.Yellow // Highlight when action card is over gladiator card
            } else {
                Color.White
            }
            // This onActionDropped call is optional if playCardOnDrop is handled elsewhere
//        if (combatAction != null) {
//            onActionDropped(combatAction, gladiator)
//        }
            Box(modifier = Modifier.background(gladiatorBgColor)) {
                GladiatorCards.CombatGladiatorCard(gladiator)
            }

        }
    }

    private fun playCardOnDrop(gladiatorActions: List<ChosenAction>) {

        if (gladiatorActions.isNotEmpty()) {
            val roundResult = combat.value!!.playCombatRound(gladiatorActions)
            Log.d(TAG, "roundResult: " + roundResult.combatReport)
            Log.d(TAG, "combat: " + combat.value.toString())
            text.value = roundResult.combatReport
            if (combat.value!!.isComplete) {
                combatCompleted()
            }
//            choice = null
        }
    }

    private fun combatCompleted() {
        Log.d(TAG, "combat completed")
        val report = combat.value?.combatReport
        saveCombatJson(combat.value!!, combatFile)
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


