package com.doorxii.ludus.combat

import android.util.Log
import com.doorxii.ludus.data.models.animal.Gladiator

class CombatRound(
    // Each round has 2 actions, one for each gladiator
    private var gladiatorA: Gladiator, private var gladiatorB: Gladiator, private val round: Int
) {

    private lateinit var beginner: Gladiator
    var roundReport: String = ""

    private fun determineBeginner(): Gladiator {
        val beginner: Gladiator
        if (gladiatorA.speed > gladiatorB.speed) {
            beginner = gladiatorA
        } else if (gladiatorA.speed < gladiatorB.speed) {
            beginner = gladiatorB
        } else {
            beginner = if (gladiatorA.attack > gladiatorB.attack) {
                gladiatorA
            } else if (gladiatorA.attack < gladiatorB.attack) {
                gladiatorB
            } else {
                if (gladiatorA.age < gladiatorB.age) {
                    gladiatorA
                } else if (gladiatorA.age > gladiatorB.age) {
                    gladiatorB
                } else {
                    gladiatorA
                }
            }
        }
        return beginner
    }

    private fun appendReport(str: String) {
        Log.d(TAG, str)
        roundReport += "$str\n"
    }

    fun run(): List<Gladiator> {
        beginner = determineBeginner()

        appendReport("Initiating round $round: ${gladiatorA.name} health: ${gladiatorA.health} vs ${gladiatorB.name} health: ${gladiatorB.health}")
        if (beginner == gladiatorA) {

            appendReport("Gladiator A goes first")

            // Gladiator A action
            updateGladiators(gladiatorA.action?.act(gladiatorA, gladiatorB)!!)

            if (!gladiatorB.isAlive()) {
                return listOf(gladiatorA)
            } else if (!gladiatorA.isAlive()) {
                return listOf(gladiatorB)
            }

            // Gladiator B action
            updateGladiators(gladiatorB.action?.act(gladiatorB, gladiatorA)!!)

            if (!gladiatorA.isAlive()) {
                return listOf(gladiatorB)
            } else if (!gladiatorB.isAlive()) {
                return listOf(gladiatorA)
            }

        } else {
            appendReport("Gladiator B goes first")

            // Gladiator B action
            updateGladiators(gladiatorB.action?.act(gladiatorB, gladiatorA)!!)

            if (!gladiatorA.isAlive()) {
                return listOf(gladiatorB)
            } else if (!gladiatorB.isAlive()) {
                return listOf(gladiatorA)
            }

            // Gladiator A action
            updateGladiators(gladiatorA.action?.act(gladiatorA, gladiatorB)!!)

            if (!gladiatorB.isAlive()) {
                return listOf(gladiatorA)
            } else if (!gladiatorA.isAlive()) {
                return listOf(gladiatorB)
            }
        }

        appendReport(
            "Combat round result: ${gladiatorA.name} health: ${gladiatorA.health} vs ${gladiatorB.name} health: ${gladiatorB.health}"
        )
        return listOf(gladiatorA, gladiatorB)
    }

    private fun updateGladiators(gladiators: List<Gladiator>) {
        gladiatorA = gladiators[0]
        gladiatorB = gladiators[1]
    }


    companion object {
        private const val TAG = "CombatRound"
        fun init(
            gladiatorA: Gladiator, gladiatorB: Gladiator, round: Int
        ): CombatRound {
            return CombatRound(gladiatorA, gladiatorB, round)
        }
    }
}