package com.doorxii.ludus.data.models.animal

import android.util.Log
import com.doorxii.ludus.actions.combatactions.CombatAction
import com.doorxii.ludus.data.models.equipment.Equipment


class Gladiator(
    name: String,
    age: Double,
    height: Double,
    health: Double,
    var morale: Double,
    var stamina: Double,
    val strength: Double,
    val speed: Double,
    val technique: Double,
    val bloodlust: Double,
    var equipment: Equipment,
    var humanControlled: Boolean = false,

    ) : Human(name, age, height) {

    var attack = calculateAttack()
    var defence = calculateDefence()

    var action: CombatAction? = null

    private fun calculateAttack(): Double {
        val fatigue = 100 - ((morale + stamina) / 200)
        val power = (strength + speed + technique / 3) + (bloodlust / 50)
        val total = power + getEquipmentAttackBonus() - fatigue
        this.attack = total
        return attack
    }

    private fun calculateDefence(): Double {
        val fatigue = 100 - ((morale + stamina) / 200)
        val power = (strength + speed + technique / 3)
        val total = power + getEquipmentDefenceBonus() - fatigue
        this.defence = total
        return defence
    }

    private fun getEquipmentAttackBonus(): Double {
        val total = equipment.getAttackBonus()
        Log.d(TAG, "getEquipmentAttackBonus: $total")
        return total
    }

    private fun getEquipmentDefenceBonus(): Double {
        val total = equipment.getDefenceBonus()
        Log.d(TAG, "getEquipmentDefenceBonus: $total")
        return total
    }

    fun updateMorale(morale: Double) {
        this.morale = morale
    }

    companion object {
        private const val TAG = "Gladiator"
    }

}