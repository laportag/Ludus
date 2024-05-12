package com.doorxii.ludus.combat

import android.util.Log
import com.doorxii.ludus.actions.CombatBehaviour
import com.doorxii.ludus.actions.combatactions.BasicAttack
import com.doorxii.ludus.actions.combatactions.TiredAttack
import com.doorxii.ludus.actions.combatactions.CombatAction
import com.doorxii.ludus.actions.combatactions.CombatActions
import com.doorxii.ludus.actions.combatactions.Wait
import com.doorxii.ludus.data.models.beings.Gladiator

class Combat(
    gladiatorList:List<Gladiator>
) {
    var round: CombatRound? = null
    var roundNumber: Int = 0
    var gladiatorList = gladiatorList
    var userChoice: CombatAction? = null
    var enemyChoice: CombatAction? = null
    var isComplete = false
    var combatReport = "Combat: ${gladiatorList[0].name} vs ${gladiatorList[1].name}\n"

    fun simCombat(): String {
        appendReport("Sim Combat started")
        while (!isComplete) {
            userChoice = CombatBehaviour.basicActionPicker(gladiatorList[0])
            enemyChoice = CombatBehaviour.basicActionPicker(gladiatorList[1])
            newRound(gladiatorList[0],gladiatorList[1], userChoice!!, enemyChoice!!)



            if (gladiatorList.size == 1) {
                appendReport("Combat over, ${gladiatorList[0].name} won in $roundNumber rounds")
                isComplete = true

            }
        }
        return combatReport
    }

    fun appendReport(str: String){
        Log.d(TAG, str)
        combatReport += "$str\n"
    }

    fun playCombatRound(choice: CombatActions): String{
        userChoice = enumToAction(choice)
        enemyChoice = CombatBehaviour.basicActionPicker(gladiatorList[1])
        newRound(gladiatorList[0],gladiatorList[1], userChoice!!, enemyChoice!!)
        round = CombatRound.init(gladiatorList[0],gladiatorList[1], userChoice!!, enemyChoice!!, roundNumber)

        return combatReport
    }

    fun newRound(gladA: Gladiator, gladB: Gladiator, actionA: CombatAction, actionB: CombatAction): List<Gladiator> {
        roundNumber++
        appendReport("Round $roundNumber: ${gladA.name}:${actionA.name} vs ${gladB.name}: ${actionB.name}")
        round = CombatRound.init(gladA, gladB, actionA, actionB, roundNumber)
        gladiatorList = round!!.run()
        appendReport(round!!.roundReport)
        return gladiatorList
    }

    fun enumToAction(choice: CombatActions): CombatAction {
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