package com.doorxii.ludus.data.models.equipment.weapon

import com.doorxii.ludus.data.models.equipment.EquipmentType

class Gladius(
    name: String = "Gladius",
    type: EquipmentType = EquipmentType.WEAPON,
    attackBonus: Double = 20.0,
    defenceBonus: Double = 5.0, id: Int
) : Weapon(
    name, type, attackBonus, defenceBonus, id
) {

}