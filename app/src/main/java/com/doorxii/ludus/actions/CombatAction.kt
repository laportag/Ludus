package com.doorxii.ludus.actions

import com.doorxii.ludus.data.models.beings.Gladiator

interface CombatAction: Action {

    fun act(aggressor: Gladiator, defender: Gladiator): List<Gladiator>

    companion object {
        private const val TAG = "CombatActions"
    }
}