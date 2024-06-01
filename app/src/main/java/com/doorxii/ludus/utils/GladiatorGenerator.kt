package com.doorxii.ludus.utils

import android.util.Log
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.equipment.Equipment
import com.doorxii.ludus.data.models.equipment.armour.Armours
import com.doorxii.ludus.data.models.equipment.weapon.Weapons
import com.doorxii.ludus.utils.NameStrings.getRandomName
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random

object GladiatorGenerator {

    private fun newGladiator(): Gladiator {
        val glad = Gladiator()
        glad.id = Random.nextInt(200, 701)
        glad.name = getRandomName()
        glad.age = getRandomAge()
        glad.height = Random.nextInt(145, 211).toDouble()
        glad.health = 100.0
        glad.stamina = 100.0
        glad.morale = 100.0
        glad.strength = getRandomStat()
        glad.speed = getRandomStat()
        glad.technique = getRandomStat()
        glad.bloodlust = getRandomStat()
        glad.equipment = Equipment()
        if (Random.nextBoolean()){
            glad.equipment.weapon = Weapons.GLADIUS
        }
        else {
            glad.equipment.weapon = Weapons.BAREFIST
        }
        if (Random.nextBoolean()){
            glad.equipment.armour = Armours.LIGHT_ARMOUR
        } else {
            glad.equipment.armour = Armours.ARMOURLESS
        }

        val json = Json { prettyPrint = true }
        val jsonString = json.encodeToString(glad)
        Log.d(TAG, "new gladiator: $jsonString")
        return glad
    }

    fun newGladiatorList(size: Int): MutableList<Gladiator> {
        var count = 1
        val gladList = mutableListOf<Gladiator>()
        for (i in 1..size){
            gladList.add(newGladiator())
        }
        return gladList
    }

    fun newGladiatorListWithLudusId(size:Int, ludusId: Int): MutableList<Gladiator> {
        val list = newGladiatorList(size)
        for (gladiator in list){
            gladiator.ludusId = ludusId
        }
        return list
    }

    private fun getRandomAge():Double{
        val rangeStart = 15
        val rangeEnd = 40
        val biasRangeStart = 18
        val biasRangeEnd = 28

        // Generate a random number with bias
        val age = Random.nextInt(rangeStart, rangeEnd)
        val biasedRandomNumber = if (Random.nextBoolean()) {
            Random.nextInt(biasRangeStart, biasRangeEnd + 1)
        } else {
            age
        }
        return biasedRandomNumber.toDouble()
    }

    private fun getRandomStat(): Double{
        val rangeStart = 20
        val rangeEnd = 100
        val biasRangeStart = 55
        val biasRangeEnd = 70

        // Generate a random number with bias
        val age = Random.nextInt(rangeStart, rangeEnd)
        val biasedRandomNumber = if (Random.nextBoolean()) {
            Random.nextInt(biasRangeStart, biasRangeEnd + 1)
        } else {
            age
        }
        return biasedRandomNumber.toDouble()
    }

    const val TAG = "GladiatorGenerator"
}