package com.doorxii.ludus.data.models.equipment

import com.doorxii.ludus.data.models.Model

open class EquipmentItem(
    val name: String,
    val type: EquipmentType,
    val attackBonus: Double,
    val defenceBonus: Double,
    id: Int
): Model(id) {

}
