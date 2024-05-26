package com.doorxii.ludus.data.models.equipment.weapon

import com.doorxii.ludus.data.models.equipment.EquipmentType
import kotlinx.serialization.Serializable

@Serializable
class Barefist() : Weapon() {
    override val name: String = "Barefists"
    override var attackBonus: Double = 0.0
    override var defenceBonus: Double = 0.0
    var id: Int = 2
}