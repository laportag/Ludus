package com.doorxii.ludus.ui.components.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.doorxii.ludus.ui.activities.LudusManagementActivityViewModel

@Composable
fun LudusManagementHomeScreen(
    viewModel: LudusManagementActivityViewModel,
    navController: NavHostController
) {
    val playerLudus = viewModel.playerLudus.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxSize()
            ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = playerLudus?.name ?: "No player name")
        Button(onClick = {
            navController.navigate(LudusManagementRoutes.Barracks.route)
        }) {
            Text("Manage Barracks")
        }
        Button(onClick = {
            navController.navigate(LudusManagementRoutes.Market.route)
        }) {
            Text("Purchase Gladiators")
        }
        Row {
            Button(onClick = {
                navController.navigate(LudusManagementRoutes.CombatSelect.route)
            }) {
                Text("Choose Enemy Ludus")
            }
        }
    }
}