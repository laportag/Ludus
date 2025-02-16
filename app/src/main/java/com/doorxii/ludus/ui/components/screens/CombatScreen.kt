package com.doorxii.ludus.ui.components.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.doorxii.ludus.data.models.actions.combatactions.ChosenAction
import com.doorxii.ludus.data.models.actions.combatactions.CombatActions
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.activities.CombatActivityViewModel
import com.doorxii.ludus.ui.components.bars.CombatButtonBar
import com.doorxii.ludus.ui.components.cards.CardRow
import com.doorxii.ludus.ui.components.cards.DropTarget
import com.doorxii.ludus.ui.components.cards.LongPressDraggable
import com.doorxii.ludus.ui.components.lists.EnemyGladiatorGrid
import com.doorxii.ludus.ui.components.lists.PlayerGladiatorGrid
import com.doorxii.ludus.ui.components.popups.CombatDialogue
import com.doorxii.ludus.utils.combat.Combat
import com.doorxii.ludus.utils.combat.CombatUtils

@Composable
fun CombatScreen(
    viewModel: CombatActivityViewModel,
    combat: MutableState<Combat?>,
    text: MutableState<String>,
    resetActions: () -> Unit,
    makePlayerTurn: (List<ChosenAction>) -> Unit,
    findNextAvailableGladiator: () -> Gladiator?,
    modifier: Modifier,
    handleSubmissions: () -> Unit,
    combatCompleted: () -> Unit
) {
    val TAG = "CombatScreen"
    val battleText: String by remember { text }
    var actingGladiator by remember {
        mutableStateOf(
            combat.value?.playerGladiatorList?.getOrNull(
                0
            )
        )
    }
    val showDialog = remember { mutableStateOf(false) }

    if (combat.value != null) {
        LongPressDraggable(modifier = Modifier) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                // Enemy Gladiator Grid
                Column(modifier = Modifier.weight(0.3f)) {
                    DropTarget<CombatActions>(
                        modifier = Modifier.fillMaxWidth(),
                        isTargetlessDropTarget = true,

                    ) { isInBound, data ->
                        EnemyGladiatorGrid(
                            modifier = modifier.fillMaxSize(),
                            combat = combat,
                            actingGladiator = actingGladiator,
                            viewModel = viewModel,
                            makePlayerTurn = makePlayerTurn,
                            resetActions = resetActions,
                            findNextAvailableGladiator = findNextAvailableGladiator,
                            onActingGladiatorChange = { actingGladiator = it },
                        )
                        if (isInBound && data != null) {
                            Log.d(TAG, "targetless card drapped ${data}")
                            val chosenAction = ChosenAction(
                                action = data,
                                actingGladiatorID = actingGladiator!!.gladiatorId,
                                targetGladiatorID = 0
                            )
                            CombatUtils.handleAction(
                                chosenAction = chosenAction,
                                actingGladiator = actingGladiator!!,
                                viewModel = viewModel,
                                onTurnEnded = makePlayerTurn,
                                resetActions = resetActions,
                                findNextAvailableGladiator = findNextAvailableGladiator,
                                onActingGladiatorChange = { actingGladiator = it }
                            )
                        }
                    }
                }

                // row for spacer
                Spacer(modifier = Modifier.weight(0.1f))

                // Action Cards
                CardRow(
                    CombatUtils.combatEnumListToActionList(
                        listOf(
                            CombatActions.BASIC_ATTACK,
                            CombatActions.TIRED_ATTACK,
                            CombatActions.WAIT,
                            CombatActions.MISSIO
                        )
                    ),
                    modifier = Modifier.weight(0.3f),
                    actingGladiator?.stamina ?: 0.0
                )

                // Buttons Bar
                CombatButtonBar(showDialog = { showDialog.value = true })

                // Gladiator Turn Bar
                Row(modifier = Modifier.fillMaxWidth()) {
                    androidx.compose.material3.Text(text = "${actingGladiator?.name}'s turn" ?: "Choose an action")
                }

                // Player Gladiator Grid
                Column(modifier = Modifier.weight(0.3f)) {
                    PlayerGladiatorGrid(
                        modifier = Modifier.fillMaxSize(),
                        combat = combat,
                        actingGladiator = actingGladiator,
                        viewModel = viewModel,
                        onActingGladiatorChange = { actingGladiator = it }
                    )
                }
            }
        }
    } else {
        androidx.compose.material3.Text("Loading combat data...")
    }
    CombatDialogue(showDialog, battleText)
}