package com.doorxii.ludus.combat

import android.util.Log
import com.doorxii.ludus.actions.CombatAction
import com.doorxii.ludus.data.models.beings.Gladiator

class CombatRound(
    gladiatorA: Gladiator,
    gladiatorB: Gladiator,
    actionA: CombatAction,
    actionB: CombatAction,
    round: Int
) {

    // Each round has 2 actions, one for each gladiator
    var gladiatorA = gladiatorA
    var gladiatorB = gladiatorB
    val actionA = actionA
    val actionB = actionB
    val round = round

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
        Log.d(TAG, "Initiating round $round: ${gladiatorA.name} health: ${gladiatorA.health} vs ${gladiatorB.name} health: ${gladiatorB.health}")
        if (determineBeginner(gladiatorA, gladiatorB) == gladiatorA) {

            Log.d(TAG, "Gladiator A goes first")
            val actionARes = actionA.act(gladiatorA, gladiatorB)
            updateGladiators(actionARes)

            if (!gladiatorB.isAlive()){
                return listOf(gladiatorA)
            }
            else if (!gladiatorA.isAlive()){
                return listOf(gladiatorB)
            }

            val actionBRes = actionB.act(gladiatorB, gladiatorA)
            updateGladiators(actionBRes)

            if (!gladiatorA.isAlive()){
                return listOf(gladiatorB)
            }
            else if (!gladiatorB.isAlive()){
                return listOf(gladiatorA)
            }

        } else {
            Log.d(TAG, "Gladiator B goes first")
            val actionBRes = actionB.act(gladiatorB, gladiatorA)
            updateGladiators(actionBRes)

            if (!gladiatorA.isAlive()){
                return listOf(gladiatorB)
            }
            else if (!gladiatorB.isAlive()){
                return listOf(gladiatorA)
            }

            val actionARes = actionA.act(gladiatorA, gladiatorB)
            updateGladiators(actionARes)

            if (!gladiatorB.isAlive()){
                return listOf(gladiatorA)
            }
            else if (!gladiatorA.isAlive()){
                return listOf(gladiatorB)
            }
        }

        Log.d(
            TAG,
            "Combat round result: ${gladiatorA.name} health: ${gladiatorA.health} vs ${gladiatorB.name} health: ${gladiatorB.health}"
        )
        return listOf(gladiatorA, gladiatorB)
    }

    fun updateGladiators(gladiators: List<Gladiator>) {
        gladiatorA = gladiators[0]
        gladiatorB = gladiators[1]
    }


    companion object {
        private const val TAG = "CombatRound"
    }
}