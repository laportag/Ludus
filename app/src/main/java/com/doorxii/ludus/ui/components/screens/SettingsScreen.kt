package com.doorxii.ludus.ui.components.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.doorxii.ludus.ui.components.items.SettingItem
import com.doorxii.ludus.ui.components.items.SettingsItemType

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Example Settings Items
        SettingItem(
            title = "Notifications",
            settingsItemType = SettingsItemType.SWITCH,
            onSettingChanged = { isEnabled ->
                // Handle notification setting change
                println("Notifications enabled: $isEnabled")
            }
        )

        SettingItem(
            title = "Dark Mode",
            settingsItemType = SettingsItemType.SWITCH,
            onSettingChanged = { isEnabled ->
                // Handle dark mode setting change
                println("Dark mode enabled: $isEnabled")
            }
        )

        SettingItem(
            title = "About",
            settingsItemType = SettingsItemType.BUTTON,
            onSettingChanged = {
                // Handle about button click
                println("About clicked")
            }
        )
    }
}

