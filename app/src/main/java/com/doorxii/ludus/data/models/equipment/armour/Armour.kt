package com.doorxii.ludus.data.models.equipment.armour

import com.doorxii.ludus.data.models.equipment.EquipmentItem
import com.doorxii.ludus.data.models.equipment.EquipmentType
import kotlinx.serialization.Serializable

@Serializable
sealed class Armour:
    EquipmentItem() {
    override var equipmentType: EquipmentType = EquipmentType.ARMOUR
}