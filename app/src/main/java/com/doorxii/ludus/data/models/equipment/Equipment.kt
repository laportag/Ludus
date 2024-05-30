package com.doorxii.ludus.data.models.equipment

import com.doorxii.ludus.data.models.equipment.armour.Armours
import com.doorxii.ludus.data.models.equipment.weapon.Weapons
import com.doorxii.ludus.utils.enums.EnumToEquipment.enumToWeapon
import com.doorxii.ludus.utils.enums.EnumToEquipment.enumToArmour
import kotlinx.serialization.Serializable

@Serializable
class Equipment {

    var weapon: Weapons = Weapons.BAREFIST
    var armour: Armours = Armours.ARMOURLESS
    fun getAttackBonus(): Double {
        return enumToWeapon(weapon).attackBonus + enumToArmour(armour).attackBonus
    }

    fun getDefenceBonus(): Double {
        return enumToWeapon(weapon).defenceBonus + enumToArmour(armour).defenceBonus
    }
}