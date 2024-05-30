package com.doorxii.ludus.data.models.equipment.weapon

import kotlinx.serialization.Serializable

@Serializable
class Gladius : Weapon(
) {
    override var name: String = "Gladius"
    override var attackBonus: Double = 20.0
    override var defenceBonus: Double = 5.0
    var id: Int = 1
}