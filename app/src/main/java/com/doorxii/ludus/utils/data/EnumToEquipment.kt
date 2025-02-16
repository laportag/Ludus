package com.doorxii.ludus.utils.data

import com.doorxii.ludus.data.models.equipment.armour.Armour
import com.doorxii.ludus.data.models.equipment.armour.Armourless
import com.doorxii.ludus.data.models.equipment.armour.Armours
import com.doorxii.ludus.data.models.equipment.armour.LightArmour
import com.doorxii.ludus.data.models.equipment.weapon.Barefist
import com.doorxii.ludus.data.models.equipment.weapon.Gladius
import com.doorxii.ludus.data.models.equipment.weapon.Weapon
import com.doorxii.ludus.data.models.equipment.weapon.Weapons

object EnumToEquipment {

    fun enumToWeapon(weaponEnum: Weapons): Weapon {
        return when (weaponEnum){
            Weapons.GLADIUS -> Gladius()
            else -> Barefist()
        }
    }

    fun enumToArmour(armourEnum: Armours): Armour {
        return when (armourEnum){
            Armours.LIGHT_ARMOUR -> LightArmour()
            else -> Armourless()
        }
    }


}