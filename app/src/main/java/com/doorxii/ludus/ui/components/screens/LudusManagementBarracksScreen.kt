package com.doorxii.ludus.ui.components.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.doorxii.ludus.ui.activities.LudusManagementActivity
import com.doorxii.ludus.ui.activities.LudusManagementActivityViewModel
import com.doorxii.ludus.ui.components.BarracksList


@Composable
fun LudusManagementBarracksScreen(
    viewModel: LudusManagementActivityViewModel,
    parentPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(parentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BarracksList(list = viewModel.playerGladiators.collectAsState().value) {
            Log.d(LudusManagementActivity.TAG, "Gladiator: $it")
        }
    }
}

