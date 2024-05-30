package com.doorxii.ludus.data.models.equipment.weapon

import com.doorxii.ludus.data.models.equipment.EquipmentItem
import com.doorxii.ludus.data.models.equipment.EquipmentType
import kotlinx.serialization.Serializable

@Serializable
sealed class Weapon : EquipmentItem() {
    override val equipmentType: EquipmentType = EquipmentType.WEAPON
}