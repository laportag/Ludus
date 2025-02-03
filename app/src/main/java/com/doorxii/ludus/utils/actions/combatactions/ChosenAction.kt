package com.doorxii.ludus.utils.actions.combatactions

import kotlinx.serialization.Serializable

@Serializable
data class ChosenAction(
    val actingGladiatorID: Int,
    val targetGladiatorID: Int,
    val action: CombatActions
) {
    val actionName: String = action.name
}