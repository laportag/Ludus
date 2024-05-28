package com.doorxii.ludus.activities

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doorxii.ludus.data.db.LudusRepository
import com.doorxii.ludus.data.models.ludus.Ludus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


open class  LudusManagementActivityViewModel(): ViewModel() {

    private lateinit var ludusRepository: LudusRepository
    private val _playerLudus = MutableStateFlow<Ludus?>(null)
    val playerLudus: StateFlow<Ludus?> = _playerLudus.asStateFlow()

    fun setRespository(repo: LudusRepository){
        ludusRepository  = repo
    }

    init {
        Log.d(TAG, "init vm")
    }

    fun setPlayerLudus(){
        viewModelScope.launch {
            _playerLudus.value = ludusRepository.getPlayerLudus().first()
        }
    }

    companion object {
        private const val TAG = "LudusManagementActivityViewModel"
    }
}