package com.doorxii.ludus.actions

import com.doorxii.ludus.data.models.beings.Gladiator

interface CombatAction: Action {

    fun act(aggressor: Gladiator, defendent: Gladiator): List<Gladiator>

    fun initiateCombatAction(aggressor: Gladiator, defendant: Gladiator): List<Gladiator>{
        act(aggressor, defendant)
        return listOf(aggressor, defendant)
    }

    companion object {
        private const val TAG = "CombatActions"
    }
}