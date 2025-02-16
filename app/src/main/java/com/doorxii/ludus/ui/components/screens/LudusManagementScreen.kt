package com.doorxii.ludus.ui.components.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.doorxii.ludus.ui.activities.LudusManagementActivityViewModel
import com.doorxii.ludus.ui.components.bars.LudusManagementNavBar
import com.doorxii.ludus.ui.components.bars.LudusManagementTopBar
import com.doorxii.ludus.utils.ui.LudusManagementRoutes

@Composable
fun LudusManagementScreen(
    viewModel: LudusManagementActivityViewModel
) {
    val TAG = "LudusManagementScreen"
    val navController = rememberNavController()
    Log.d(TAG, "player ludus on create: ${viewModel.playerLudus.collectAsState().value}")
    Scaffold(
        topBar = {
            LudusManagementTopBar(navController, viewModel.playerLudus.collectAsState().value?.name ?: "Ludus")
        },
        bottomBar = {
            LudusManagementNavBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LudusManagementRoutes.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(LudusManagementRoutes.Home.route) {
                    LudusManagementHomeScreen(
                        viewModel = viewModel,
                        navController=navController
                    )

            }
            composable(LudusManagementRoutes.Market.route) {
                LudusManagementMarketScreen(
                    viewModel = viewModel
                )
            }
            composable(LudusManagementRoutes.Barracks.route) {
                LudusManagementBarracksScreen(viewModel = viewModel)
            }
            composable(LudusManagementRoutes.CombatSelect.route) {
                LudusManagementCombatSelectScreen(viewModel = viewModel)
            }
            composable(LudusManagementRoutes.Settings.route) {
                SettingsScreen()
            }
        }
    }




}