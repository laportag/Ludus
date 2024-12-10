package com.doorxii.ludus.activities

import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
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
import com.doorxii.ludus.utils.enums.EnumToAction
import java.io.File

class CombatActivity : ComponentActivity() {

    private lateinit var viewModel: CombatActivityViewModel

    private var combat = mutableStateOf<Combat?>(null)

    //    private var choice: CombatActions? = null
    private var text = mutableStateOf("")
    private lateinit var combatFile: File

    private var isFinished by mutableStateOf(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[CombatActivityViewModel::class.java]
        combat.value = readCombatFromJson()
        Log.d(TAG, "combat null?: " + combat.value!!.combatName)

        enableEdgeToEdge()
        setContent {

            LudusTheme {
                LongPressDraggable(modifier = Modifier.fillMaxSize()) {
                    if (!isFinished){
                        CombatLayout(viewModel)
                    }
                }
            }

            LaunchedEffect(combat.value) {
                if (combat.value != null) {
                    resetActions()
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

    @Composable
    fun CombatLayout(viewModel: CombatActivityViewModel) {

        val battleText: String by remember { text }

        val combatActions =
            listOf(CombatActions.BASIC_ATTACK, CombatActions.TIRED_ATTACK, CombatActions.WAIT)

        var actingGladiator by remember {
            mutableStateOf(
                combat.value?.playerGladiatorList?.getOrNull(
                    0
                )
            )
        }
//        var chosenActions by remember { mutableStateOf<List<ChosenAction>>(emptyList()) }

//        var currentPlayerIndex by remember { mutableIntStateOf(0) }

        var showDialog by remember { mutableStateOf(false) }

        var gladiatorActions by remember { mutableStateOf<Map<Gladiator, ChosenAction?>>(emptyMap()) }



        if (combat.value != null) {

            gladiatorActions = combat.value!!.playerGladiatorList.associateWith { null }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom

            ) {

                val actionCardModifier =
                    Modifier.height(LocalConfiguration.current.screenHeightDp.dp * 0.3f)


                // Enemy Gladiator Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // Two columns
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth() // Fill available width
                ) {
                    items(combat.value!!.enemyGladiatorList) { gladiator ->
                        EnemyGladiatorCardWithDropTarget(gladiator) { action, target ->
                            if (actingGladiator != null) {
                                Log.d(
                                    TAG,
                                    "actor: ${actingGladiator?.name} action: $action target: ${target.name}"
                                )
                                val chosenAct = ChosenAction(actingGladiator!!.gladiatorId, target.gladiatorId, action)
//                                actingGladiator!!.action = chosenAct

                                viewModel.updateGladiatorAction(actingGladiator!!, chosenAct)
                                actingGladiator = findNextAvailableGladiator()
                                if (viewModel.haveAllGladiatorsHadATurn()) {
                                    makePlayerTurn(viewModel.gladiatorActions.value.values.toList().filterNotNull())
                                    resetActions()
                                    actingGladiator = findNextAvailableGladiator()
                                    showDialog = true
                                }
                            }
                        }
                    }
                }

                // row for spacer
                Spacer(modifier = Modifier.weight(1f))

                // Action Cards
                ActionCards.CardRow(
                    EnumToAction.combatEnumListToActionList(combatActions),
                    actionCardModifier,
                    actingGladiator?.stamina
                        ?: 0.0
                )

                // Buttons Bar
                Row() {
                    Button(onClick = { showDialog = true }) {
                        Text(text = "Show Battle Report")
                    }
                }

                // Gladiator Turn Bar
                Row() {
                    Text(text = "Name for turn")
                }

                // Player Gladiator Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // Two columns
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth() // Fill available width
                ) {
                    items(combat.value!!.playerGladiatorList) { gladiator ->
                        PlayerGladiatorCardWithActionSelection(
                            gladiator,
                            gladiator == actingGladiator
                        ) { clickedGladiator ->
                            if (!viewModel.hasGladiatorHadATurn(clickedGladiator)){
                                actingGladiator = clickedGladiator
                            }
                        }
                    }
                }
            }
        } else {
            Text("Loading combat data...")
        }
        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false }
            ) {
                Card { // Use a Card for better visual appearance
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()) // Make text scrollable
                    ) {
                        Text(text = battleText)

                        Button(onClick = { showDialog = false }) {
                            Text("Close")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PlayerGladiatorCardWithActionSelection(
        playerGladiator: Gladiator,
        isCurrentTurn: Boolean,
        onGladiatorClicked: (Gladiator) -> Unit
    ) {
        val gladiatorBgColor = if (isCurrentTurn) {
            Color.Green // Highlight current player gladiator
        } else {
            Color.White
        }
        val hadTurn = viewModel.hasGladiatorHadATurn(playerGladiator)
        Box(modifier = Modifier
            .background(gladiatorBgColor)
            .graphicsLayer {
                alpha = if (hadTurn) 0.5f else 1f
            }
            .clickable (enabled = !hadTurn) {
                onGladiatorClicked(playerGladiator)
            }
        ) {
            GladiatorCards.CombatGladiatorCard(playerGladiator)
        }
    }

    @Composable
    fun EnemyGladiatorCardWithDropTarget(
        enemyGladiator: Gladiator,
        onActionDropped: (CombatActions, Gladiator) -> Unit
    ) {
        DropTarget<CombatActions>(modifier = Modifier.fillMaxWidth()) { gladiatorIsInBound, combatAction ->
            val gladiatorBgColor = if (gladiatorIsInBound) {
                Color.Yellow // Highlight when action card is over gladiator card
            } else {
                Color.White
            }
            // Callback to handle the dropped action
            if (combatAction != null) {
                onActionDropped(combatAction, enemyGladiator)
            }
            Box(modifier = Modifier.background(gladiatorBgColor)) {
                GladiatorCards.CombatGladiatorCard(enemyGladiator)
            }

        }
    }

    private fun resetActions(){
        viewModel.resetGladiatorActions() // Reset for new combat
        for (gladiator in combat.value!!.playerGladiatorList) {
            viewModel.updateGladiatorAction(gladiator, null)
        }
    }

    private fun makePlayerTurn(gladiatorActions: List<ChosenAction>) {
        if (gladiatorActions.isNotEmpty()) {
            val roundResult = combat.value!!.playCombatRound(gladiatorActions)
            Log.d(TAG, "roundResult: " + roundResult.combatReport)
            Log.d(TAG, "combat: " + combat.value.toString())
            text.value = roundResult.combatReport
            if (combat.value!!.isComplete) {
                handleSubmissions()
                combatCompleted()
            }
        }
    }

    private fun findNextAvailableGladiator(): Gladiator? {
        return combat.value?.playerGladiatorList?.firstOrNull { !viewModel.hasGladiatorHadATurn(it) }
    }

    private fun handleSubmissions() {
        Log.d(TAG, "handle submissions")
        val allSubmittedGladiators = combat.value!!.submittedEnemyGladiatorList + combat.value!!.submittedPlayerGladiatorList
        allSubmittedGladiators.forEach { gladiator ->
            val listToAddTo = if (gladiator in combat.value!!.originalEnemyGladiatorList) {
                combat.value!!.enemyGladiatorList
            } else {
                combat.value!!.playerGladiatorList
            }

            if (missioGranted(gladiator)) {
                combat.value!!.appendReport("${gladiator.name}: missio")
                listToAddTo.add(gladiator)
            } else {
                combat.value!!.appendReport("${gladiator.name}: sine missio")
            }
        }
    }

    private fun missioGranted(gladiator: Gladiator, ): Boolean {
        return true
    }

    private fun combatCompleted() {
        Log.d(TAG, "combat completed")
//        val report = combat.value?.combatReport
        saveCombatJson(combat.value!!, combatFile)
        val data = Intent().apply {
            addFlags(FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(combatFile.toUri(), contentResolver.getType(combatFile.toUri()))
        }
        setResult(RESULT_OK, data)
        isFinished = true
        finish()
        Log.d(TAG, "combat completed finish")
    }

    companion object {
        private const val TAG = "CombatActivity"
    }
}


