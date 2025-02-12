package com.doorxii.ludus.ui.components.lists

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.activities.CombatActivityViewModel
import com.doorxii.ludus.ui.components.cards.EnemyGladiatorCardWithDropTarget
import com.doorxii.ludus.utils.actions.combatactions.ChosenAction
import com.doorxii.ludus.utils.combat.Combat

@Composable
fun EnemyGladiatorGrid (
    combat: MutableState<Combat?>,
    actingGladiator: Gladiator?,
    viewModel: CombatActivityViewModel,
    makePlayerTurn: (List<ChosenAction>) -> Unit,
    resetActions: () -> Unit,
    findNextAvailableGladiator: () -> Gladiator?,
    onActingGladiatorChange: (Gladiator?) -> Unit,
    modifier: Modifier
) {
    val TAG = "EnemyGladiatorGrid"

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Two columns
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth() // Fill available width
            .wrapContentHeight()
    ) {
        items(combat.value!!.enemyGladiatorList) { gladiator ->
            EnemyGladiatorCardWithDropTarget(gladiator) { action, target ->
                if (actingGladiator != null) {
                    Log.d(
                        TAG,
                        "actor: ${actingGladiator.name} action: $action target: ${target.name}"
                    )
                    val chosenAct =
                        ChosenAction(actingGladiator.gladiatorId, target.gladiatorId, action)

                    viewModel.updateGladiatorAction(actingGladiator, chosenAct)
                    onActingGladiatorChange(findNextAvailableGladiator())
                    if (viewModel.haveAllGladiatorsHadATurn()) {
                        makePlayerTurn(
                            viewModel.gladiatorActions.value.values.toList().filterNotNull()
                        )
                        resetActions()
                        onActingGladiatorChange(findNextAvailableGladiator())
                    }
                }
            }
        }
    }
}