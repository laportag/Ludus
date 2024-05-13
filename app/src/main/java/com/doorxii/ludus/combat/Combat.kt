package com.doorxii.ludus.combat

import android.util.Log
import com.doorxii.ludus.actions.CombatBehaviour
import com.doorxii.ludus.actions.combatactions.BasicAttack
import com.doorxii.ludus.actions.combatactions.TiredAttack
import com.doorxii.ludus.actions.combatactions.CombatAction
import com.doorxii.ludus.actions.combatactions.CombatActions
import com.doorxii.ludus.actions.combatactions.Wait
import com.doorxii.ludus.data.models.animal.Gladiator

class Combat(
    var gladiatorList: List<Gladiator>
) {
    private var round: CombatRound? = null
    private var roundNumber: Int = 0
    private var userChoice: CombatAction? = null
    private var enemyChoice: CombatAction? = null
    var isComplete = false
    var combatReport = "Combat: ${gladiatorList[0].name} vs ${gladiatorList[1].name}\n"

    fun simCombat(): String {
        appendReport("Sim Combat started")
        while (!isComplete) {
            userChoice = CombatBehaviour.basicActionPicker(gladiatorList[0])
            enemyChoice = CombatBehaviour.basicActionPicker(gladiatorList[1])
            runNewRound(gladiatorList[0], gladiatorList[1], userChoice!!, enemyChoice!!)



            if (gladiatorList.size == 1) {
                appendReport("Combat over, ${gladiatorList[0].name} won in $roundNumber rounds")
                isComplete = true

            }
        }
        return combatReport
    }

    private fun appendReport(str: String) {
        Log.d(TAG, str)
        combatReport += "$str\n"
    }

    fun playCombatRound(choice: CombatActions): String {
        if (gladiatorList.size > 2){
            return combatReport
        } else {

            userChoice = enumToAction(choice)
            enemyChoice = CombatBehaviour.basicActionPicker(gladiatorList[1])
            runNewRound(gladiatorList[0], gladiatorList[1], userChoice!!, enemyChoice!!)
            if (gladiatorList.size == 1) {
                appendReport("Combat over, ${gladiatorList[0].name} won in $roundNumber rounds")
                isComplete = true
            }
//            round = CombatRound.init(gladiatorList[0], gladiatorList[1], roundNumber)
        }

        return combatReport
    }

    private fun runNewRound(
        gladA: Gladiator,
        gladB: Gladiator,
        actionA: CombatAction,
        actionB: CombatAction
    ): List<Gladiator> {
        roundNumber++
        gladA.action = actionA
        gladB.action = actionB
        appendReport("Round $roundNumber: ${gladA.name}:${gladA.action?.name} vs ${gladB.name}: ${gladB.action?.name}")

        round = CombatRound.init(gladA, gladB, roundNumber)
        gladiatorList = round!!.run()

        appendReport(round!!.roundReport)
        nullGladiatorActions()
        return gladiatorList
    }

    private fun nullGladiatorActions() {
        for (gladiator in gladiatorList) {
            gladiator.action = null
        }
    }

    private fun enumToAction(choice: CombatActions): CombatAction {
        return when (choice) {
            CombatActions.BASIC_ATTACK -> BasicAttack()
            CombatActions.TIRED_ATTACK -> TiredAttack()
            CombatActions.WAIT -> Wait()
        }
    }

    companion object {
        const val TAG = "Combat"
        fun init(gladiatorList: List<Gladiator>): Combat {
            return Combat(gladiatorList)
        }
    }
}