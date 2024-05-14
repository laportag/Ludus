package com.doorxii.ludus.data.models.equipment.weapon

import com.doorxii.ludus.data.models.equipment.EquipmentItem
import com.doorxii.ludus.data.models.equipment.EquipmentType

open class Weapon(name: String, type: EquipmentType = EquipmentType.WEAPON, attackBonus: Double, defenceBonus: Double,
                  id: Int
) :
    EquipmentItem(name, type, attackBonus, defenceBonus, id) {}