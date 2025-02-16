package com.doorxii.ludus.utils.combat

import com.doorxii.ludus.utils.actions.combatactions.BasicAttack
import com.doorxii.ludus.utils.actions.combatactions.CombatAction
import com.doorxii.ludus.utils.actions.combatactions.CombatActions
import com.doorxii.ludus.utils.actions.combatactions.Missio
import com.doorxii.ludus.utils.actions.combatactions.TiredAttack
import com.doorxii.ludus.utils.actions.combatactions.Wait

object EnumToAction {
    fun combatEnumToAction(choice: CombatActions): CombatAction {
        return when (choice) {
            CombatActions.BASIC_ATTACK -> BasicAttack()
            CombatActions.TIRED_ATTACK -> TiredAttack()
            CombatActions.WAIT -> Wait()
            CombatActions.MISSIO -> Missio()
        }
    }

    fun combatActionToEnum(action: CombatAction): CombatActions {
        return when (action) {
            is BasicAttack -> CombatActions.BASIC_ATTACK
            is TiredAttack -> CombatActions.TIRED_ATTACK
            is Wait -> CombatActions.WAIT
            is Missio -> CombatActions.MISSIO
            else -> {
                CombatActions.WAIT}
        }
    }

    fun combatEnumListToActionList(list: List<CombatActions>): List<CombatAction> {
        return list.map { combatEnumToAction(it) }
    }
}