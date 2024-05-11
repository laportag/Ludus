package com.doorxii.ludus.combat

import android.app.AlertDialog
import android.util.Log
import androidx.compose.runtime.Composable
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
    var round: Int = 0
    var gladiatorList = gladiatorList
    var userChoice: CombatAction? = null
    var enemyChoice: CombatAction? = null
    var isComplete = false

    fun simCombat() {
        Log.d(TAG, "Combat started")
        while (!isComplete) {
            userChoice = CombatBehaviour.basicActionPicker(gladiatorList[0])
            enemyChoice = CombatBehaviour.basicActionPicker(gladiatorList[1])
            newRound(gladiatorList[0],gladiatorList[1], userChoice!!, enemyChoice!!)
//            gladiatorList = checkAlive()
            if (gladiatorList.size == 1) {
                Log.d(TAG, "Combat over, ${gladiatorList[0].name} won in $round rounds")
                isComplete = true
            }
        }
    }

    fun playCombat(){
        Log.d(TAG, "Combat started")
        while (!isComplete) {
//            userChoice =
            enemyChoice = CombatBehaviour.basicActionPicker(gladiatorList[1])
            newRound(gladiatorList[0],gladiatorList[1], userChoice!!, enemyChoice!!)
        }
    }

    fun checkAlive(): List<Gladiator> {
        if (gladiatorList.all { it.isAlive() }) {
            gladiatorList = listOf(gladiatorList[0], gladiatorList[1])
            return gladiatorList
        }
        else if (gladiatorList[0].isAlive()) {
            gladiatorList = listOf(gladiatorList[0])
            Log.d(TAG, "Combat over, ${gladiatorList[0].name} won in $round rounds")
            isComplete = true
            return gladiatorList
        }
        else {
            gladiatorList = listOf(gladiatorList[1])
            Log.d(TAG, "Combat over, ${gladiatorList[1].name} won in $round rounds")
            isComplete = true
            return gladiatorList
        }
    }

    fun newRound(gladA: Gladiator, gladB: Gladiator, actionA: CombatAction, actionB: CombatAction): List<Gladiator> {
        round++
        gladiatorList = CombatRound(
            gladA,
            gladB,
            actionA,
            actionB,
            round
        ).initiateRound()
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