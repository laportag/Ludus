package com.doorxii.ludus.utils.actions.combatactions

import com.doorxii.ludus.utils.actions.ActionResult
import com.doorxii.ludus.data.models.animal.Gladiator

class CombatActionResult(
    val actor: Gladiator,
    val target: Gladiator,
    val deltaHealth: Double,
    val deltaStamina: Double
): ActionResult