package com.doorxii.ludus.data.models.actions.combatactions

import com.doorxii.ludus.data.models.actions.ActionResult
import com.doorxii.ludus.data.models.animal.Gladiator

class CombatActionResult(
    val actor: Gladiator,
    val target: Gladiator?,
    val deltaHealth: Double,
    val deltaStamina: Double
): ActionResult