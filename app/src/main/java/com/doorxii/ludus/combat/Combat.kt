package com.doorxii.ludus.combat

import android.util.Log
import com.doorxii.ludus.actions.CombatBehaviour
import com.doorxii.ludus.actions.combatactions.CombatAction
import com.doorxii.ludus.actions.combatactions.CombatActions
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.utils.enums.EnumToAction.combatActionToEnum
import com.doorxii.ludus.utils.enums.EnumToAction.combatEnumToAction
import kotlinx.serialization.Serializable

@Serializable
class Combat(

) {
    var gladiatorList: List<Gladiator> = listOf()
    var originalGladiatorList: List<Gladiator> = listOf()
    private var round: CombatRound? = null
    private var roundNumber: Int = 0
    private var userChoice: CombatActions? = null
    private var enemyChoice: CombatActions? = null
    var isComplete = false
    lateinit var combatName: String
    lateinit var combatReport: String

    fun simCombat(): CombatResult {
        appendReport("Sim Combat started")
        while (!isComplete) {
            userChoice = combatActionToEnum(CombatBehaviour.basicActionPicker(gladiatorList[0]))
            enemyChoice = combatActionToEnum(CombatBehaviour.basicActionPicker(gladiatorList[1]))
            gladiatorList = runNewRound(
                gladiatorList,
                combatEnumToAction(userChoice!!),
                combatEnumToAction(enemyChoice!!)
            )

            if (gladiatorList.size < 2) {
                if (gladiatorList.isEmpty()) {
                    appendReport("Combat over, both dead in $roundNumber rounds")
                } else {
                    appendReport("Combat over, ${gladiatorList[0].name} won in $roundNumber rounds")
                }
                isComplete = true

            }
        }
        return CombatResult(true, combatReport)
    }

    private fun appendReport(str: String) {
        Log.d(TAG, str)
        combatReport += "$str\n"
    }

    fun playCombatRound(choice: CombatActions): CombatResult {
        if (gladiatorList.size < 2) {
            appendReport("Combat over, ${gladiatorList[0].name} won in $roundNumber rounds")
            isComplete = true
            return CombatResult(true, combatReport)

        } else {
            userChoice = choice
            enemyChoice = combatActionToEnum(CombatBehaviour.basicActionPicker(gladiatorList[1]))

            gladiatorList = runNewRound(
                gladiatorList,
                combatEnumToAction(userChoice!!),
                combatEnumToAction(enemyChoice!!)
            )
            if (gladiatorList.size < 2) {
                if (gladiatorList.isEmpty()) {
                    appendReport("Combat over, both dead in $roundNumber rounds")
                } else {
                    appendReport("Combat over, ${gladiatorList[0].name} won in $roundNumber rounds")
                }
                isComplete = true
                return CombatResult(true, combatReport)
            }
        }

        return CombatResult(false, combatReport)
    }

    private fun runNewRound(
        gladiatorList: List<Gladiator>,
        actionA: CombatAction,
        actionB: CombatAction
    ): List<Gladiator> {
        var gladiatorList = gladiatorList
        roundNumber++
        gladiatorList[0].action = combatActionToEnum(actionA)
        gladiatorList[1].action = combatActionToEnum(actionB)
        appendReport("Round $roundNumber: ${gladiatorList[0].name}:${gladiatorList[0].action.toString()} vs ${gladiatorList[1].name}: ${gladiatorList[1].action?.name}")
        if (gladiatorList.size <= 1) {
            return gladiatorList
        }
        round = CombatRound.init(gladiatorList, roundNumber)
        gladiatorList = round!!.run(gladiatorList)

        appendReport(round!!.roundReport)
        nullGladiatorActions()
        return gladiatorList
    }

    private fun nullGladiatorActions() {
        for (gladiator in gladiatorList) {
            gladiator.action = null
        }
    }


    companion object {
        const val TAG = "Combat"
        fun init(gladiatorList: List<Gladiator>): Combat {
            val combat = Combat()
            combat.gladiatorList = gladiatorList
            combat.originalGladiatorList = gladiatorList
            combat.combatName = "Combat: ${gladiatorList[0].name} vs ${gladiatorList[1].name}"
            combat.combatReport = "${combat.combatName}\n"
            return combat
        }
    }
}