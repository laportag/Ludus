package com.doorxii.ludus.data.models.beings

import android.util.Log
import com.doorxii.ludus.data.models.equipment.Equipment


class Gladiator(
    name: String,
    age: Double,
    height: Double,
    health: Double,
    val strength: Double,
    val speed: Double,
    val technique: Double,
    val morale: Double,
    var stamina: Double,
    val bloodlust: Double,
    var equipment: List<Equipment>

) : Human(name, age, height, health) {

    var attack = calculateAttack()
    var defence = calculateDefence()


    fun calculateAttack(): Double {
        val fatigue = 100 - ((morale + stamina) / 200)
        val power = (strength + speed + technique / 3) + (bloodlust / 50)
        val total = power + getEquipmentAttackBonus() - fatigue
        this.attack = total
        return attack
    }

    fun calculateDefence(): Double {
        val fatigue = 100 - ((morale + stamina) / 200)
        val power = (strength + speed + technique / 3)
        val total = power + getEquipmentDefenceBonus() - fatigue
        this.defence = total
        return defence
    }

    fun getEquipmentAttackBonus(): Double {
        var total = 0.0
        for (item in equipment){
            total += item.attackBonus
        }

        Log.d(TAG, "getEquipmentAttackBonus: $total")
        return total
    }

    fun getEquipmentDefenceBonus(): Double {
        var total = 0.0
        for (item in equipment){
            total += item.defenceBonus
        }
        Log.d(TAG, "getEquipmentDefenceBonus: $total")
        return total
    }

    fun updateHealth() {

    }

    fun updateMorale() {

    }

    companion object {
        private const val TAG = "Gladiator"
    }

}