package com.doorxii.ludus.ui.activities

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doorxii.ludus.data.db.LudusRepository
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.ludus.Ludus
import com.doorxii.ludus.utils.GladiatorGenerator.newGladiatorList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


open class LudusManagementActivityViewModel : ViewModel() {

    private lateinit var ludusRepository: LudusRepository

    private val _playerLudus = MutableStateFlow<Ludus?>(null)
    val playerLudus: StateFlow<Ludus?> = _playerLudus.asStateFlow()

    private val _allLudi = MutableStateFlow<List<Ludus>>(emptyList())
    val allLudi: StateFlow<List<Ludus>> = _allLudi.asStateFlow()

    private val _ludiExcludingPlayer = MutableStateFlow<List<Ludus>>(emptyList())
    val ludiExcludingPlayer: StateFlow<List<Ludus>> = _ludiExcludingPlayer.asStateFlow()

    private val _playerGladiators = MutableStateFlow<List<Gladiator>>(emptyList())
    val playerGladiators: StateFlow<List<Gladiator>> = _playerGladiators.asStateFlow()

    private val _selectedEnemyLudus = MutableStateFlow<Ludus?>(null)
    val selectedEnemyLudus: StateFlow<Ludus?> = _selectedEnemyLudus.asStateFlow()

    private val _gladiatorsByLudus = MutableStateFlow<List<Gladiator>>(emptyList())
    val gladiatorsByLudus: StateFlow<List<Gladiator>> = _gladiatorsByLudus.asStateFlow()

    private val _marketGladiatorList = MutableStateFlow<List<Gladiator>>(emptyList())
    val marketGladiatorList: StateFlow<List<Gladiator>> = _marketGladiatorList.asStateFlow()

    private val _selectedPlayerGladiators: MutableState<List<Gladiator?>> =
        mutableStateOf(emptyList())
    private val _selectedEnemyGladiators: MutableState<List<Gladiator?>> =
        mutableStateOf(emptyList())
    private val _isCombatReady: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isCombatStarted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isCombatSimmed: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isCombatResultShown: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _combatResultText: MutableStateFlow<String> = MutableStateFlow("")

    val selectedPlayerGladiators: State<List<Gladiator?>> = _selectedPlayerGladiators
    val selectedEnemyGladiators: State<List<Gladiator?>> = _selectedEnemyGladiators
    val isCombatReady: StateFlow<Boolean> = _isCombatReady
    val isCombatStarted: StateFlow<Boolean> = _isCombatStarted
    val isCombatSimmed: StateFlow<Boolean> = _isCombatSimmed
    val isCombatResultShown: StateFlow<Boolean> = _isCombatResultShown
    val combatResultText: StateFlow<String> = _combatResultText

    init {
        Log.d(TAG, "init vm")

    }

    fun start(repo: LudusRepository) {
        ludusRepository = repo
        viewModelScope.launch {
            // Fetch initial data from the database
            ludusRepository.getPlayerLudus().collect { ludus ->
                _playerLudus.value = ludus
                Log.d(TAG, "playerLudus: $ludus")
            }
        }
        viewModelScope.launch {
            ludusRepository.getAllLudi().collect { ludusList ->
                Log.d(TAG, "getAllLudi: $ludusList")
                _allLudi.value = ludusList
            }
            getLudiExcludingPlayer()
        }

        viewModelScope.launch {
            getPlayerGladiators(playerLudus.value?.ludusId ?: 0)

        }

        generateMarketGladiatorList()

    }

    fun setSelectedEnemyLudus(ludus: Ludus) {
        viewModelScope.launch {
            _selectedEnemyLudus.value = ludus
        }
    }

    fun updatePlayerLudus(newLudus: Ludus) {
        viewModelScope.launch {
            ludusRepository.updateLudus(newLudus)
            _playerLudus.value = newLudus // Update the local state
        }
    }

    fun setSelectedPlayerGladiators(gladiators: List<Gladiator?>) {
        _selectedPlayerGladiators.value = gladiators
    }

    fun setSelectedEnemyGladiators(gladiators: List<Gladiator?>) {
        _selectedEnemyGladiators.value = gladiators
    }

    fun setGladiatorsByLudus(gladiators: List<Gladiator>) {
        _gladiatorsByLudus.value = gladiators
    }

