package com.doorxii.ludus.actions.combatactions

import com.doorxii.ludus.actions.ActionResult
import com.doorxii.ludus.data.models.animal.Gladiator

class CombatActionResult(
    val source: Gladiator,
    val target: Gladiator,
    val deltaHealth: Double,
    val deltaStamina: Double
): ActionResult