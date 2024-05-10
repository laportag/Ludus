package com.doorxii.ludus.actions

import com.doorxii.ludus.data.models.beings.Gladiator

class CombatActions: Actions() {

    fun attack(aggressor: Gladiator, defender: Gladiator){
        val combatDifference = aggressor.attack - defender.defence
        val damage: Double = when (combatDifference) {
            in 0.0..50.0 -> 10.0
            else -> 20.0
        }

    }

    fun defend(){

    }
}