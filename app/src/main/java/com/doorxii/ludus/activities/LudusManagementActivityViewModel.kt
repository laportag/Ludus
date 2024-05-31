package com.doorxii.ludus.activities

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doorxii.ludus.data.db.LudusRepository
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.ludus.Ludus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


open class  LudusManagementActivityViewModel: ViewModel() {

    private lateinit var ludusRepository: LudusRepository

    private val _playerLudus = MutableStateFlow<Ludus?>(null)
    val playerLudus: StateFlow<Ludus?> = _playerLudus.asStateFlow()

    private val _allLudi = MutableStateFlow<List<Ludus>>(emptyList())
    val allLudi: StateFlow<List<Ludus>> = _allLudi.asStateFlow()

    private val _ludiExcludingPlayer = MutableStateFlow<List<Ludus>>(emptyList())
    val ludiExcludingPlayer: StateFlow<List<Ludus>> = _ludiExcludingPlayer.asStateFlow()

    private val _playerGladiators = MutableStateFlow<List<Gladiator>>(emptyList())
    val playerGladiators: StateFlow<List<Gladiator>> = _playerGladiators.asStateFlow()

    private val _gladiatorsByLudus = MutableStateFlow<List<Gladiator>>(emptyList())
    val gladiatorsByLudus: StateFlow<List<Gladiator>> = _gladiatorsByLudus.asStateFlow()


    init {
        Log.d(TAG, "init vm")

    }

    fun start(repo: LudusRepository){
        ludusRepository  = repo
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
            _ludiExcludingPlayer.value = getLudiExcludingPlayer()
        }

        viewModelScope.launch {
            getPlayerGladiators(playerLudus.value?.ludusId ?: 0)

        }


    }

    fun updatePlayerLudus(newLudus: Ludus) {
        viewModelScope.launch {
            ludusRepository.updateLudus (newLudus)
            _playerLudus.value = newLudus // Update the local state
        }
    }

    fun getAllLudi(): List<Ludus>{
        viewModelScope.launch {
            ludusRepository.getAllLudi().collect { ludusList ->
                Log.d(TAG, "getAllLudi: $ludusList")
                _allLudi.value = ludusList
            }
        }
        return allLudi.value
    }

    fun getLudiExcludingPlayer(): List<Ludus> {
        return allLudi.value.filter { it != playerLudus.value }
    }

    fun getGladiatorsByLudusId(ludusId: Int): List<Gladiator> {
        viewModelScope.launch {
            ludusRepository.getGladiatorsByLudusId(ludusId).collect { gladiators ->
                Log.d(TAG, "getGladiatorsByLudusId $ludusId: $gladiators")
                _gladiatorsByLudus.value = gladiators
            }
        }
        return gladiatorsByLudus.value
    }

    fun getPlayerGladiators(id: Int) {
        viewModelScope.launch {
            ludusRepository.getGladiatorsByLudusId(id).collect { gladiators ->
                Log.d(TAG, "getPlayerGladiators: $gladiators")
                _playerGladiators.value = gladiators
            }
        }
    }


    companion object {
        private const val TAG = "LudusManagementActivityViewModel"
    }
}