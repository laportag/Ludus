package com.doorxii.ludus.combat

import android.util.Log
import com.doorxii.ludus.actions.CombatBehaviour
import com.doorxii.ludus.actions.combatactions.ChosenAction
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.utils.dice.Dice
import com.doorxii.ludus.utils.dice.DiceTypes
import com.doorxii.ludus.utils.enums.EnumToAction.combatActionToEnum
import kotlinx.serialization.Serializable

@Serializable
class Combat {
    var playerGladiatorList: List<Gladiator> = listOf()
    var enemyGladiatorList: List<Gladiator> = listOf()
    var originalPlayerGladiatorList: List<Gladiator> = listOf()
    var originalEnemyGladiatorList: List<Gladiator> = listOf()
    private var round: CombatRound? = null
    private var roundNumber: Int = 0
    var isComplete = false
    lateinit var combatName: String
    lateinit var combatReport: String

    fun simCombat(): CombatResult {
        appendReport("Sim Combat started")
        while (!isComplete) {
            val playerChoices: MutableList<ChosenAction> = mutableListOf()
            for (gladiator in enemyGladiatorList) {
                playerChoices.add(
                    ChosenAction(
                        gladiator.gladiatorId,
                        enemyTargetSelector().gladiatorId,
                        combatActionToEnum(CombatBehaviour.basicActionPicker(gladiator))
                    )
                )
            }
            val enemyChoices: MutableList<ChosenAction> = mutableListOf()
            for (gladiator in enemyGladiatorList) {
                enemyChoices.add(
                    ChosenAction(
                        gladiator.gladiatorId,
                        enemyTargetSelector().gladiatorId,
                        combatActionToEnum(CombatBehaviour.basicActionPicker(gladiator))
                    )
                )
            }
            val roundResult = runNewRound(playerChoices, enemyChoices)
            playerGladiatorList = roundResult.playerGladiatorList
            enemyGladiatorList = roundResult.enemyGladiatorList
            isComplete = !isCombatStillGoing()
            nullGladiatorActions()
        }
        return CombatResult(playerGladiatorList, enemyGladiatorList, combatReport)
    }

    private fun appendReport(str: String) {
        Log.d(TAG, str)
        combatReport += "$str\n"
    }

    private fun isCombatStillGoing(): Boolean {
        if (playerGladiatorList.isEmpty()) {
            appendReport("Combat over, player lost in $roundNumber rounds")
            return false
        } else if (enemyGladiatorList.isEmpty()) {
            appendReport("Combat over, player won in $roundNumber rounds")
            return false
        } else {
            return true
        }
    }

    private fun enemyTargetSelector(): Gladiator {
        val lowestHealthGladiator = playerGladiatorList.minByOrNull { it.health }
        val randomFactor = Dice.totalRolls(Dice.roll(1, DiceTypes.D100))
        return if (randomFactor <= 40 && lowestHealthGladiator != null) { // 40% chance to target lowest health
            lowestHealthGladiator
        } else {
            playerGladiatorList.random() // Otherwise, choose randomly
        }
    }

    fun playCombatRound(playerChoices: List<ChosenAction>): CombatResult {
        if (!isCombatStillGoing()) {
            isComplete = true
            return CombatResult(playerGladiatorList, enemyGladiatorList, combatReport)
        } else {
            // enemy gladiator choices
            val enemyChoices: MutableList<ChosenAction> = mutableListOf()
            for (gladiator in enemyGladiatorList) {
                enemyChoices.add(
                    ChosenAction(
                        gladiator.gladiatorId,
                        enemyTargetSelector().gladiatorId,
                        combatActionToEnum(CombatBehaviour.basicActionPicker(gladiator))
                    )
                )
            }
            val combatRoundResult: CombatRoundResult = runNewRound(playerChoices, enemyChoices)
            playerGladiatorList = combatRoundResult.playerGladiatorList
            enemyGladiatorList = combatRoundResult.enemyGladiatorList
            isComplete = !isCombatStillGoing()
        }
        return CombatResult(playerGladiatorList, enemyGladiatorList, combatReport)
    }

    private fun runNewRound(
        playerChoices: List<ChosenAction>,
        enemyChoices: List<ChosenAction>
    ): CombatRoundResult {
        if (!isCombatStillGoing()) {
            return CombatRoundResult(playerGladiatorList, enemyGladiatorList)
        }
        roundNumber++
        appendReport("Round $roundNumber: ${playerGladiatorList.joinToString { it.name } + " vs " + enemyGladiatorList.joinToString { it.name }}")

        val allChoices = playerChoices + enemyChoices
        val chosenActionMap = allChoices.associateBy { it.actingGladiatorID }

        for (gladiator in playerGladiatorList) {
            val chosenAction = chosenActionMap[gladiator.gladiatorId]
            if (chosenAction != null) {
                gladiator.action = chosenAction
            }
        }
        for (gladiator in enemyGladiatorList) {
            val chosenAction = chosenActionMap[gladiator.gladiatorId]
            if (chosenAction != null) {
                gladiator.action = chosenAction
            }
        }

        round = CombatRound.init(playerGladiatorList, enemyGladiatorList, roundNumber)
        val roundResult = round!!.run()
        appendReport(round!!.roundReport)
        nullGladiatorActions()
        return roundResult
    }

    private fun nullGladiatorActions() {
        for (gladiator in playerGladiatorList) {
            gladiator.action = null
        }
        for (gladiator in enemyGladiatorList) {
            gladiator.action = null
        }
    }

    companion object {
        const val TAG = "Combat"
        fun init(
            playerGladiatorList: List<Gladiator?>,
            enemyGladiatorList: List<Gladiator?>
        ): Combat {
            val combat = Combat()
            combat.playerGladiatorList = playerGladiatorList as List<Gladiator>
            combat.originalPlayerGladiatorList = playerGladiatorList
            combat.enemyGladiatorList = enemyGladiatorList as List<Gladiator>
            combat.originalEnemyGladiatorList = enemyGladiatorList
            combat.combatName =
                "C${playerGladiatorList.joinToString { it.name } + " vs " + enemyGladiatorList.joinToString { it.name }}"
            combat.combatReport = "${combat.combatName}\n\n"
            return combat
        }
    }
}