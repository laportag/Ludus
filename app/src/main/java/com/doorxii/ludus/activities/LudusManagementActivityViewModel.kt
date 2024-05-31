package com.doorxii.ludus.activities

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doorxii.ludus.data.db.LudusRepository
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


    companion object {
        private const val TAG = "LudusManagementActivityViewModel"
    }
}