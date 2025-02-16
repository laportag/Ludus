package com.doorxii.ludus.data.models.actions.combatactions

import kotlinx.serialization.Serializable

@Serializable
data class ChosenAction(
    val actingGladiatorID: Int,
    val targetGladiatorID: Int = 0, // 0 if targetless
    val action: CombatActions
) {
    val actionName: String = action.name
}