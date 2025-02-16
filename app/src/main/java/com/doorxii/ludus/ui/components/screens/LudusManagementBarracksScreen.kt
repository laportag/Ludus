package com.doorxii.ludus.ui.components.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.doorxii.ludus.ui.activities.LudusManagementActivityViewModel
import com.doorxii.ludus.ui.components.items.GladiatorItemContentBarracks
import com.doorxii.ludus.ui.components.items.GladiatorListItem
import com.doorxii.ludus.ui.components.lists.GladiatorList


@Composable
fun LudusManagementBarracksScreen(
    viewModel: LudusManagementActivityViewModel,
) {
    val TAG = "LudusManagementBarracksScreen"
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GladiatorList(
            list = viewModel.playerGladiators.collectAsState().value,
            onItemSelected = { gladiator ->
                Log.d(TAG, "Gladiator: ${gladiator.name}")
            },
            itemContent = { gladiator, onItemSelected ->
                GladiatorListItem(
                    gladiator = gladiator,
                    onItemSelected = onItemSelected,
                    content = { GladiatorItemContentBarracks(it) },
                    buttons = {
                        Column {
                            Button(onClick = { /*TODO*/ }) {
                                Text("Check Stats")
                            }
                            Button(onClick = { /* healAliveGladiators(listOf(gladiator)) */ }) {
                                Text("Heal")
                            }
                            Button(onClick = { /*TODO*/ }) {
                                Text("Sell")
                            }
                        }
                    }
                )
            }
        )
    }
}

