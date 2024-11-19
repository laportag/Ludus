package com.doorxii.ludus.combat

import com.doorxii.ludus.data.models.animal.Gladiator

class CombatResult(
    val playerGladiatorList: List<Gladiator>,
    val enemyGladiatorList: List<Gladiator>,
    var combatReport: String = ""
)