package com.doorxii.ludus.data.models.equipment.armour

import com.doorxii.ludus.data.models.equipment.EquipmentType

class Armourless(
    name: String = "Armourless",
    type: EquipmentType = EquipmentType.ARMOUR,
    attackBonus: Double = 0.0,
    defenceBonus: Double = 0.0
) :
    Armour(name, type, attackBonus, defenceBonus) {
}