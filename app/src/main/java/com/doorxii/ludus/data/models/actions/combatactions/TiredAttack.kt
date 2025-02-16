package com.doorxii.ludus.data.models.actions.combatactions

import android.util.Log
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.components.cards.CombatActionType
import com.doorxii.ludus.utils.dice.Dice
import com.doorxii.ludus.utils.dice.DiceTypes

class TiredAttack: CombatAction {

    override val name: String = "Tired Attack"
    override val description: String = "Tired Attack"
    override val staminaCost: Double = -5.0
    override val actionEnum: CombatActions = CombatActions.TIRED_ATTACK
    override val cardType= CombatActionType.Targeted

    override fun act(actor: Gladiator, target: Gladiator?): CombatActionResult {
        val combatDifference = actor.attack - (target?.defence ?: 0.0)
        Log.d(TAG, "Combat difference: $combatDifference")

        var defenderDamageTaken: Double = when (combatDifference) {
            in 0.0..10.0 -> {
                Dice.totalRolls(Dice.roll(2, DiceTypes.D6), Dice.calculateModifier(2.0))
            }
            in 10.0..20.0 -> {
                Dice.totalRolls(Dice.roll(3, DiceTypes.D6), Dice.calculateModifier(2.0))
            }
            in 20.0..30.0 -> {
                Dice.totalRolls(Dice.roll(4, DiceTypes.D6), Dice.calculateModifier(2.0))
            }
            else -> {
                Dice.totalRolls(Dice.roll(5, DiceTypes.D6), Dice.calculateModifier(2.0))
            }
        }
        defenderDamageTaken /= 2
        return CombatActionResult(
            actor,
            target,
            defenderDamageTaken,
            staminaCost
        )

    }

    companion object {
        private const val TAG = "TiredAttack"
    }
}