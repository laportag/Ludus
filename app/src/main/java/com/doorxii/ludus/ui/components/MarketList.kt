package com.doorxii.ludus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.data.models.animal.Gladiator

@Composable
fun MarketList(
    list: List<Gladiator>,
    onItemSelected: (Gladiator) -> Unit
)
{
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(list) { gladiator ->
            SelectableItemMarketGladiator(
                gladiator,
                onItemSelected
            )
        }
    }
}