package com.doorxii.ludus.utils.combat

import android.util.Log
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.utils.actions.CombatBehaviour
import com.doorxii.ludus.utils.actions.combatactions.ChosenAction
import com.doorxii.ludus.utils.dice.Dice
import com.doorxii.ludus.utils.dice.DiceTypes
import com.doorxii.ludus.utils.enums.EnumToAction.combatActionToEnum
import kotlinx.serialization.Serializable

@Serializable
class Combat {
    var playerGladiatorList: MutableList<Gladiator> = mutableListOf()
    var enemyGladiatorList: MutableList<Gladiator> = mutableListOf()
    var originalPlayerGladiatorList: List<Gladiator> = listOf()
    var originalEnemyGladiatorList: List<Gladiator> = listOf()
    var submittedPlayerGladiatorList: MutableList<Gladiator> = mutableListOf()
    var submittedEnemyGladiatorList: MutableList<Gladiator> = mutableListOf()
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
            for (gladiator in playerGladiatorList) {
                updateMoraleAfterRound(gladiator)
            }
            for (gladiator in enemyGladiatorList) {
                updateMoraleAfterRound(gladiator)
            }
            updateListsFromCombatRoundResult(roundResult)
            nullGladiatorActions()
        }
        return CombatResult(playerGladiatorList, enemyGladiatorList, combatReport)
    }

    private fun updateListsFromCombatRoundResult(roundResult: CombatRoundResult) {
        playerGladiatorList = roundResult.playerGladiatorList.toMutableList()
        enemyGladiatorList = roundResult.enemyGladiatorList.toMutableList()
        submittedPlayerGladiatorList = roundResult.submittedPlayerGladiatorList.toMutableList()
        submittedEnemyGladiatorList = roundResult.submittedEnemyGladiatorList.toMutableList()
        isComplete = !isCombatStillGoing()
    }

    fun appendReport(str: String) {
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
            updateListsFromCombatRoundResult(combatRoundResult)
            for (gladiator in playerGladiatorList) {
                updateMoraleAfterRound(gladiator)
            }
            for (gladiator in enemyGladiatorList) {
                updateMoraleAfterRound(gladiator)
            }
        }
        return CombatResult(playerGladiatorList, enemyGladiatorList, combatReport)
    }

    private fun updateMoraleAfterRound(updatingGladiator: Gladiator) {
        val isPlayer = updatingGladiator in playerGladiatorList
        val ownList = if (isPlayer) playerGladiatorList else enemyGladiatorList
        val otherList = if (isPlayer) enemyGladiatorList else playerGladiatorList
        val originalList = if (isPlayer) originalPlayerGladiatorList else originalEnemyGladiatorList

        // Outnumbered modifier
        if (ownList.count() > otherList.count()){
            updatingGladiator.morale += (ownList.count() - otherList.count()) * 3.0
        }

        // Mourning modifier
        updatingGladiator.morale -= (originalList.count() - ownList.count()) * 5.0

        // Combat strength modifier
        val totalDifference = ownList.sumOf { it.attack + it.defence } - otherList.sumOf { it.attack + it.defence }
        updatingGladiator.morale += when {
            totalDifference > 0 -> 1.0
            totalDifference < 0 -> -2.0
            else -> 0.0
        }

        // low health modifier
        if (updatingGladiator.health <= 50) {
            updatingGladiator.morale += (1 - (updatingGladiator.health/100)) * 5
        }


        // Ensure morale is between 5 and 100
        updatingGladiator.morale = updatingGladiator.morale.coerceIn(5.0, 120.0)
    }

    private fun runNewRound(
        playerChoices: List<ChosenAction>,
        enemyChoices: List<ChosenAction>
    ): CombatRoundResult {
        if (!isCombatStillGoing()) {
            return CombatRoundResult(
                playerGladiatorList,
                enemyGladiatorList,
                submittedPlayerGladiatorList,
                submittedEnemyGladiatorList
            )
        }
        roundNumber++
        appendReport("Round $roundNumber: ${playerGladiatorList.joinToString { it.name } + " vs " + enemyGladiatorList.joinToString { it.name }}")

        val allChoices = playerChoices + enemyChoices
        val chosenActionMap = allChoices.associateBy { it.actingGladiatorID }

        playerGladiatorList.forEach { it.action = chosenActionMap[it.gladiatorId] }
        enemyGladiatorList.forEach { it.action = chosenActionMap[it.gladiatorId] }

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
            combat.playerGladiatorList.addAll(playerGladiatorList.filterNotNull())
            combat.enemyGladiatorList.addAll(enemyGladiatorList.filterNotNull())
            combat.combatName =
                combat.playerGladiatorList.joinToString { it.name } + " vs " + combat.enemyGladiatorList.joinToString { it.name }
            combat.combatReport = "${combat.combatName}\n\n"
            return combat
        }
    }
}