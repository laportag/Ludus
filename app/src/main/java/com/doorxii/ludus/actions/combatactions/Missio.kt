package com.doorxii.ludus.actions.combatactions

import com.doorxii.ludus.data.models.animal.Gladiator

class Missio(
    override val name: String = "Missio",
    override val description: String = "Beg for mercy",
    override val staminaCost: Double = 0.0,
    override val actionEnum: CombatActions = CombatActions.MISSIO
) : CombatAction {

    override fun act(actor: Gladiator, target: Gladiator): CombatActionResult {
        actor.morale = 0.0
        return CombatActionResult(
            actor,
            target,
            0.0,
            0.0
        )
    }
}