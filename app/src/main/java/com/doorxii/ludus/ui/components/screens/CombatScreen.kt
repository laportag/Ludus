package com.doorxii.ludus.ui.components.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.activities.CombatActivityViewModel
import com.doorxii.ludus.ui.components.bars.CombatButtonBar
import com.doorxii.ludus.ui.components.cards.ActionCards
import com.doorxii.ludus.ui.components.lists.EnemyGladiatorGrid
import com.doorxii.ludus.ui.components.lists.PlayerGladiatorGrid
import com.doorxii.ludus.ui.components.popups.CombatDialogue
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
    var actingGladiator by remember {
        mutableStateOf(
            combat.value?.playerGladiatorList?.getOrNull(
                0
            )
        )
    }
    val showDialog = remember { mutableStateOf(false) }

    if (combat.value != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            val actionCardModifier =
                Modifier.height(LocalConfiguration.current.screenHeightDp.dp * 0.3f)

            // Enemy Gladiator Grid
            EnemyGladiatorGrid(
                combat = combat,
                actingGladiator = actingGladiator,
                viewModel = viewModel,
                makePlayerTurn = makePlayerTurn,
                resetActions = resetActions,
                findNextAvailableGladiator = findNextAvailableGladiator,
                onActingGladiatorChange = { actingGladiator = it }
            )

            // row for spacer
            Spacer(modifier = Modifier.weight(1f))

            // Action Cards
            ActionCards.CardRow(
                EnumToAction.combatEnumListToActionList(
                    listOf(
                        CombatActions.BASIC_ATTACK,
                        CombatActions.TIRED_ATTACK,
                        CombatActions.WAIT,
                        CombatActions.MISSIO
                    )
                ),
                actionCardModifier,
                actingGladiator?.stamina ?: 0.0
            )

            // Buttons Bar
            CombatButtonBar(showDialog = { showDialog.value = true })

            // Gladiator Turn Bar
            Row() {
                androidx.compose.material3.Text(text = "Name for turn")
            }

            // Player Gladiator Grid
            PlayerGladiatorGrid(
                combat = combat,
                actingGladiator = actingGladiator,
                viewModel = viewModel,
                onActingGladiatorChange = { actingGladiator = it }
            )
        }
    } else {
        androidx.compose.material3.Text("Loading combat data...")
    }
    CombatDialogue(showDialog, battleText)
}