package com.doorxii.ludus.ui.components.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SettingItem(
    title: String,
    settingsItemType: SettingsItemType,
    onSettingChanged: (Boolean) -> Unit
) {
    val isEnabled = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (settingsItemType == SettingsItemType.BUTTON) {
                    onSettingChanged(true)
                } else {
                    isEnabled.value = !isEnabled.value
                    onSettingChanged(isEnabled.value)
                }
            }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.weight(1f))

        if (settingsItemType == SettingsItemType.SWITCH) {
            Switch(
                checked = isEnabled.value,
                onCheckedChange = {
                    isEnabled.value = it
                    onSettingChanged(it)
                }
            )
        }
    }
    HorizontalDivider()
}


