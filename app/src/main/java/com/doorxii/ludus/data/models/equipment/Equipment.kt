package com.doorxii.ludus.data.models.equipment

import com.doorxii.ludus.data.models.equipment.armour.Armour
import com.doorxii.ludus.data.models.equipment.armour.Armourless
import com.doorxii.ludus.data.models.equipment.weapon.Barefist
import com.doorxii.ludus.data.models.equipment.weapon.Weapon

class Equipment(
    private var weapon: Weapon = Barefist(),
    private var armour: Armour = Armourless()
) {
    fun getAttackBonus(): Double {
        return weapon.attackBonus + armour.attackBonus
    }

    fun getDefenceBonus(): Double {
        return weapon.defenceBonus + armour.defenceBonus
    }
}