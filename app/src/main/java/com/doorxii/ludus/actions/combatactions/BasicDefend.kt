package com.doorxii.ludus.actions.combatactions

import android.util.Log
import com.doorxii.ludus.data.models.beings.Gladiator

class BasicDefend: CombatAction {

    override val staminaCost: Double = -5.0

    override fun act(aggressor: Gladiator, defender: Gladiator): List<Gladiator> {
        val combatDifference = aggressor.attack - defender.defence
        Log.d(TAG, "Combat difference: $combatDifference")

        var defenderDamageTaken: Double = when (combatDifference) {
            in 0.0..10.0 -> 10.0
            in 10.0..20.0 -> 15.0
            in 20.0..30.0 -> 20.0
            else -> 25.0
        }
        defenderDamageTaken /= 2
        Log.d(TAG, "${defender.name} damage taken: $defenderDamageTaken")
        reduceStamina(aggressor)
        defender.health -= defenderDamageTaken
        Log.d(TAG, "${defender.name} health: ${defender.health}")

        return listOf(aggressor, defender)
    }


    companion object {
        private const val TAG = "BasicDefend"
    }
}