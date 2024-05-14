package com.doorxii.ludus.data.models.equipment.armour

import com.doorxii.ludus.data.models.equipment.EquipmentType

class LightArmour(
    name: String = "Light Armour",
    type: EquipmentType = EquipmentType.ARMOUR,
    attackBonus: Double = 0.0,
    defenceBonus: Double = 15.0, id: Int
) : Armour(name, type, attackBonus, defenceBonus, id) {
}