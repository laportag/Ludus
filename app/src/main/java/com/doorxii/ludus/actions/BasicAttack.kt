package com.doorxii.ludus.actions

import android.util.Log
import com.doorxii.ludus.data.models.beings.Gladiator

class BasicAttack: CombatAction {
    override fun act(aggressor: Gladiator, defendent: Gladiator): List<Gladiator> {
        Log.d(TAG, "Basic attack: ${aggressor.name} vs ${defendent.name}")

        val combatDifference = aggressor.attack - defendent.defence
        Log.d(TAG, "Combat difference: $combatDifference")

        val defendentDamage: Double = when (combatDifference) {
            in 0.0..10.0 -> 10.0
            in 10.0..20.0 -> 15.0
            in 20.0..30.0 -> 20.0
            else -> 25.0
        }

        defendent.health -= defendentDamage
        Log.d(TAG, "Defendent health: ${defendent.health}")

        return listOf(aggressor, defendent)
    }

    companion object{
        const val TAG = "BasicAttack"

    }
}