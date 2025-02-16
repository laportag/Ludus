package com.doorxii.ludus.data.models.actions.combatactions

import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.components.cards.CombatActionType

class Wait : CombatAction {

    override val name: String = "Wait"
    override val description: String = "Do Nothing"
    override val staminaCost: Double = -20.0
    override val actionEnum: CombatActions = CombatActions.WAIT
    override val cardType = CombatActionType.Targetless

    override fun act(actor: Gladiator, target: Gladiator?): CombatActionResult {
        return CombatActionResult(
            actor,
            target,
            0.0,
            staminaCost
        )
    }
}