package com.doorxii.ludus.data.models.equipment.armour


import kotlinx.serialization.Serializable

@Serializable
class Armourless : Armour() {
    override val name: String = "Armourless"
    override var attackBonus: Double = 0.0
    override var defenceBonus: Double = 0.0
    var id: Int = -1
}