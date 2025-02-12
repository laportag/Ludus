package com.doorxii.ludus.ui.activities

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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.data.db.LudusRepository
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.ludus.Ludus
import com.doorxii.ludus.ui.components.BarracksListShort
import com.doorxii.ludus.ui.components.CombatResultAlert
import com.doorxii.ludus.ui.components.screens.LudusManagementBarracksScreen
import com.doorxii.ludus.ui.components.screens.LudusManagementHomeScreen
import com.doorxii.ludus.ui.components.screens.LudusManagementMarketScreen
import com.doorxii.ludus.ui.theme.LudusTheme
import com.doorxii.ludus.utils.CombatSerialization
import com.doorxii.ludus.utils.DatabaseManagement.returnDb
import com.doorxii.ludus.utils.combat.Combat
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
    private val selectedEnemyLudus = mutableStateOf<Ludus?>(null)
    private val playerGladiators = mutableStateOf<List<Gladiator>>(emptyList())
    private val gladiatorsInSelectedEnemyLudus = mutableStateOf<List<Gladiator>>(emptyList())
    private val selectedPlayerGladiators = mutableStateOf<List<Gladiator?>>(emptyList())
    private val selectedEnemyGladiators = mutableStateOf<List<Gladiator?>>(emptyList())
//    private var marketGladiatorList = mutableStateOf<List<Gladiator>>(emptyList())

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
                    selectedEnemyLudus.value = it
                }
            }
        }

        // Gladiators of enemy ludus
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gladiatorsByLudus.collect {
                    Log.d(TAG, "obServeFlow enemyGladiators: $it")
                    gladiatorsInSelectedEnemyLudus.value = it
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
//                    marketGladiatorList.value = it
                }
            }
        }
    }

    private fun startCombat() {
        combatFile = CombatSerialization.returnCombatFile(applicationContext)
        combat = Combat.init(selectedPlayerGladiators.value, selectedEnemyGladiators.value)
        CombatSerialization.saveCombatJson(combat!!, combatFile)
        Log.d(TAG, "combatFile: " + combatFile.readText())
        val uri = combatFile.toUri()
        val intent = Intent(this, CombatActivity::class.java)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, contentResolver.getType(uri))
        selectedPlayerGladiators.value = emptyList()
        selectedEnemyGladiators.value = emptyList()
        combatResultLauncher.launch(intent)
    }

    private fun combatFinished(resCombat: Combat) {
        Log.d(TAG, "Combat complete?: ${resCombat.isComplete}")


        var winners = emptyList<Gladiator>()
        if (resCombat.playerGladiatorList.isEmpty() && resCombat.enemyGladiatorList.isEmpty()) {
            Log.d(TAG, "No winner")
            return
        }
        else if (resCombat.playerGladiatorList.isEmpty()) {
            Log.d(TAG, "Player loses")
            winners = resCombat.enemyGladiatorList
        }
        else if (resCombat.enemyGladiatorList.isEmpty()) {
            Log.d(TAG, "Player wins")
            winners = resCombat.playerGladiatorList
        }
        else {
            Log.d(TAG, "Combat still going, shouldn't have returned here")
        }

        Log.d(TAG, "Winners: ${winners.joinToString(", " ) {it.name}}")

        val allGladiators = resCombat.originalPlayerGladiatorList + resCombat.originalEnemyGladiatorList
        allGladiators.forEach { gladiator ->
            if (gladiator !in resCombat.playerGladiatorList && gladiator !in resCombat.enemyGladiatorList) {
                gladiator.health = 0.0 // Mark dead gladiators
            }
            viewModel.updateGladiator(gladiator)
        }
        viewModel.getGladiatorsByLudusId(selectedEnemyLudus.value!!.ludusId)
        viewModel.getPlayerGladiators(ludus.value!!.ludusId)
        combatResultText.value = resCombat.combatReport
        combatResultAlertEnabled.value = true
        Log.d(TAG, "combatResultText: $combatResultText")
    }

    private fun simCombat() {
        combat = Combat.init(selectedPlayerGladiators.value, selectedEnemyGladiators.value)
        selectedPlayerGladiators.value = emptyList()
        selectedEnemyGladiators.value = emptyList()
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
                    playerLudus?.let { LudusManagementHomeScreen(innerPadding, it) }
                }

                LudusManagementViews.BARRACKS_MANAGEMENT -> {
                    LudusManagementBarracksScreen(viewModel, innerPadding)
                }

                LudusManagementViews.GLADIATOR_MARKET -> {
                    LudusManagementMarketScreen(viewModel, innerPadding)
                }

                LudusManagementViews.COMBAT_SELECT -> {
//                    LudusManagementCombatSelectScreen(innerPadding)
                }

            }
        }

    }

    @Composable
    fun CombatResultAlertDialogue() {
        val showAlert by remember { combatResultAlertEnabled }
        combatResultText.value.let {
            CombatResultAlert().CombatResultAlertDialog(showPopup = showAlert, combatReport = it) {
                combatResultAlertEnabled.value = false
            }
        }
    }

