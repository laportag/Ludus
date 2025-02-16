package com.doorxii.ludus.utils.combat

import com.doorxii.ludus.data.models.actions.combatactions.BasicAttack
import com.doorxii.ludus.data.models.actions.combatactions.CombatAction
import com.doorxii.ludus.data.models.actions.combatactions.Missio
import com.doorxii.ludus.data.models.actions.combatactions.TiredAttack
import com.doorxii.ludus.data.models.actions.combatactions.Wait
import com.doorxii.ludus.data.models.animal.Gladiator

object CombatBehaviour {

    fun basicActionPicker(gladiator: Gladiator): CombatAction {
        return when {
            gladiator.morale < 7 -> Missio()
            gladiator.stamina < 10 -> Wait()
            gladiator.stamina < 20 -> TiredAttack()
            else -> BasicAttack()
        }
    }

    fun waitActionPicker(): CombatAction {
        return Wait()
    }


}