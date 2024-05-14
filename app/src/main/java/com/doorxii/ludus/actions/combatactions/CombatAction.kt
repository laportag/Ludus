package com.doorxii.ludus.actions.combatactions

import com.doorxii.ludus.actions.Action
import com.doorxii.ludus.data.models.animal.Gladiator

interface CombatAction: Action {

    val staminaCost: Double
    fun act(gladiatorList: List<Gladiator>): CombatActionResult

    fun reduceStamina(aggressor: Gladiator){
        aggressor.stamina -= staminaCost
    }

    companion object {
        private const val TAG = "CombatActions"
    }
}