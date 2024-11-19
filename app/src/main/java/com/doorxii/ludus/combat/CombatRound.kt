package com.doorxii.ludus.combat

import android.util.Log
import com.doorxii.ludus.actions.combatactions.CombatActionResult
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.utils.dice.Dice
import com.doorxii.ludus.utils.dice.DiceTypes
import com.doorxii.ludus.utils.enums.EnumToAction.combatEnumToAction
import kotlinx.serialization.Serializable

@Serializable
class CombatRound {
    private var playerGladiatorList: List<Gladiator> = mutableListOf()
    private var enemyGladiatorList: List<Gladiator> = mutableListOf()
    private var round: Int? = null
    var roundReport: String = ""
    private var roundOrder: List<Map<Int, Double>> = mutableListOf()

    private fun determineRoundOrder() {
        val gladiatorList = playerGladiatorList + enemyGladiatorList
        roundOrder = gladiatorList.associate {
            it.gladiatorId to determineGladiatorInitiative(it)
        }.map {
            mapOf(it.key to it.value)
        }.toMutableList().sortedByDescending { it.values.maxOrNull() }
    }

    private fun determineGladiatorInitiative(gladiator: Gladiator): Double {
        return Dice.totalRolls(
            Dice.roll(
                1,
                DiceTypes.D20
            )
        ) * (gladiator.speed / 100) + Dice.totalRolls(
            Dice.roll(
                1,
                DiceTypes.D6
            )
        ) * (gladiator.attack / 100)
    }

    private fun appendReport(str: String) {
        Log.d(TAG, str)
        roundReport += "$str\n"
    }

    private fun isCombatStillGoing(): Boolean {
        if (playerGladiatorList.isEmpty()) {
            appendReport("Combat over, player lost in $round rounds")
            return false
        } else if (enemyGladiatorList.isEmpty()) {
            appendReport("Combat over, player won in $round rounds")
            return false
        } else {
            return true
        }
    }

    fun run(): CombatRoundResult {

        if (!isCombatStillGoing()) {
            return CombatRoundResult(playerGladiatorList, enemyGladiatorList)
        }
        determineRoundOrder()
//        var gladiatorList = playerGladiatorList + enemyGladiatorList
        roundOrder.forEach { map ->
            val gladiatorId = map.keys.first()
            val gladiator = playerGladiatorList.find { it.gladiatorId == gladiatorId }
                ?: enemyGladiatorList.find { it.gladiatorId == gladiatorId }
            val target =
                playerGladiatorList.find { gladiator?.action!!.targetGladiatorID != gladiatorId }
                    ?: enemyGladiatorList.find { gladiator?.action!!.targetGladiatorID != gladiatorId }
            if (gladiator != null) {
                updateFromCombatActionResult(
                    combatEnumToAction(gladiator.action!!.action).act(
                        gladiator,
                        target!!
                    )
                )
                isCombatStillGoing()
            }
        }

        appendReport(
            "Combat round $round result: ${playerGladiatorList.joinToString { "${it.name} - health: ${it.health}" } + " vs " + enemyGladiatorList.joinToString { "${it.name} - health: ${it.health}" }}"
        )
        return CombatRoundResult(playerGladiatorList, enemyGladiatorList)
    }

    //    private fun updateFromCombatActionResult(result: CombatActionResult): CombatRoundResult {
//        Log.d(TAG, "updating from action result: $result")
//        // reduce health and stamina
//        result.actor.stamina -= result.deltaStamina
//        result.target.health -= result.deltaHealth
//
//        playerGladiatorList = playerGladiatorList.map { gladiator ->
//            if (gladiator.gladiatorId == result.actor.gladiatorId) {
//                gladiator.stamina -= result.deltaStamina // Update actor's stamina directly
//                gladiator // Return the modified gladiator
//            } else if (gladiator.gladiatorId == result.target.gladiatorId) {
//                gladiator.health -= result.deltaHealth // Update target's health directly
//                gladiator // Return the modified gladiator
//            } else {
//                gladiator // Keep gladiator unchanged if not involved in the action
//            }
//        }
//
//        enemyGladiatorList = enemyGladiatorList.map { gladiator ->
//            if (gladiator.gladiatorId == result.actor.gladiatorId) {
//                gladiator.stamina -= result.deltaStamina
//                gladiator
//            } else if (gladiator.gladiatorId == result.target.gladiatorId) {
//                gladiator.health -= result.deltaHealth
//                gladiator
//            } else {
//                gladiator
//            }
//        }
//
//        // remove dead gladiators
//        val aliveList = mutableListOf<Gladiator>()
//        val deadList = mutableListOf<Gladiator>()
//        for (gladiator in playerGladiatorList){
//            if (!gladiator.isAlive()){
//                appendReport("Gladiator ${gladiator.name} is dead")
//                deadList.add(gladiator)
//            }
//            else {
//                aliveList.add(gladiator)
//            }
//        }
//        for (gladiator in enemyGladiatorList){
//            if (!gladiator.isAlive()){
//                appendReport("Gladiator ${gladiator.name} is dead")
//                deadList.add(gladiator)
//            }
//            else {
//                aliveList.add(gladiator)
//            }
//        }
//        return CombatRoundResult(playerGladiatorList, enemyGladiatorList)
//    }
    private fun updateFromCombatActionResult(result: CombatActionResult): CombatRoundResult {
        Log.d(TAG, "updating from action result: $result")

        result.actor.stamina -= result.deltaStamina
        result.target.health -= result.deltaHealth

        // Update player and enemy lists, removing dead gladiators
        playerGladiatorList = playerGladiatorList.filter {
            it.isAlive().also { alive -> if (!alive) appendReport("Gladiator ${it.name} is dead") }
        }
        enemyGladiatorList = enemyGladiatorList.filter {
            it.isAlive().also { alive -> if (!alive) appendReport("Gladiator ${it.name} is dead") }
        }

        return CombatRoundResult(playerGladiatorList, enemyGladiatorList)
    }


    companion object {
        private const val TAG = "CombatRound"
        fun init(
            playerGladiatorList: List<Gladiator>,
            enemyGladiatorList: List<Gladiator>,
            round: Int
        ): CombatRound {
            val combatRound = CombatRound()
            combatRound.playerGladiatorList = playerGladiatorList
            combatRound.enemyGladiatorList = enemyGladiatorList
            combatRound.round = round
            return combatRound
        }
    }
}