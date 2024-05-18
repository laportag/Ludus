package com.doorxii.ludus.data.models.animal

import android.util.Log
import com.doorxii.ludus.actions.combatactions.CombatAction
import com.doorxii.ludus.data.models.equipment.Equipment
import kotlinx.serialization.Serializable

@Serializable
class Gladiator() : Human() {

    var morale: Double = 100.0
    var stamina: Double = 100.0
    var strength: Double = 60.0
    var speed: Double = 60.0
    var technique: Double = 60.0
    var bloodlust: Double = 60.0
    var equipment: Equipment = Equipment()
    var humanControlled: Boolean = false

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

    override var id: Int = 11

}