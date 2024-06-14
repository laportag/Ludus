package com.doorxii.ludus.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.doorxii.ludus.combat.Combat
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.data.db.LudusRepository
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.ludus.Ludus
import com.doorxii.ludus.ui.CombatResultAlert
import com.doorxii.ludus.ui.theme.LudusTheme
import com.doorxii.ludus.utils.CombatSerialization
import com.doorxii.ludus.utils.DatabaseManagement.returnDb
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File

class LudusManagementActivity : ComponentActivity() {

    lateinit var db: AppDatabase
    private lateinit var repository: LudusRepository
    private lateinit var viewModel: LudusManagementActivityViewModel

    val ludus = mutableStateOf<Ludus?>(null)

    private val ludusManagementView = mutableStateOf(LudusManagementViews.HOME)


    private val allLudi = mutableStateOf<List<Ludus>>(emptyList())
    private val ludiExcludingPlayer = mutableStateOf<List<Ludus>>(emptyList())
    private val selectedEnenmyLudus = mutableStateOf<Ludus?>(null)
    private val playerGladiators = mutableStateOf<List<Gladiator>>(emptyList())
    private val selectedLudusGladiators = mutableStateOf<List<Gladiator>>(emptyList())
    private val selectedCombatant = mutableStateOf<Gladiator?>(null)
    private var marketGladiatorList = mutableStateOf<List<Gladiator>>(emptyList())

    private lateinit var combatFile: File
    private var combat: Combat? = null

    private val combatResultAlertEnabled = mutableStateOf(false)
    private val combatResultText = mutableStateOf("")

