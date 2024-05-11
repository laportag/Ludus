package com.doorxii.ludus.combat

import android.util.Log
import com.doorxii.ludus.actions.CombatBehaviour
import com.doorxii.ludus.actions.combatactions.BasicAttack
import com.doorxii.ludus.actions.combatactions.BasicDefend
import com.doorxii.ludus.actions.combatactions.CombatAction
import com.doorxii.ludus.actions.combatactions.CombatActions
import com.doorxii.ludus.actions.combatactions.Wait
import com.doorxii.ludus.data.models.beings.Gladiator

class Combat(
    gladiatorA: Gladiator, gladiatorB: Gladiator
) {

    val gladiatorA = gladiatorA
    val gladiatorB = gladiatorB

    fun init() {
        Log.d(TAG, "Combat started")
        var gladiatorList = listOf(gladiatorA, gladiatorB)
        var round = 1
        // run rounds until one dies
        while (gladiatorList.size > 1) {

            gladiatorList = CombatRound(
                gladiatorA,
                gladiatorB,
                BasicAttack(),
                CombatBehaviour.basicActionPicker(gladiatorB),
                round
            ).initiateRound()
            round++
        }
        // balances the increment in the final round
        round--
        Log.d(TAG, "Combat over, ${gladiatorList[0].name} won in $round rounds")
    }

    fun selectRoundAction(choice: CombatActions): CombatAction {
        return when (choice) {
            CombatActions.BASIC_ATTACK -> BasicAttack()
            CombatActions.BASIC_DEFEND -> BasicDefend()
            CombatActions.WAIT -> Wait()
        }
    }



    companion object {
        const val TAG = "Combat"

    }
}