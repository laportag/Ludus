package com.doorxii.ludus.ui.components.bars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.doorxii.ludus.utils.ui.LudusManagementRoutes

@Composable
fun LudusManagementNavBar(navController: NavHostController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(onClick = {
            navController.navigate(LudusManagementRoutes.Home.route)
        }) {
            Text("Home")
        }
        Button(onClick = {
            navController.navigate(LudusManagementRoutes.Barracks.route)
        }) {
            Text("Barracks")
        }
        Button(onClick = {
            navController.navigate(LudusManagementRoutes.Market.route)
        }) {
            Text("Market")
        }
        Button(onClick = {
            navController.navigate(LudusManagementRoutes.CombatSelect.route)
        }) {
            Text("Combat")
        }

    }
}