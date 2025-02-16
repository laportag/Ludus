package com.doorxii.ludus.data.models.actions.combatactions

import com.doorxii.ludus.data.models.actions.Action
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.components.cards.CombatActionType

interface CombatAction: Action {

    val staminaCost: Double
    val actionEnum: CombatActions
    val cardType: CombatActionType
    fun act(actor: Gladiator, target: Gladiator?): CombatActionResult

    fun reduceStamina(aggressor: Gladiator){
        aggressor.stamina -= staminaCost
    }

    companion object {
        private const val TAG = "CombatActions"
    }
}