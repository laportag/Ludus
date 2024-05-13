package com.doorxii.ludus.data.models.equipment

class Gladius(
    name: String = "Gladius",
    type: EquipmentType = EquipmentType.Weapon,
    attackBonus: Double = 20.0,
    defenceBonus: Double = 5.0
) : Equipment(
    name, type, attackBonus, defenceBonus
) {

}