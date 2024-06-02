package com.doorxii.ludus.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StartActivityViewModel: ViewModel() {

    private val _databases = MutableStateFlow<List<String>>(emptyList())
    val databases: StateFlow<List<String>> = _databases.asStateFlow()

    fun setDatabases(databases: List<String>) {
        viewModelScope.launch {
            _databases.value = databases
        }
    }

    companion object {
        private const val TAG = "StartActivityViewModel"
    }
}