package com.doorxii.ludus.combat

import android.util.Log
import com.doorxii.ludus.actions.CombatAction
import com.doorxii.ludus.data.models.beings.Gladiator

class CombatRound(
    gladiatorA: Gladiator,
    gladiatorB: Gladiator,
    actionA: CombatAction,
    actionB: CombatAction
) {

    // Each round has 2 actions, one for each gladiator
    val gladiatorA = gladiatorA
    val gladiatorB = gladiatorB
    val actionA = actionA
    val actionB = actionB

    fun determineBeginner(gladiatorA: Gladiator, gladiatorB: Gladiator): Gladiator {
        val beginner: Gladiator
        if (gladiatorA.speed > gladiatorB.speed) {
            beginner = gladiatorA
        } else if (gladiatorA.speed < gladiatorB.speed) {
            beginner = gladiatorB
        } else {
            if (gladiatorA.attack > gladiatorB.attack) {
                beginner = gladiatorA
            } else if (gladiatorA.attack < gladiatorB.attack) {
                beginner = gladiatorB
            } else {
                if (gladiatorA.age < gladiatorB.age) {
                    beginner = gladiatorA
                } else if (gladiatorA.age > gladiatorB.age) {
                    beginner = gladiatorB
                } else {
                    beginner = gladiatorA
                }
            }
        }
        return beginner
    }

    fun initiateRound(): List<Gladiator> {
        if (determineBeginner(gladiatorA, gladiatorB) == gladiatorA) {
            actionA.initiateCombatAction(gladiatorA, gladiatorB)
            actionB.initiateCombatAction(gladiatorB, gladiatorA)

        } else {
            actionB.initiateCombatAction(gladiatorB, gladiatorA)
            actionA.initiateCombatAction(gladiatorA, gladiatorB)
        }

        Log.d(
            TAG,
            "Combat results: ${gladiatorA.name} health: ${gladiatorA.health} vs ${gladiatorB.name} health: ${gladiatorB.health}"
        )
        return listOf(gladiatorA, gladiatorB)
    }

    companion object {
        private const val TAG = "CombatRound"
    }
}