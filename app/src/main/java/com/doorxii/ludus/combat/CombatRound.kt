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
        Log.d(TAG, "round order: $roundOrder")
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
        var res = CombatRoundResult(playerGladiatorList, enemyGladiatorList)

        if (!isCombatStillGoing()) {
            return res
        }
        determineRoundOrder()
        roundOrder.forEach { map ->
            val gladiatorId = map.keys.first()

            val gladiator = playerGladiatorList.find { it.gladiatorId == gladiatorId }
                ?: enemyGladiatorList.find { it.gladiatorId == gladiatorId }
            if (gladiator == null) {
                Log.d(TAG, "retrieved gladiator from round order is null ")
            } else {
                Log.d(TAG, "gladiator: ${gladiator.gladiatorId} ${gladiator.name} ${gladiator.action?.actionName ?: "no action"} ${gladiator.action?.targetGladiatorID ?: "no target"}")

                val target =
                    playerGladiatorList.find { it.gladiatorId == gladiator.action?.targetGladiatorID }
                    ?: enemyGladiatorList.find { it.gladiatorId == gladiator.action?.targetGladiatorID }

                Log.d(TAG, "target: ${target?.gladiatorId ?: "no target"} ${target?.name ?: "no target"}")
                // only act if target exists

                if (target != null){
                    res = updateFromCombatActionResult(
                        combatEnumToAction(gladiator.action!!.action).act(
                            gladiator,
                            target
                        )
                    )
                }
                isCombatStillGoing()
            }
        }
        appendReport(
            "Combat round $round result: ${playerGladiatorList.joinToString { "${it.name} - health: ${it.health}" } + " vs " + enemyGladiatorList.joinToString { "${it.name} - health: ${it.health}" }}"
        )
        return res
    }

    private fun updateFromCombatActionResult(result: CombatActionResult): CombatRoundResult {
        Log.d(TAG, "updating from action result: $result")

        result.actor.stamina -= result.deltaStamina
        result.target.health -= result.deltaHealth

        val submittedPlayerGladiatorList = mutableListOf<Gladiator>()
        val submittedEnemyGladiatorList = mutableListOf<Gladiator>()

        // Update player and enemy lists, removing dead and submitted gladiators
        playerGladiatorList = playerGladiatorList.filter { gladiator ->
            val isAlive = gladiator.isAlive().also { alive -> if (!alive) appendReport("Gladiator ${gladiator.name} is dead") }
            if (isAlive && gladiator.hasNoMorale()) {
                submittedPlayerGladiatorList.add(gladiator)
                false // false on the filter lambda will remove the gladiator from playerGladiatorList
            } else {
                isAlive // Keep in playerGladiatorList if alive and not submitted
            }
        }

        enemyGladiatorList = enemyGladiatorList.filter { gladiator ->
            val isAlive = gladiator.isAlive().also { alive -> if (!alive) appendReport("Gladiator ${gladiator.name} is dead") }
            if (isAlive && gladiator.hasNoMorale()) {
                submittedEnemyGladiatorList.add(gladiator)
                false // Remove from enemyGladiatorList if submitted
            } else {
                isAlive // Keep in enemyGladiatorList if alive and not submitted
            }
        }

        return CombatRoundResult(playerGladiatorList, enemyGladiatorList, submittedPlayerGladiatorList, submittedEnemyGladiatorList)
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