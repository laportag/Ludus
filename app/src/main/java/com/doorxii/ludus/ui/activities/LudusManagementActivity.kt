package com.doorxii.ludus.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.data.db.LudusRepository
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.ludus.Ludus
import com.doorxii.ludus.ui.components.screens.LudusManagementScreen
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

    private val allLudi = mutableStateOf<List<Ludus>>(emptyList())
    private val ludiExcludingPlayer = mutableStateOf<List<Ludus>>(emptyList())
    private val selectedEnemyLudus = mutableStateOf<Ludus?>(null)
    private val playerGladiators = mutableStateOf<List<Gladiator>>(emptyList())
    private val gladiatorsInSelectedEnemyLudus = mutableStateOf<List<Gladiator>>(emptyList())

//    private var marketGladiatorList = mutableStateOf<List<Gladiator>>(emptyList())

    private lateinit var combatFile: File
    private var combat: Combat? = null

//    private val combatResultAlertEnabled = mutableStateOf(false)
//    private val combatResultText = mutableStateOf("")

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
//        observeStates()
        viewModel.start(repository)
        Log.d(TAG, "player ludus on create: ${viewModel.playerLudus.value}")


        enableEdgeToEdge()
        setContent {
            LudusTheme {
                LudusManagementScreen(viewModel = viewModel)
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

                    if (it != null) {
                        Log.d(TAG, "obServeFlow selectedEnemyLudus: $it")
                        selectedEnemyLudus.value = it
                        viewModel.setGladiatorsByLudus(viewModel.getGladiatorsByLudusId(it.ludusId))
                    }
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

        // launch combat
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isCombatStarted.collect { combatStarted ->
                    Log.d(TAG, "observeStates combatStarted: ${combatStarted}")
                    if (combatStarted) {
                        startCombat()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isCombatSimmed.collect {
                    Log.d(TAG, "observeStates combatSimmed: ${it}")
                    if (it) {
                        simCombat()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isCombatResultShown.collect {
                    Log.d(TAG, "observeStates isCombatResultShown: ${it}")
//                    viewModel.setCombatResultShown(it)
                }
            }
        }
    }

    private fun startCombat() {
        Log.d(TAG, "startCombat")
        viewModel.resetCombatStarted()
        combatFile = CombatSerialization.returnCombatFile(applicationContext)
        combat =
            Combat.init(
                viewModel.selectedPlayerGladiators.value,
                viewModel.selectedEnemyGladiators.value
            )
        CombatSerialization.saveCombatJson(combat!!, combatFile)
        Log.d(TAG, "combatFile: " + combatFile.readText())
        val uri = combatFile.toUri()
        val intent = Intent(this, CombatActivity::class.java)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, contentResolver.getType(uri))
        viewModel.setSelectedEnemyGladiators(emptyList())
        viewModel.setSelectedPlayerGladiators(emptyList())
        combatResultLauncher.launch(intent)
    }

    private fun simCombat() {
        viewModel.resetCombatSimmed()
        Log.d(TAG, "simCombat")
        combat =
            Combat.init(
                viewModel.selectedPlayerGladiators.value,
                viewModel.selectedEnemyGladiators.value
            )
        viewModel.setSelectedEnemyGladiators(emptyList())
        viewModel.setSelectedPlayerGladiators(emptyList())
        val res = combat!!.simCombat()
        Log.d(TAG, "res: " + res.combatReport)
        combatFinished(combat!!)
    }

    private fun combatFinished(resCombat: Combat) {
        Log.d(TAG, "Combat complete?: ${resCombat.isComplete}")

        var winners = emptyList<Gladiator>()
        if (resCombat.playerGladiatorList.isEmpty() && resCombat.enemyGladiatorList.isEmpty()) {
            Log.d(TAG, "No winner")
            return
        } else if (resCombat.playerGladiatorList.isEmpty()) {
            Log.d(TAG, "Player loses")
            winners = resCombat.enemyGladiatorList
        } else if (resCombat.enemyGladiatorList.isEmpty()) {
            Log.d(TAG, "Player wins")
            winners = resCombat.playerGladiatorList
        } else {
            Log.d(TAG, "Combat still going, shouldn't have returned here")
        }

        Log.d(TAG, "Winners: ${winners.joinToString(", ") { it.name }}")

        val allGladiators =
            resCombat.originalPlayerGladiatorList + resCombat.originalEnemyGladiatorList
        allGladiators.forEach { gladiator ->
            if (gladiator !in resCombat.playerGladiatorList && gladiator !in resCombat.enemyGladiatorList) {
                gladiator.health = 0.0 // Mark dead gladiators
            }
            viewModel.updateGladiator(gladiator)
        }
        viewModel.getGladiatorsByLudusId(selectedEnemyLudus.value!!.ludusId)
        viewModel.getPlayerGladiators(ludus.value!!.ludusId)
        viewModel.setCombatResultText(resCombat.combatReport)
        viewModel.setCombatResultShown(true)
        Log.d(TAG, "combatResultText: ${viewModel.combatResultText.value}")
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

    companion object {
        const val TAG = "LudusManagementActivity"
    }
}