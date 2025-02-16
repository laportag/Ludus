package com.doorxii.ludus.utils.combat

import com.doorxii.ludus.data.models.actions.combatactions.BasicAttack
import com.doorxii.ludus.data.models.actions.combatactions.ChosenAction
import com.doorxii.ludus.data.models.actions.combatactions.CombatAction
import com.doorxii.ludus.data.models.actions.combatactions.CombatActions
import com.doorxii.ludus.data.models.actions.combatactions.Missio
import com.doorxii.ludus.data.models.actions.combatactions.TiredAttack
import com.doorxii.ludus.data.models.actions.combatactions.Wait
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.ui.activities.CombatActivityViewModel

object CombatUtils {
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

    fun handleAction(
        chosenAction: ChosenAction,
        actingGladiator: Gladiator,
        viewModel: CombatActivityViewModel,
        onTurnEnded: (List<ChosenAction>) -> Unit,
        resetActions: () -> Unit,
        findNextAvailableGladiator: () -> Gladiator?,
        onActingGladiatorChange: (Gladiator?) -> Unit
    ) {
        viewModel.updateGladiatorAction(actingGladiator, chosenAction)
        onActingGladiatorChange(findNextAvailableGladiator())
        if (viewModel.haveAllGladiatorsHadATurn()) {
            onTurnEnded(
                viewModel.gladiatorActions.value.values.toList().filterNotNull()
            )
            resetActions()
            onActingGladiatorChange(findNextAvailableGladiator())
        }
    }
}