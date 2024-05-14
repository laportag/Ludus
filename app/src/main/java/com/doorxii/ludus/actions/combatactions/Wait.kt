package com.doorxii.ludus.actions.combatactions

import com.doorxii.ludus.data.models.animal.Gladiator

class Wait : CombatAction {

    override val name: String = "Wait"
    override val staminaCost: Double = -20.0
    override fun act(gladiatorList: List<Gladiator>): CombatActionResult {
        return CombatActionResult(
            gladiatorList[0],
            gladiatorList[1],
            0.0,
            staminaCost
        )
    }
}