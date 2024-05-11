package com.doorxii.ludus.combat

import android.util.Log
import com.doorxii.ludus.actions.BasicAttack
import com.doorxii.ludus.data.models.beings.Gladiator

class Combat(
    gladiatorA: Gladiator, gladiatorB: Gladiator) {

    val gladiatorA = gladiatorA
    val gladiatorB = gladiatorB

    fun init(){
        Log.d(TAG, "Combat started")
        var gladiatorList = listOf(gladiatorA, gladiatorB)
        var round = 1
        // run rounds until one dies
        while (gladiatorList.size > 1) {
            gladiatorList = CombatRound(gladiatorA, gladiatorB, BasicAttack(), BasicAttack(), round).initiateRound()
            round++
        }
        // balances the increment in the final round
        round--
        Log.d(TAG, "Combat over, ${gladiatorList[0].name} won in ${round} rounds")
    }

    companion object{
        const val TAG = "Combat"

    }
}