    fun getAllLudi(): List<Ludus> {
        viewModelScope.launch {
            ludusRepository.getAllLudi().collect { ludusList ->
                Log.d(TAG, "getAllLudi: $ludusList")
                _allLudi.value = ludusList
            }
        }
        return allLudi.value
    }

    fun getLudiExcludingPlayer() {
        viewModelScope.launch {
            val list = allLudi.value.toMutableList()
            for (ludus in allLudi.value) {
                if (ludus.ludusId == playerLudus.value?.ludusId) {
                    list.remove(ludus)
                }
            }
            _ludiExcludingPlayer.value = list
            Log.d(TAG, "getLudiExcludingPlayer: ${ludiExcludingPlayer.value}")
        }
    }

    fun getGladiatorsByLudusId(ludusId: Int): List<Gladiator> {
        viewModelScope.launch {
            ludusRepository.getGladiatorsByLudusId(ludusId).collect { gladiators ->
                Log.d(TAG, "getGladiatorsByLudusId $ludusId: $gladiators")
                val list = gladiators.toMutableList()
                for (gladiator in gladiators) {
                    if (!gladiator.isAlive()) {
                        list.remove(gladiator)
                    }
                }
                _gladiatorsByLudus.value = list
            }
        }
        return gladiatorsByLudus.value
    }

    fun getPlayerGladiators(id: Int) {
        viewModelScope.launch {
            ludusRepository.getGladiatorsByLudusId(id).collect { gladiators ->
                Log.d(TAG, "getPlayerGladiators: $gladiators")
                val list = gladiators.toMutableList()
                for (gladiator in gladiators) {
                    if (!gladiator.isAlive()) {
                        list.remove(gladiator)
                    }
                }
                _playerGladiators.value = list
            }
        }
    }

    fun updateGladiator(gladiator: Gladiator) {
        viewModelScope.launch {
            ludusRepository.updateGladiator(gladiator)
        }
    }

    fun generateMarketGladiatorList() {
        viewModelScope.launch {
            val list = newGladiatorList(5)
            _marketGladiatorList.value = list
        }
    }

    fun addGladiatorToPlayer(gladiator: Gladiator) {
        Log.d(TAG, "addGladiatorToPlayer: $gladiator")
        viewModelScope.launch {
            Log.d(TAG, "addGladiatorToPlayer id: $gladiator.ludusId")
            gladiator.ludusId = playerLudus.value?.ludusId!!
            ludusRepository.insertGladiator(gladiator)
            getPlayerGladiators(playerLudus.value?.ludusId ?: 0)
        }
        removeGladiatorFromMarketList(gladiator)
    }

    fun removeGladiatorFromMarketList(gladiator: Gladiator) {
        viewModelScope.launch {
            val list = marketGladiatorList.value.toMutableList()
            for (marketGladiator in marketGladiatorList.value) {
                if (marketGladiator.ludusId != -1) {
                    list.remove(marketGladiator)
                }
            }
            _marketGladiatorList.value = list
        }
    }

    fun updateCombatReadiness() {
        viewModelScope.launch {
            _isCombatReady.value =
                selectedEnemyLudus.value != null && selectedPlayerGladiators.value.isNotEmpty()
        }
    }

    fun startCombat() {
        Log.d(TAG, "startCombat: vm")
        viewModelScope.launch {
            Log.d(TAG, "startCombat: vm scope")
//            val playerGladiators = selectedPlayerGladiators.value.filterNotNull()
//            val enemyGladiators = selectedEnemyGladiators.value.filterNotNull()
            _isCombatStarted.value = true
        }
    }

    fun simCombat() {
        viewModelScope.launch {
            _isCombatSimmed.value = true
        }
    }

    fun resetCombatSimmed() {
        viewModelScope.launch {
            _isCombatStarted.value = false
        }
    }

    fun resetCombatStarted() {
        viewModelScope.launch {
            _isCombatStarted.value = false
        }
    }

    fun setCombatResultShown(shown: Boolean) {
        viewModelScope.launch {
            _isCombatResultShown.value = shown
        }
    }

    fun setCombatResultText(text: String) {
        viewModelScope.launch {
            _combatResultText.value = text
        }
    }


    companion object {
        private const val TAG = "LudusManagementActivityViewModel"
    }
}