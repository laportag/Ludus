package com.doorxii.ludus.ui.components.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.ui.activities.LudusManagementActivity
import com.doorxii.ludus.ui.activities.LudusManagementActivityViewModel
import com.doorxii.ludus.ui.components.EnemyGladiatorsDisplay
import com.doorxii.ludus.ui.components.InitiateCombatBar
import com.doorxii.ludus.ui.components.LudusSelectionBar
import com.doorxii.ludus.ui.components.PlayerGladiatorsDisplay

@Composable
fun LudusManagementCombatSelectScreen(
    viewModel: LudusManagementActivityViewModel,
    parentPadding: PaddingValues
) {
//    val selectedPlayerGladiators = mutableStateOf<List<Gladiator?>>(emptyList())
//    val selectedEnemyGladiators = mutableStateOf<List<Gladiator?>>(emptyList())
//    val gladiatorsInSelectedEnemyLudus = mutableStateOf<List<Gladiator>>(emptyList())

    Column(
        Modifier
            .fillMaxSize()
            .padding(parentPadding)
    ) {
        LudusSelectionBar(
            selectedEnemyLudus = viewModel.selectedEnemyLudus.collectAsState().value,
            ludiExcludingPlayer = viewModel.ludiExcludingPlayer.collectAsState().value
        ) { ludus ->
            viewModel.setSelectedEnemyLudus(ludus)
            viewModel.getGladiatorsByLudusId(ludus.ludusId)
        }

        Column(
            Modifier
                .height(LocalConfiguration.current.screenHeightDp.dp * 14 / 15)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Select Combatants")
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenHeightDp.dp * 12 / 15)
            ) {
                PlayerGladiatorsDisplay(
                    playerLudus = viewModel.playerLudus.collectAsState().value,
                    playerGladiators = viewModel.playerGladiators.collectAsState().value,
                    selectedPlayerGladiators = viewModel.selectedPlayerGladiators.value
                ) { gladiator ->
                    val currentSelection = viewModel.selectedPlayerGladiators.value
                    viewModel.setSelectedPlayerGladiators(
                        if (currentSelection.contains(gladiator)) {
                            currentSelection - gladiator
                        } else if (!currentSelection.any { it?.gladiatorId == gladiator.gladiatorId }) { // Check for duplicates
                            currentSelection + gladiator
                        } else {
                            currentSelection // If duplicate, do nothing
                        }
                    )
                }
                EnemyGladiatorsDisplay(
                    selectedEnemyLudus = viewModel.selectedEnemyLudus.collectAsState().value,
                    enemyGladiators = viewModel.gladiatorsByLudus.collectAsState().value,
                    selectedEnemyGladiators = viewModel.selectedEnemyGladiators.value
                ) { gladiator ->
                    val currentSelection = viewModel.selectedEnemyGladiators.value
                    viewModel.setSelectedEnemyGladiators(
                        if (currentSelection.contains(gladiator)) {
                            currentSelection - gladiator
                        } else if (!currentSelection.any { it?.gladiatorId == gladiator.gladiatorId }) { // Check for duplicates
                            currentSelection + gladiator
                        } else {
                            currentSelection // If duplicate, do nothing
                        }
                    )
                }
            }
            InitiateCombatBar(
                onStartCombat = {
                    Log.d(
                        LudusManagementActivity.TAG,
                        "Combatants: ${viewModel.selectedPlayerGladiators.value.joinToString(", ") { it!!.name }} "
                    )
                    viewModel.startCombat()
                },
                onSimCombat = {
                    viewModel.simCombat()
                },
                isEnabled = viewModel.selectedEnemyLudus.collectAsState().value != null && viewModel.selectedPlayerGladiators.value.isNotEmpty()
            )
        }
    }
}

