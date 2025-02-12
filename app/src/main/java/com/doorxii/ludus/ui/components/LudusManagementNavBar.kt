package com.doorxii.ludus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LudusManagementNavBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(onClick = {
//            ludusManagementView.value = LudusManagementViews.HOME
        }) {
            Text("Home")
        }
        Button(onClick = {
//            ludusManagementView.value = LudusManagementViews.BARRACKS_MANAGEMENT
        }) {
            Text("Barracks")
        }
        Button(onClick = {
//            ludusManagementView.value = LudusManagementViews.GLADIATOR_MARKET
        }) {
            Text("Market")
        }
        Row {
            Button(onClick = {
//                ludusManagementView.value = LudusManagementViews.COMBAT_SELECT
            }) {
                Text("Combat")
            }
        }
    }
}