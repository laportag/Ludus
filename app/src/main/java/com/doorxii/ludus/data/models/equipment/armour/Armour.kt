package com.doorxii.ludus.data.models.equipment.armour

import com.doorxii.ludus.data.models.equipment.EquipmentItem
import com.doorxii.ludus.data.models.equipment.EquipmentType

open class Armour(name: String, type: EquipmentType = EquipmentType.ARMOUR, attackBonus: Double, defenceBonus: Double,
                  id: Int
) :
    EquipmentItem(name, type, attackBonus, defenceBonus, id) {
}