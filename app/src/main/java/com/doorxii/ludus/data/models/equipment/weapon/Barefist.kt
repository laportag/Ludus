package com.doorxii.ludus.data.models.equipment.weapon

import com.doorxii.ludus.data.models.equipment.EquipmentType

class Barefist(
    name: String = "Barefists",
    type: EquipmentType = EquipmentType.WEAPON,
    attackBonus: Double = 0.0,
    defenceBonus: Double = 0.0,
    id: Int = -1
) :
    Weapon(name, type, attackBonus, defenceBonus, id) {
}