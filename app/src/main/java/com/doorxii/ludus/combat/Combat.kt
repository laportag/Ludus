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
    gladiatorA: Gladiator, gladiatorB: Gladiator
) {

    val gladiatorA = gladiatorA
    val gladiatorB = gladiatorB
    var round: Int = 0
    var gladiatorList = listOf(gladiatorA, gladiatorB)


    fun init() {
        Log.d(TAG, "Combat started")
        var round = 1


        // run rounds until one dies
        while (gladiatorList.size > 1) {

            newRound(gladiatorA, gladiatorB, selectRoundAction(CombatActions.BASIC_ATTACK), selectRoundAction(CombatActions.BASIC_DEFEND))

        }
        // balances the increment in the final round
        round--
        Log.d(TAG, "Combat over, ${gladiatorList[0].name} won in $round rounds")
    }

    fun chooseActionLoop(){
        lateinit var choice: CombatActions
        var inputGiven: Boolean = false
        while (!inputGiven) {
            choice = uiActionChooser()
            if (choice != null) {
                inputGiven = true
            }
        }
    }

    fun newRound(gladA: Gladiator, gladB: Gladiator, actionA: CombatAction, actionB: CombatAction) {
        gladiatorList = CombatRound(
            gladA,
            gladB,
            actionA,
            actionB,
            round
        ).initiateRound()
        round++
    }

    fun selectRoundAction(choice: CombatActions): CombatAction {
        return when (choice) {
            CombatActions.BASIC_ATTACK -> BasicAttack()
            CombatActions.BASIC_DEFEND -> TiredAttack()
            CombatActions.WAIT -> Wait()
        }
    }

    fun uiActionChooser(): CombatActions {
        return CombatActions.WAIT
    }

    companion object {
        const val TAG = "Combat"

    }
}