//    @Composable
//    fun LudusManagementHome(parentPadding: PaddingValues, playerLudus: Ludus) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(parentPadding),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text("Ludus: ${ludus.value?.name}")
//            Button(onClick = {
//                ludusManagementView.value = LudusManagementViews.BARRACKS_MANAGEMENT
//            }) {
//                Text("Manage Barracks")
//            }
//            Button(onClick = {
//                ludusManagementView.value = LudusManagementViews.GLADIATOR_MARKET
//            }) {
//                Text("Purchase Gladiators")
//            }
//            Row {
//                Button(onClick = {
//                    ludusManagementView.value = LudusManagementViews.COMBAT_SELECT
//                }) {
//                    Text("Choose Enemy Ludus")
//                }
//            }
//        }
//    }


//    @Composable
//    fun BarracksManagement(parentPadding: PaddingValues) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(parentPadding),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            BarracksList(list = playerGladiators.value) {
//                Log.d(TAG, "Gladiator: $it")
//            }
//        }
//    }

//    @Composable
//    fun GladiatorMarket(parentPadding: PaddingValues) {
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(parentPadding),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text("Gladiator Market")
//            MarketList(list = marketGladiatorList.value) {
//
//            }
//        }
//    }

    @Composable
    fun CombatSelect(parentPadding: PaddingValues) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(parentPadding)
        ) {
            LudusSelectionBar(
                selectedEnemyLudus = selectedEnemyLudus.value,
                ludiExcludingPlayer = ludiExcludingPlayer.value
            ) { ludus ->
                viewModel.setSelectedEnemyLudus(ludus)
                viewModel.getGladiatorsByLudusId(ludus.ludusId)
            }

            Column(
                Modifier
                    .height(LocalConfiguration.current.screenHeightDp.dp * 14 / 15)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Select Combatants")
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(LocalConfiguration.current.screenHeightDp.dp * 12 / 15)
                ) {
                    PlayerGladiatorsDisplay(
                        playerLudus = ludus.value,
                        playerGladiators = playerGladiators.value,
                        selectedPlayerGladiators = selectedPlayerGladiators.value
                    ) { gladiator ->
                        val currentSelection = selectedPlayerGladiators.value
                        selectedPlayerGladiators.value = if (currentSelection.contains(gladiator)) {
                            currentSelection - gladiator
                        } else if (!currentSelection.any { it?.gladiatorId == gladiator.gladiatorId }) { // Check for duplicates
                            currentSelection + gladiator
                        } else {
                            currentSelection // If duplicate, do nothing
                        }
                    }
                    EnemyGladiatorsDisplay(
                        selectedEnemyLudus = selectedEnemyLudus.value,
                        enemyGladiators = gladiatorsInSelectedEnemyLudus.value,
                        selectedEnemyGladiators = selectedEnemyGladiators.value
                    ) { gladiator ->
                        val currentSelection = selectedEnemyGladiators.value
                        selectedEnemyGladiators.value = if (currentSelection.contains(gladiator)) {
                            currentSelection - gladiator
                        } else if (!currentSelection.any { it?.gladiatorId == gladiator.gladiatorId }) { // Check for duplicates
                            currentSelection + gladiator
                        } else {
                            currentSelection // If duplicate, do nothing
                        }
                    }
                }
                InitiateCombatBar(
                    onStartCombat = {
                        Log.d(TAG, "Combatants: ${selectedPlayerGladiators.value.joinToString(", ") { it!!.name }} ")
                        startCombat()
                    },
                    onSimCombat = {
                        simCombat()
                    },
                    isEnabled = selectedEnemyLudus.value != null && selectedPlayerGladiators.value.isNotEmpty()
                )
            }
        }
    }

    @Composable
    fun PlayerGladiatorsDisplay(
        playerLudus: Ludus?,
        playerGladiators: List<Gladiator>,
        selectedPlayerGladiators: List<Gladiator?>,
        onGladiatorSelected: (Gladiator) -> Unit
    ) {
        Column(
            Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp / 2)
        ) {
            Text(text = playerLudus?.name ?: "Player")
            // Use LazyVerticalGrid here
            BarracksListShort(
                list = playerGladiators,
                selectedGladiators = selectedPlayerGladiators, // Pass the Set here
                onItemSelected = onGladiatorSelected
            )
        }
    }

    @Composable
    fun EnemyGladiatorsDisplay(
        selectedEnemyLudus: Ludus?,
        enemyGladiators: List<Gladiator>,
        selectedEnemyGladiators: List<Gladiator?>,
        onGladiatorSelected: (Gladiator) -> Unit
    ) {
        Column(
            Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp / 2) // Assuming you have screenWidth available
        ) {
            Text(text = selectedEnemyLudus?.name ?: "Enemy")
            BarracksListShort(
                list = enemyGladiators,
                selectedGladiators = selectedEnemyGladiators,
                onItemSelected = onGladiatorSelected
            )
        }
    }

    @Composable
    fun LudusSelectionBar(
        selectedEnemyLudus: Ludus?,
        ludiExcludingPlayer: List<Ludus>,
        onLudusSelected: (Ludus) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp / 15) // Assuming you have screenHeight available
                .clickable { expanded = true }
        ) {
            Text(
                text = selectedEnemyLudus?.name ?: "Select Enemy Ludus",
                modifier = Modifier.align(Alignment.Center)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ludiExcludingPlayer.forEach { ludus ->
                    DropdownMenuItem(
                        text = { Text(text = ludus.name) },
                        onClick = {
                            expanded = false
                            onLudusSelected(ludus)
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun InitiateCombatBar(
        onStartCombat: () -> Unit,
        onSimCombat: () -> Unit,
        isEnabled: Boolean
    ) {
        Row {
            Button(onClick = onStartCombat, enabled = isEnabled) {
                Text("Start Combat")
            }
            Button(onClick = onSimCombat, enabled = isEnabled) {
                Text("Sim Combat")
            }
        }
    }

//    @Composable
//    fun BarracksList(
//        list: List<Gladiator>,
//        onItemSelected: (Gladiator) -> Unit
//    ) {
//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(list) { gladiator ->
//                SelectableItemBarracksGladiator(
//                    gladiator,
//                    onItemSelected
//                )
//            }
//        }
//    }

//    @Composable
//    fun MarketList(
//        list: List<Gladiator>,
//        onItemSelected: (Gladiator) -> Unit
//    ) {
//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(list) { gladiator ->
//                SelectableItemMarketGladiator(
//                    gladiator,
//                    onItemSelected
//                )
//            }
//        }
//    }

//    @Composable
//    fun BarracksListShort(
//        list: List<Gladiator>,
//        selectedGladiators: List<Gladiator?>, // Add selectedGladiators parameter
//        onItemSelected: (Gladiator) -> Unit
//    ) {
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(1),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(list) { gladiator ->
//                val isSelected = selectedGladiators.contains(gladiator) // Check if selected
//                SelectableItemShort(
//                    gladiator,
//                    isSelected, // Pass isSelected state
//                    onItemSelected
//                )
//            }
//        }
//    }


//    @Composable
//    fun SelectableItemShort(
//        item: Gladiator,
//        isSelected: Boolean,
//        onSelected: (Gladiator) -> Unit
//    ) {
//        val backgroundColour = if (isSelected) Color.LightGray else Color.Transparent
//
//        Surface(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { onSelected(item) },
//            shape = MaterialTheme.shapes.small,
//            tonalElevation = 4.dp,
//            color = backgroundColour
//        ) {
//            Text(
//                text = item.name,
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//    }

//    @Composable
//    fun SelectableItemBarracksGladiator(
//        item: Gladiator,
//        onSelected: (Gladiator) -> Unit
//    ) {
//        Surface(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { onSelected(item) },
//            shape = MaterialTheme.shapes.small,
//            tonalElevation = 4.dp
//        ) {
//            Row(modifier = Modifier.padding(16.dp)) {
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(text = item.name)
//                    Text(text = "H: ${item.health}")
//                    Text(text = "S: ${item.stamina}")
//                    Text(text = "M: ${item.morale}")
//                }
//                Column {
//                    Button(onClick = { /*TODO*/ }) {
//                        Text("Check Stats")
//                    }
//                    Button(onClick = { /* healAliveGladiators(listOf(item)) */ }) {
//                        Text("Heal")
//                    }
//                    Button(onClick = { /*TODO*/ }) {
//                        Text("Sell")
//                    }
//                }
//            }
//        }
//    }

//    @Composable
//    fun SelectableItemMarketGladiator(
//        item: Gladiator,
//        onSelected: (Gladiator) -> Unit
//    ) {
//        Surface(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { onSelected(item) },
//            shape = MaterialTheme.shapes.small,
//            tonalElevation = 4.dp
//        ) {
//            Row(modifier = Modifier.padding(16.dp)) { // Apply padding to the Row
//                Column(modifier = Modifier.weight(1f)) { // Make the first Column take available space
//                    Text(text = item.name)
//                    Text(text = "Height: ${item.height}")
//                    Text(text = "Age: ${item.age}")
//                }
//                Column(modifier = Modifier.weight(1f)) { // Make the second Column take available space
//                    Text(text = "Strength: ${item.strength}")
//                    Text(text = "Speed: ${item.speed}")
//                    Text(text = "Technique: ${item.technique}")
//                }
//                Button(onClick = { /* buyGladiator(item) */ }) { // Assuming buyGladiator is defined elsewhere
//                    Text("Buy")
//                }
//            }
//        }
//    }
    companion object {
        const val TAG = "LudusManagementActivity"
    }
}