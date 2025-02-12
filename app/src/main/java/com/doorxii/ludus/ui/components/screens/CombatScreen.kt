package com.doorxii.ludus.ui.components.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.activities.CombatActivityViewModel
import com.doorxii.ludus.ui.components.cards.ActionCards
import com.doorxii.ludus.ui.components.cards.EnemyGladiatorCardWithDropTarget
import com.doorxii.ludus.ui.components.cards.PlayerGladiatorCardWithActionSelection
import com.doorxii.ludus.utils.actions.combatactions.ChosenAction
import com.doorxii.ludus.utils.actions.combatactions.CombatActions
import com.doorxii.ludus.utils.combat.Combat
import com.doorxii.ludus.utils.enums.EnumToAction

@Composable
fun CombatScreen(
    viewModel: CombatActivityViewModel,
    combat: MutableState<Combat?>,
    text: MutableState<String>,
    resetActions: () -> Unit,
    makePlayerTurn: (List<ChosenAction>) -> Unit,
    findNextAvailableGladiator: () -> Gladiator?,
    handleSubmissions: () -> Unit,
    combatCompleted: () -> Unit
) {
    val battleText: String by remember { text }

    val combatActions =
        listOf(CombatActions.BASIC_ATTACK, CombatActions.TIRED_ATTACK, CombatActions.WAIT, CombatActions.MISSIO)

    var actingGladiator by remember {
        mutableStateOf(
            combat.value?.playerGladiatorList?.getOrNull(
                0
            )
        )
    }

    var showDialog by remember { mutableStateOf(false) }

    if (combat.value != null) {

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
                modifier = Modifier
                    .fillMaxWidth() // Fill available width
                    .wrapContentHeight()
            ) {
                items(combat.value!!.enemyGladiatorList) { gladiator ->
                    EnemyGladiatorCardWithDropTarget(gladiator) { action, target ->
                        if (actingGladiator != null) {
                            Log.d(
                                "CombatLayout",
                                "actor: ${actingGladiator?.name} action: $action target: ${target.name}"
                            )
                            val chosenAct = ChosenAction(actingGladiator!!.gladiatorId, target.gladiatorId, action)

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
                        gladiator == actingGladiator,
                        viewModel
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