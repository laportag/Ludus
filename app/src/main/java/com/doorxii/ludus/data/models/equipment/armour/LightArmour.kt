package com.doorxii.ludus.data.models.equipment.armour

import com.doorxii.ludus.data.models.equipment.EquipmentType
import kotlinx.serialization.Serializable

@Serializable
class LightArmour() : Armour() {
    override val name: String = "Light Armour"
    override var attackBonus: Double = 0.0
    override var defenceBonus: Double = 15.0
    val id: Int = 4
}