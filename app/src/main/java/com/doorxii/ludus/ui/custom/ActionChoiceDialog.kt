import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.doorxii.ludus.actions.combatactions.CombatActions

class ActionChoiceDialog() {

    companion object {
        fun makeChoice(choice: CombatActions?) {

        }
    }
}

@Composable
@Preview
fun CombatDropDownAndButton() {
    var expanded by remember { mutableStateOf(false) }
    val combatActions = listOf(CombatActions.BASIC_ATTACK, CombatActions.TIRED_ATTACK, CombatActions.WAIT)
    var choice: CombatActions? by remember {
        mutableStateOf(null)
    }
    var buttontext: String by remember { mutableStateOf("Action Choice") }

    Row {
        Button(onClick = { expanded = true }) {
            Text(buttontext)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            for (action in combatActions) {
                DropdownMenuItem(text = { Text(action.name) }, onClick = {
                    choice = action
                    buttontext = action.name
                    expanded = false
                })
            }
        }

        Button(onClick = { ActionChoiceDialog.makeChoice(choice) }){
            Text("Submit")
        }
    }
}