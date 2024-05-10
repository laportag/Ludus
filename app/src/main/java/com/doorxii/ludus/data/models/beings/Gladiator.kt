package com.doorxii.ludus.data.models.beings

import com.doorxii.ludus.data.models.equipment.Equipment


class Gladiator(
    name: String,
    age: Double,
    height: Double,
    val strength: Double,
    val speed: Double,
    val technique: Double,
    val morale: Double,
    val health: Double,
    val stamina: Double,
    val bloodlust: Double,
    var attack: Double,
    var defence: Double,
    var equipment: MutableList<Equipment>

) : Human(name, age, height) {


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


        return 10.0
    }

    fun getEquipmentDefenceBonus(): Double {
        var total = 0.0
        for (item in equipment){
            total += item.defenceBonus
        }

        return 25.0
    }

    fun updateHealth() {

    }

    fun updateMorale() {

    }

}