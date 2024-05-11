import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class ActionChoiceDialog() {
    val combatActions = listOf("Basic Attack", "Tired Attack", "Wait")
}

@Composable
@Preview
fun AlertDialogWithDropdownMenu() {
    var showDialog by remember { mutableStateOf(true) }
    var selectedOption by remember { mutableStateOf("") }



    if (showDialog) {

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Select an Option") },
            text = {
                DropdownMenu(
                    expanded = selectedOption.isEmpty(),
                    onDismissRequest = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DropdownMenuItem(text = { Text("A") }, onClick = { selectedOption = "A" })
                    DropdownMenuItem(text = { Text("B") }, onClick = { selectedOption = "B" })
                    DropdownMenuItem(text = { Text("C") }, onClick = { selectedOption = "C" })
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}
}

@Composable
fun DropDownMenuCombatActions(selectedOption: String){
    val combatActions = listOf("Basic Attack", "Tired Attack", "Wait")

    var option = selectedOption

    DropdownMenu(
        expanded = selectedOption.isEmpty(),
        onDismissRequest = { },
        modifier = Modifier.fillMaxWidth()
    ) {

        for (action in combatActions) {
            DropdownMenuItem(text = { Text(action) }, onClick = { option = action})
        }
    }
}