    private val combatResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d(TAG, "Combat result: $result")
                val resultUri: Uri = result.data?.data!!
                val resFile = File(resultUri.path!!)
                val resCombat = Json.decodeFromString<Combat>(resFile.readText())
                combatFinished(resCombat)
            }
        }

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

    private fun observeFlows() {

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

        // selected enemy ludus
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedEnemyLudus.collect {
                    Log.d(TAG, "obServeFlow selectedEnemyLudus: $it")
                    selectedEnenmyLudus.value = it
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

        // market list
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.marketGladiatorList.collect {
                    Log.d(TAG, "observeFlow marketList: $it")
                    marketGladiatorList.value = it
                }
            }
        }
    }

    private fun startCombat() {
        val enemy = selectedLudusGladiators.value.random()
        Log.d(TAG, "Combatant: ${selectedCombatant.value?.name}, enemy: ${enemy.name}")
        val gladiatorList = listOf(selectedCombatant.value!!, enemy)
        combatFile = CombatSerialization.returnCombatFile(applicationContext)
        combat = Combat.init(gladiatorList)
        CombatSerialization.saveCombatJson(combat!!, combatFile)
        Log.d(TAG, "combatFile: " + combatFile.readText())
        val uri = combatFile.toUri()
        val intent = Intent(this, CombatActivity::class.java)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, contentResolver.getType(uri))
        combatResultLauncher.launch(intent)
        startActivity(intent)
    }

    private fun combatFinished(resCombat: Combat) {
        Log.d(TAG, "Combat complete?: ${resCombat.isComplete}")
        if (resCombat.gladiatorList.isEmpty()) {
            Log.d(TAG, "No winner")
            return
        }
        val winner = resCombat.gladiatorList[0]
        Log.d(TAG, "Winner: ${winner.name}")


        for (gladiator in resCombat.originalGladiatorList) {
            if (gladiator !in resCombat.gladiatorList) {
                // dead gladiators
                gladiator.health = 0.0
                viewModel.updateGladiator(gladiator)

            }
        }
        for (gladiator in resCombat.gladiatorList) {
            viewModel.updateGladiator(gladiator)
        }
        viewModel.getGladiatorsByLudusId(selectedEnenmyLudus.value!!.ludusId)
        viewModel.getPlayerGladiators(ludus.value!!.ludusId)
        combatResultAlertEnabled.value = true
        combatResultText.value = resCombat.combatReport
    }

    private fun simCombat() {
        val enemy = selectedLudusGladiators.value.random()
        val gladiatorList = listOf(selectedCombatant.value!!, enemy)
        combat = Combat.init(gladiatorList)
        val res = combat!!.simCombat()
        Log.d(TAG, "res: " + res.combatReport)
        combatFinished(combat!!)
    }

    fun healAliveGladiators(list: List<Gladiator>) {
        for (gladiator in list) {
            if (gladiator.health < 100.0 && gladiator.health > 0.0) {
                Log.d(TAG, "healing: ${gladiator.name}")
                gladiator.health = 100.0
                gladiator.stamina = 100.0
                gladiator.morale = 100.0
                viewModel.updateGladiator(gladiator)
            }
        }
        viewModel.getPlayerGladiators(ludus.value!!.ludusId)
    }

    fun buyGladiator(gladiator: Gladiator) {
        Log.d(TAG, "buyGladiator: $gladiator")
        viewModel.addGladiatorToPlayer(gladiator)
    }


    @Preview
    @Composable
    fun LudusManagementLayout() {
        val playerLudus by viewModel.playerLudus.collectAsState(initial = null)
        val showAlert by remember { combatResultAlertEnabled }
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

            if (showAlert) {
                CombatResultAlertDialogue()
            }

            when (ludusManagementView.value) {
                LudusManagementViews.HOME -> {
                    playerLudus?.let { LudusManagementHome(innerPadding, it) }
                }

                LudusManagementViews.BARRACKS_MANAGEMENT -> {
                    BarracksManagement(innerPadding)
                }

                LudusManagementViews.GLADIATOR_MARKET -> {
                    GladiatorMarket(innerPadding)
                }

                LudusManagementViews.COMBAT_SELECT -> {
                    CombatSelect(innerPadding)
                }

            }
        }

    }

    @Composable
    fun CombatResultAlertDialogue() {
        val showAlert by remember { combatResultAlertEnabled }
        combat?.let {
            CombatResultAlert().CombatResultAlertDialog(showPopup = showAlert, combat = it) {
                combatResultAlertEnabled.value = false
            }
        }
    }

    @Composable
    fun LudusManagementHome(parentPadding: PaddingValues, playerLudus: Ludus) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(parentPadding),
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
    fun BarracksManagement(parentPadding: PaddingValues) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(parentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BarracksList(list = playerGladiators.value) {
                Log.d(TAG, "Gladiator: $it")
            }
        }
    }

    @Composable
    fun GladiatorMarket(parentPadding: PaddingValues) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(parentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Gladiator Market")
            MarketList(list = marketGladiatorList.value) {

            }
        }
    }

    @Composable
    fun CombatSelect(parentPadding: PaddingValues) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp
        var expanded by remember { mutableStateOf(false) }
        Column(
            Modifier
                .fillMaxSize()
                .padding(parentPadding),
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
                        text = selectedEnenmyLudus.value?.name ?: "Select Enemy Ludus",
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
                                    viewModel.setSelectedEnemyLudus(ludus)
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
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 12 / 15)
                ) {
                    Column(
                        Modifier
                            .width(screenWidth / 2)
                    ) {
                        Text(text = ludus.value?.name ?: "Player")
                        BarracksListShort(list = playerGladiators.value) {
                            selectedCombatant.value = it
                        }
                    }
                    Column(
                        Modifier
                            .width(screenWidth / 2)
                    ) {
                        Text(text = selectedEnenmyLudus.value?.name ?: "Enemy")
                        BarracksListShort(list = selectedLudusGladiators.value) {
                            Log.d(TAG, "Enemy combatant: $it")
                        }
                    }
                }
                Row {
                    Button(onClick = {
                        Log.d(TAG, "Combatant: $selectedCombatant")
                        startCombat()
                    }, enabled = selectedEnenmyLudus.value != null && selectedCombatant.value != null) {
                        Text("Start Combat")
                    }
                    Button(onClick = {
                        simCombat()
                    }) {
                        Text("Sim Combat")
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
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list) { gladiator ->
                SelectableItemBarracksGladiator(
                    gladiator,
                    onItemSelected
                )
            }
        }
    }

    @Composable
    fun MarketList(
        list: List<Gladiator>,
        onItemSelected: (Gladiator) -> Unit
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list) { gladiator ->
                SelectableItemMarketGladiator(
                    gladiator,
                    onItemSelected
                )
            }
        }
    }

    @Composable
    fun BarracksListShort(
        list: List<Gladiator>,
        onItemSelected: (Gladiator) -> Unit
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list) { gladiator ->
                SelectableItemShort(
                    gladiator,
                    onItemSelected
                )
            }
        }
    }


    @Composable
    fun SelectableItemShort(
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

    @Composable
    fun SelectableItemBarracksGladiator(
        item: Gladiator,
        onSelected: (Gladiator) -> Unit
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelected(item) },
            shape = MaterialTheme.shapes.small,
            tonalElevation = 4.dp
        ) {
            Column {
                Row {
                    Column {
                        Text(
                            text = item.name,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "H: ${item.health}",
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "S: ${item.stamina}",
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "M: ${item.morale}",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Column {
                        Button(onClick = { /*TODO*/ }) {
                            Text("Check Stats")
                        }
                        Button(onClick = { healAliveGladiators(listOf(item)) }) {
                            Text("Heal")
                        }
                        Button(onClick = { /*TODO*/ }) {
                            Text("Sell")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SelectableItemMarketGladiator(
        item: Gladiator,
        onSelected: (Gladiator) -> Unit
    ) {


        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelected(item) },
            shape = MaterialTheme.shapes.small,
            tonalElevation = 4.dp
        ) {
            Column {
                Row {
                    Column {
                        Text(
                            text = item.name,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Height: ${item.height}",
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Age: ${item.age}",
                            modifier = Modifier.padding(16.dp)
                        )

                    }
                    Column {
                        Text(
                            text = "Strength: ${item.strength}",
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Speed: ${item.speed}",
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Technique: ${item.technique}",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Column {
                        Button(onClick = { buyGladiator(item) }) {
                            Text("Buy")
                        }
                    }
                }
            }
        }
    }


    companion object {
        const val TAG = "LudusManagementActivity"
    }


}