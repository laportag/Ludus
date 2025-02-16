package com.doorxii.ludus.ui.components.bars

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.doorxii.ludus.utils.ui.LudusManagementRoutes
import com.doorxii.ludus.utils.ui.NavigationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LudusManagementTopBar(
    navController: NavHostController,
    ludusName: String,
//    onMainMenuClicked: () -> Unit,
//    onSettingsClicked: () -> Unit
) {
    val context = LocalContext.current
    val TAG = "LudusManagementTopBar"
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ludus Name (Left-Aligned)
                Text(
                    text = ludusName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )

                // Main Menu and Settings Buttons (Right-Aligned)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Button(onClick = {
                        Log.d(TAG, "start button clicked")
                        NavigationUtils.navigateToStartActivity(context)
                    }) {
                        Text("Main Menu")
                    }
                    Button(onClick = {
                        navController.navigate(LudusManagementRoutes.Settings.route)
                    }) {
                        Text("Settings")
                    }
                }
            }
        }
    )
}