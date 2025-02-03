package com.doorxii.ludus.utils.combat

import com.doorxii.ludus.data.models.animal.Gladiator

class CombatResult(
    val playerGladiatorList: List<Gladiator>,
    val enemyGladiatorList: List<Gladiator>,
    var combatReport: String = ""
)