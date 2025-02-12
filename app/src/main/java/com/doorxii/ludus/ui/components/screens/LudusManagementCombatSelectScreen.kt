package com.doorxii.ludus.ui.components.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.doorxii.ludus.ui.activities.LudusManagementActivityViewModel
import com.doorxii.ludus.ui.components.lists.GladiatorDisplay
import com.doorxii.ludus.ui.components.bars.InitiateCombatBar
import com.doorxii.ludus.ui.components.bars.LudusSelectionBar
import com.doorxii.ludus.ui.components.popups.CombatResultAlertDialogue

@Composable
fun LudusManagementCombatSelectScreen(
    viewModel: LudusManagementActivityViewModel,
    parentPadding: PaddingValues
) {
    val TAG = "LudusManagementCombatSelectScreen"

    CombatResultAlertDialogue(showPopup = viewModel.isCombatResultShown.collectAsState().value, combatReport = viewModel.combatResultText.collectAsState().value) {
        viewModel.setCombatResultShown(false)
    }

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
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Select Combatants")
            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
            ) {
                GladiatorDisplay(
                    title = viewModel.playerLudus.collectAsState().value?.name ?: "Player",
                    gladiators = viewModel.playerGladiators.collectAsState().value,
                    selectedGladiators = viewModel.selectedPlayerGladiators.value
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
                    )}
                GladiatorDisplay(
                    title = viewModel.selectedEnemyLudus.collectAsState().value?.name ?: "Select Enemy",
                    gladiators = viewModel.gladiatorsByLudus.collectAsState().value,
                    selectedGladiators = viewModel.selectedEnemyGladiators.value
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
                    Log.d(TAG, "selected player gladiators: ${viewModel.selectedPlayerGladiators.value.joinToString(", ") { it!!.name }}")
                    Log.d(TAG, "selected enemy gladiators: ${viewModel.selectedEnemyGladiators.value.joinToString(", ") { it!!.name }}")
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

