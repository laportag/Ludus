package com.doorxii.ludus.actions.combatactions

import android.util.Log
import com.doorxii.ludus.data.models.beings.Gladiator
import com.doorxii.ludus.utils.dice.Dice
import com.doorxii.ludus.utils.dice.DiceTypes

class TiredAttack: CombatAction {

    override val name: String = "Tired Attack"
    override val staminaCost: Double = -5.0

    override fun act(aggressor: Gladiator, defender: Gladiator): List<Gladiator> {
        val combatDifference = aggressor.attack - defender.defence
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
        Log.d(TAG, "${defender.name} damage taken: $defenderDamageTaken")
        reduceStamina(aggressor)
        defender.health -= defenderDamageTaken
        Log.d(TAG, "${defender.name} health: ${defender.health}")

        return listOf(aggressor, defender)
    }


    companion object {
        private const val TAG = "BasicDefend"
    }
}