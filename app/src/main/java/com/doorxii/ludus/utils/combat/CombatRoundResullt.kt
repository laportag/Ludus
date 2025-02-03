package com.doorxii.ludus.utils.combat

import com.doorxii.ludus.data.models.animal.Gladiator

class CombatRoundResult (
    var playerGladiatorList: List<Gladiator>,
    var enemyGladiatorList: List<Gladiator>,
    val submittedPlayerGladiatorList: MutableList<Gladiator> = mutableListOf(),
    val submittedEnemyGladiatorList: MutableList<Gladiator> = mutableListOf()
)