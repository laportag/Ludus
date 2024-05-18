package com.doorxii.ludus.data.models.equipment.weapon

import com.doorxii.ludus.data.models.equipment.EquipmentType
import kotlinx.serialization.Serializable

@Serializable
class Gladius() : Weapon(
) {
    override var name: String = "Gladius"
    override var attackBonus: Double = 20.0
    override var defenceBonus: Double = 5.0
    override var id: Int = 1
}