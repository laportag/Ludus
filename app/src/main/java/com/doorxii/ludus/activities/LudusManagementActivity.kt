package com.doorxii.ludus.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.data.db.LudusRepository
import com.doorxii.ludus.data.models.ludus.Ludus
import com.doorxii.ludus.ui.theme.LudusTheme
import com.doorxii.ludus.utils.DatabaseManagement.returnDb
import kotlinx.coroutines.launch

class LudusManagementActivity : ComponentActivity() {

    lateinit var db: AppDatabase
    private lateinit var repository: LudusRepository
    private lateinit var viewModel: LudusManagementActivityViewModel
    val ludus = mutableStateOf<Ludus?>(null)
    private val ludusManagementView = mutableStateOf(LudusManagementViews.HOME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbName = intent.getStringExtra("db_name")
        Log.d(TAG, "onCreate db name: $dbName")

        db = returnDb(dbName!!, applicationContext)
        repository = LudusRepository(db)

        viewModel = ViewModelProvider(this)[LudusManagementActivityViewModel::class.java]
        viewModel.start(repository)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playerLudus.collect { playerLudus ->
                    Log.d(TAG, "onCreate playerLudus: $playerLudus")
                    ludus.value = playerLudus
                }
            }
        }

        enableEdgeToEdge()
        setContent {
            LudusTheme {
                LudusManagementLayout()
            }
        }
    }

    @Preview
    @Composable
    fun LudusManagementLayout() {

        Scaffold(
            bottomBar = {
                // Your bottom app bar content here
                // For example:
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {
                        ludusManagementView.value = LudusManagementViews.HOME
                    }) {
                        Text("Home")
                    }
                    Button(onClick = {
                        ludusManagementView.value = LudusManagementViews.BARRACKS_MANAGEMENT
                    }) {
                        Text("Barracks")
                    }
                    Button(onClick = {
                        ludusManagementView.value = LudusManagementViews.GLADIATOR_MARKET
                    }) {
                        Text("Market")
                    }
                    Row {
                        Button(onClick = {
                            ludusManagementView.value = LudusManagementViews.COMBAT_SELECT
                        }) {
                            Text("Combat")
                        }
                    }
                }
            }
        ) { innerPadding ->
            // Your main content goes here, using innerPadding
            Log.d(TAG, innerPadding.toString())
            when (ludusManagementView.value) {
                LudusManagementViews.HOME -> { LudusManagementHome() }
                LudusManagementViews.BARRACKS_MANAGEMENT -> { BarracksManagement() }
                LudusManagementViews.GLADIATOR_MARKET -> { GladiatorMarket() }
                LudusManagementViews.COMBAT_SELECT -> { CombatSelect() }
            }
        }

    }

    @Composable
    fun LudusManagementHome(){
        if (ludus.value != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Ludus: ${ludus.value?.name}")
                Button(onClick = {
                    ludusManagementView.value = LudusManagementViews.BARRACKS_MANAGEMENT
                }) {
                    Text("Manage Barracks")
                }
                Button(onClick = {
                    ludusManagementView.value = LudusManagementViews.GLADIATOR_MARKET
                }) {
                    Text("Purchase Gladiators")
                }
                Row {
                    Button(onClick = {
                        ludusManagementView.value = LudusManagementViews.COMBAT_SELECT
                    }) {
                        Text("Choose Enemy Ludus")
                    }
                }
            }
        }
    }

    @Composable
    fun BarracksManagement(){
        Text("Barracks Management")
    }

    @Composable
    fun GladiatorMarket(){
        Text("Gladiator Market")
    }

    @Composable
    fun CombatSelect(){
        Text("Combat Select")
    }

//    override fun finishActivity(requestCode: Int) {
//        super.finishActivity(requestCode)
//    }

    companion object {
        const val TAG = "LudusManagementActivity"
    }


}