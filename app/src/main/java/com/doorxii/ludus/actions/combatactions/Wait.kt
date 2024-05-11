package com.doorxii.ludus.actions.combatactions

import com.doorxii.ludus.data.models.beings.Gladiator

class Wait: CombatAction {

    override val staminaCost: Double = -20.0
    override fun act(aggressor: Gladiator, defender: Gladiator): List<Gladiator> {
        reduceStamina(aggressor)
        return listOf(aggressor, defender)
    }
}