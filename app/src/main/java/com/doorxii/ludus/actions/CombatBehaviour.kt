package com.doorxii.ludus.actions

import com.doorxii.ludus.actions.combatactions.BasicAttack
import com.doorxii.ludus.actions.combatactions.TiredAttack
import com.doorxii.ludus.actions.combatactions.CombatAction
import com.doorxii.ludus.actions.combatactions.Wait
import com.doorxii.ludus.data.models.animal.Gladiator

object CombatBehaviour {

    fun basicActionPicker(gladiator: Gladiator): CombatAction {
        return when {
            gladiator.stamina < 10 -> Wait()
            gladiator.stamina < 20 -> TiredAttack()
            else -> BasicAttack()
        }
    }

    fun waitActionPicker(): CombatAction {
        return Wait()
    }


}