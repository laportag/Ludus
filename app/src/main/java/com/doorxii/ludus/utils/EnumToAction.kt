package com.doorxii.ludus.utils

import com.doorxii.ludus.actions.combatactions.BasicAttack
import com.doorxii.ludus.actions.combatactions.CombatAction
import com.doorxii.ludus.actions.combatactions.CombatActions
import com.doorxii.ludus.actions.combatactions.TiredAttack
import com.doorxii.ludus.actions.combatactions.Wait

object EnumToAction {
    fun combatEnumToAction(choice: CombatActions): CombatAction {
        return when (choice) {
            CombatActions.BASIC_ATTACK -> BasicAttack()
            CombatActions.TIRED_ATTACK -> TiredAttack()
            CombatActions.WAIT -> Wait()
        }
    }

    fun combatEnumListToActionList(list: List<CombatActions>): List<CombatAction> {
        return list.map { combatEnumToAction(it) }

    }
}