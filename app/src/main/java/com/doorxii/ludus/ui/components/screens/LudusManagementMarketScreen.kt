package com.doorxii.ludus.ui.components.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.doorxii.ludus.ui.activities.LudusManagementActivityViewModel
import com.doorxii.ludus.ui.components.MarketList

@Composable
fun LudusManagementMarketScreen(
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
        Text("Gladiator Market")
        MarketList(list = viewModel.marketGladiatorList.collectAsState().value) {
            Log.d("Selected gladiator", it.name)
        }
    }
}