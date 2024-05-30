package com.doorxii.ludus.data.models.equipment

import kotlinx.serialization.Serializable

@Serializable
abstract class EquipmentItem {
    abstract val name: String
    abstract val equipmentType: EquipmentType
    abstract val attackBonus: Double
    abstract val defenceBonus: Double
}
