package com.doorxii.ludus.activities

import android.util.Log
import androidx.lifecycle.ViewModel
import com.doorxii.ludus.actions.combatactions.ChosenAction
import com.doorxii.ludus.data.models.animal.Gladiator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CombatActivityViewModel: ViewModel() {
    private val _gladiatorActions = MutableStateFlow<Map<Gladiator, ChosenAction?>>(emptyMap())
    val gladiatorActions: StateFlow<Map<Gladiator, ChosenAction?>> = _gladiatorActions.asStateFlow()

    fun updateGladiatorAction(gladiator: Gladiator, chosenAction: ChosenAction?) {
        Log.d(TAG, "actions before: ${gladiatorActions.value.values.all { it != null }}")
        _gladiatorActions.value += (gladiator to chosenAction) //Use plusAssign
        Log.d(TAG, "actions after: ${gladiatorActions.value.values.all { it != null }}")
    }

    fun haveAllGladiatorsHadATurn(): Boolean {
        return gladiatorActions.value.values.all { it != null }
    }

    fun hasGladiatorHadATurn(gladiator: Gladiator): Boolean {
        return gladiatorActions.value[gladiator] != null
    }

    fun resetGladiatorActions() {
        _gladiatorActions.value = _gladiatorActions.value.mapValues { null }
    }

    companion object {
        const val TAG = "CombatActivityViewModel"
    }
}