package com.doorxii.ludus.combat

import android.util.Log
import com.doorxii.ludus.actions.combatactions.CombatActionResult
import com.doorxii.ludus.data.models.animal.Gladiator

class CombatRound(
    // Each round has 2 actions, one for each gladiator
    private var gladiatorList: List<Gladiator>, private val round: Int
) {

    private lateinit var beginner: Gladiator
    var roundReport: String = ""

    private fun determineBeginner(): Gladiator {
        val beginner: Gladiator
        if (gladiatorList[0].speed > gladiatorList[1].speed) {
            beginner = gladiatorList[0]
        } else if (gladiatorList[0].speed < gladiatorList[1].speed) {
            beginner = gladiatorList[1]
        } else {
            beginner = if (gladiatorList[0].attack > gladiatorList[1].attack) {
                gladiatorList[0]
            } else if (gladiatorList[0].attack < gladiatorList[1].attack) {
                gladiatorList[1]
            } else {
                if (gladiatorList[0].age < gladiatorList[1].age) {
                    gladiatorList[0]
                } else if (gladiatorList[0].age > gladiatorList[1].age) {
                    gladiatorList[1]
                } else {
                    gladiatorList[0]
                }
            }
        }
        return beginner
    }

    private fun appendReport(str: String) {
        Log.d(TAG, str)
        roundReport += "$str\n"
    }

    fun run(gladiatorList: List<Gladiator>): List<Gladiator> {
        var gladiatorList = gladiatorList
        val beginner = determineBeginner()

        if (beginner == gladiatorList[0]) {
            // A Starts
            val resA = gladiatorList[0].action?.act(gladiatorList)!!
            gladiatorList = updateFromCombatActionResult(resA)
            if (!gladiatorList[1].isAlive()) {
                return listOf(gladiatorList[0])
            } else if (!gladiatorList[0].isAlive()) {
                return listOf(gladiatorList[1])
            }

            val resB = gladiatorList[1].action?.act(gladiatorList)
            gladiatorList = updateFromCombatActionResult(resB!!)
            if (!gladiatorList[0].isAlive()) {
                return listOf(gladiatorList[1])
            } else if (!gladiatorList[1].isAlive()) {
                return listOf(gladiatorList[0])
            }

//            if dead return 1
        } else {
            // B Starts
            val resB = gladiatorList[1].action?.act(gladiatorList)
            gladiatorList = updateFromCombatActionResult(resB!!)
            if (!gladiatorList[0].isAlive()) {
                return listOf(gladiatorList[1])
            } else if (!gladiatorList[1].isAlive()) {
                return listOf(gladiatorList[0])
            }

            val resA = gladiatorList[0].action?.act(gladiatorList)
            gladiatorList = updateFromCombatActionResult(resA!!)
            if (!gladiatorList[1].isAlive()) {
                return listOf(gladiatorList[0])
            } else if (!gladiatorList[0].isAlive()) {
                return listOf(gladiatorList[1])
            }

        }

        appendReport(
            "Combat round result: ${gladiatorList[0].name} health: ${gladiatorList[0].health} vs ${gladiatorList[1].name} health: ${gladiatorList[1].health}"
        )
        return listOf(gladiatorList[0], gladiatorList[1])
    }
    
    fun updateFromCombatActionResult(result: CombatActionResult): List<Gladiator> {
        val gladiatorList = gladiatorList
        Log.d(TAG, "updating from action result...")
        Log.d(TAG, "CombatActionResult: $result")
        for (gladiator in gladiatorList){
            if (gladiator.id == result.source.id){
                Log.d(TAG, "gladiator: ${gladiator.name} losing ${result.deltaStamina} stamina")
                gladiator.stamina -= result.deltaStamina
            }
            if (gladiator.id == result.target.id){
                Log.d(TAG, "gladiator: ${gladiator.name} losing ${result.deltaHealth} health")
                gladiator.health -= result.deltaHealth
            }
        }
        return gladiatorList
    }


    companion object {
        private const val TAG = "CombatRound"
        fun init(
            gladiatorList: List<Gladiator>, round: Int
        ): CombatRound {
            return CombatRound(gladiatorList, round)
        }
    }
}