package com.doorxii.ludus.ui.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.activities.CombatActivityViewModel
import com.doorxii.ludus.ui.components.cards.PlayerGladiatorCardWithActionSelection
import com.doorxii.ludus.utils.combat.Combat

@Composable
fun PlayerGladiatorGrid(
    combat: MutableState<Combat?>,
    actingGladiator: Gladiator?,
    viewModel: CombatActivityViewModel,
    onActingGladiatorChange: (Gladiator?) -> Unit,
    modifier: Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Two columns
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth() // Fill available width
    ) {
        items(combat.value!!.playerGladiatorList) { gladiator ->
            PlayerGladiatorCardWithActionSelection(
                gladiator,
                gladiator == actingGladiator,
                viewModel
            ) { clickedGladiator ->
                if (!viewModel.hasGladiatorHadATurn(clickedGladiator)) {
                    onActingGladiatorChange(clickedGladiator)
                }
            }
        }
    }
}