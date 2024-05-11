package com.doorxii.ludus.actions

import android.util.Log
import com.doorxii.ludus.data.models.beings.Gladiator

class BasicAttack: CombatAction {
    override fun act(aggressor: Gladiator, defender: Gladiator): List<Gladiator> {
        Log.d(TAG, "Basic attack: ${aggressor.name} vs ${defender.name}")

        val combatDifference = aggressor.attack - defender.defence
        Log.d(TAG, "Combat difference: $combatDifference")

        val defenderDamageTaken: Double = when (combatDifference) {
            in 0.0..10.0 -> 10.0
            in 10.0..20.0 -> 15.0
            in 20.0..30.0 -> 20.0
            else -> 25.0
        }
        Log.d(TAG, "${defender.name} damage taken: $defenderDamageTaken")
        defender.health -= defenderDamageTaken
        Log.d(TAG, "${defender.name} health: ${defender.health}")

        return listOf(aggressor, defender)
    }

    companion object{
        const val TAG = "BasicAttack"

    }
}