package com.doorxii.ludus.data.models.actions.combatactions

import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.components.cards.CombatActionType

class Missio: CombatAction {

    override val name: String = "Missio"
    override val description: String = "Beg for mercy"
    override val staminaCost: Double = 0.0
    override val actionEnum: CombatActions = CombatActions.MISSIO
    override val cardType: CombatActionType = CombatActionType.Targetless

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