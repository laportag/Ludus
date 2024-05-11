package com.doorxii.ludus.actions.combatactions

import com.doorxii.ludus.actions.Action
import com.doorxii.ludus.data.models.beings.Gladiator

interface CombatAction: Action {

    val staminaCost: Double

    fun act(aggressor: Gladiator, defender: Gladiator): List<Gladiator>

    fun reduceStamina(aggressor: Gladiator){
        aggressor.stamina -= staminaCost
    }

    companion object {
        private const val TAG = "CombatActions"
    }
}