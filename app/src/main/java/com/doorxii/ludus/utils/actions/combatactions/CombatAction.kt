package com.doorxii.ludus.utils.actions.combatactions

import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.utils.actions.Action

interface CombatAction: Action {

    val staminaCost: Double
    val actionEnum: CombatActions
    fun act(actor: Gladiator, target: Gladiator): CombatActionResult

    fun reduceStamina(aggressor: Gladiator){
        aggressor.stamina -= staminaCost
    }

    companion object {
        private const val TAG = "CombatActions"
    }
}