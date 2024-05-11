package com.doorxii.ludus

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.doorxii.ludus.combat.Combat
import com.doorxii.ludus.data.models.beings.Gladiator
import com.doorxii.ludus.data.models.equipment.Equipment
import com.doorxii.ludus.data.models.equipment.Gladius
import com.doorxii.ludus.ui.theme.LudusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LudusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    testCombat()

    }

    fun testCombat() {
        var titus = Gladiator(
            name = "Titus",
            age = 21.0,
            strength = 75.0,
            speed = 60.0,
            technique = 90.0,
            morale = 100.0,
            health = 100.0,
            stamina = 100.0,
            equipment = listOf<Equipment>(Gladius()),
            bloodlust = 60.0,
            height = 160.0,
        )

        var joseph = Gladiator(
            name = "Joseph",
            age = 34.0,
            strength = 75.0,
            speed = 60.0,
            technique = 40.0,
            morale = 800.0,
            health = 100.0,
            stamina = 100.0,
            equipment = listOf<Equipment>(Gladius()),
            bloodlust = 60.0,
            height = 160.0,
        )

        Log.d(TAG, "Titus attack: ${titus.attack} defence: ${titus.defence}")
        Log.d(TAG, "Joseph attack: ${joseph.attack} defence: ${joseph.defence}")

        Combat(titus, joseph).init()
    }




    companion object {
        private const val TAG = "MainActivity"
    }

}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LudusTheme {
        Greeting("Android")
    }
}

@Composable
fun CombatActionButtons(){
    Button(onClick = { /*TODO*/ }) {
        Text("BasicAttack")
    }
    Button(onClick = { /*TODO*/ }) {
        Text("TiredAttack")
    }
    Button(onClick = { /*TODO*/ }) {
        Text("Wait")
    }
}