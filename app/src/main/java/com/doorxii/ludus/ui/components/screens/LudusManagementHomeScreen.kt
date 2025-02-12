package com.doorxii.ludus.ui.components.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.doorxii.ludus.data.models.ludus.Ludus

@Composable
fun LudusManagementHomeScreen(parentPadding: PaddingValues, playerLudus: Ludus) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(parentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ludus: ${playerLudus.name}")
        Button(onClick = {
//            ludusManagementView.value = LudusManagementViews.BARRACKS_MANAGEMENT
        }) {
            Text("Manage Barracks")
        }
        Button(onClick = {
//            ludusManagementView.value = LudusManagementViews.GLADIATOR_MARKET
        }) {
            Text("Purchase Gladiators")
        }
        Row {
            Button(onClick = {
//                ludusManagementView.value = LudusManagementViews.COMBAT_SELECT
            }) {
                Text("Choose Enemy Ludus")
            }
        }
    }
}