package com.doorxii.ludus.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.data.db.LudusRepository
import com.doorxii.ludus.data.models.animal.Gladiator
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


    private val allLudi = mutableStateOf<List<Ludus>>(emptyList())
    private val ludiExcludingPlayer = mutableStateOf<List<Ludus>>(emptyList())

    private val selectedEnemyLudus = mutableStateOf<Ludus?>(null)

    val playerGladiators = mutableStateOf<List<Gladiator>>(emptyList())
    private val selectedLudusGladiators = mutableStateOf<List<Gladiator>>(emptyList())
    private val selectedCombatant = mutableStateOf<Gladiator?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbName = intent.getStringExtra("db_name")
        Log.d(TAG, "onCreate db name: $dbName")

        db = returnDb(dbName!!, applicationContext)
        repository = LudusRepository(db)

        viewModel = ViewModelProvider(this)[LudusManagementActivityViewModel::class.java]
        observeFlows()
        viewModel.start(repository)


        enableEdgeToEdge()
        setContent {
            LudusTheme {
                LudusManagementLayout()
            }
        }
    }

    fun observeFlows() {

        // All ludi
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allLudi.collect {
                    Log.d(TAG, "obServeFlow allLudi: $it")
                    allLudi.value = it
                    viewModel.getLudiExcludingPlayer()
                }
            }
        }

        // Player Ludus
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playerLudus.collect { playerLudus ->
                    Log.d(TAG, "obServeFlow playerLudus: $playerLudus")
                    ludus.value = playerLudus
                    playerLudus?.ludusId?.let { viewModel.getPlayerGladiators(it) }
                    viewModel.getLudiExcludingPlayer()
                }
            }
        }

        // List of ludi excluding the player
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ludiExcludingPlayer.collect {
                    Log.d(TAG, "obServeFlow ludiExcludingPlayer: $it")
                    ludiExcludingPlayer.value = it
                }
            }
        }

        // Gladiators of enemy ludus
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gladiatorsByLudus.collect {
                    Log.d(TAG, "obServeFlow enemyGladiators: $it")
                    selectedLudusGladiators.value = it
                }
            }
        }

        // list of player gladiators
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playerGladiators.collect {
                    Log.d(TAG, "observeFlow player Gladiators: $it")
                    playerGladiators.value = it
                }
            }
        }
    }

    @Preview
    @Composable
    fun LudusManagementLayout() {
        val allLudi by viewModel.allLudi.collectAsState(initial = emptyList())
        val playerLudus by viewModel.playerLudus.collectAsState(initial = null)
        Scaffold(
            bottomBar = {
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
            Log.d(TAG, innerPadding.toString())
            when (ludusManagementView.value) {
                LudusManagementViews.HOME -> {
                    playerLudus?.let { LudusManagementHome(it) }
                }

                LudusManagementViews.BARRACKS_MANAGEMENT -> {
                    BarracksManagement()
                }

                LudusManagementViews.GLADIATOR_MARKET -> {
                    GladiatorMarket()
                }

                LudusManagementViews.COMBAT_SELECT -> {
                    CombatSelect()
                }
            }
        }

    }

    @Composable
    fun LudusManagementHome(playerLudus: Ludus) {
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

    @Composable
    fun BarracksManagement() {
        Text("Barracks Management")
    }

    @Composable
    fun GladiatorMarket() {
        Text("Gladiator Market")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun CombatSelect() {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp

        var selectedLudus by remember { mutableStateOf<Ludus?>(null) }
        var expanded by remember { mutableStateOf(false) }
        val allLudiList by viewModel.allLudi.collectAsState(initial = emptyList())
        Column(
            Modifier.fillMaxSize(),
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight / 15)
                        .clickable(
                            onClick = { expanded = true }
                        )
                ) {
                    Text(
                        text = selectedLudus?.name ?: "Select Enemy Ludus",
                        modifier = Modifier.align(Alignment.Center)
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        ludiExcludingPlayer.value.forEach { ludus ->
                            DropdownMenuItem(
                                text = { Text(text = ludus.name) },
                                onClick = {
                                    expanded = false
                                    selectedLudus = ludus
                                    viewModel.getGladiatorsByLudusId(ludus.ludusId)
                                }
                            )
                        }
                    }
                }

            }
            Column(
                Modifier
                    .height(screenHeight * 14 / 15)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Select Combatant")
                Row {
                    Column(
                        Modifier
                            .width(screenWidth / 2)
                    ){
                        Text(text = "Player")
                        BarracksList(list = playerGladiators.value) {
                            selectedCombatant.value = it
                        }
                    }
                    Column(
                        Modifier
                            .width(screenWidth / 2)
                    ) {
                        Text(text = "Enemy")
                        BarracksList(list = selectedLudusGladiators.value) {
                            Log.d(TAG, "Enemy combatant: $it")
                        }
                    }
                }
            }
        }

    }

    @Composable
    fun BarracksList(
        list: List<Gladiator>,
        onItemSelected: (Gladiator) -> Unit
    ) {
        LazyColumn {
            items(list) { gladiator ->
                SelectableItem(
                    gladiator,
                    onItemSelected
                )
            }
        }
    }


@Composable
fun SelectableItem(
    item: Gladiator,
    onSelected: (Gladiator) -> Unit
) {
    val isSelected = item == selectedCombatant.value
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelected(item) },
        shape = MaterialTheme.shapes.small,
        tonalElevation = 4.dp,
        color = if (isSelected) Color.LightGray else Color.Transparent
    ) {
        Text(
            text = item.name,
            modifier = Modifier.padding(16.dp)
        )
    }

}

companion object {
    const val TAG = "LudusManagementActivity"
}


}