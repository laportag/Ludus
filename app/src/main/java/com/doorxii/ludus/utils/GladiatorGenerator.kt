package com.doorxii.ludus.utils

import android.util.Log
import com.doorxii.ludus.MainActivity
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.equipment.Equipment
import com.doorxii.ludus.data.models.equipment.armour.Armourless
import com.doorxii.ludus.data.models.equipment.armour.LightArmour
import com.doorxii.ludus.data.models.equipment.weapon.Barefist
import com.doorxii.ludus.data.models.equipment.weapon.Gladius
import com.doorxii.ludus.utils.NameStrings.getRandomName
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.random.Random

object GladiatorGenerator {

    fun newGladiator(): Gladiator {
        val glad = Gladiator()
        glad.name = getRandomName()
        glad.age = getRandomAge()
        glad.height = Random.nextInt(150, 200).toDouble()
        glad.health = 100.0
        glad.stamina = 100.0
        glad.morale = 100.0
        glad.strength = getRandomStat()
        glad.speed = getRandomStat()
        glad.technique = getRandomStat()
        glad.bloodlust = getRandomStat()
        glad.equipment = Equipment()
        if (Random.nextBoolean()){
            glad.equipment.weapon = Gladius()
        }
        else {
            glad.equipment.weapon = Barefist()
        }
        if (Random.nextBoolean()){
            glad.equipment.armour = LightArmour()
        } else {
            glad.equipment.armour = Armourless()
        }

        val json = Json { prettyPrint = true }
        val jsonString = json.encodeToString(glad)
        Log.d(TAG, "new gladiator: $jsonString")
        return glad
    }

    fun doubleToOneSigFig(number: Double): Double {
        val exponent = floor(log10(abs(number))).toDouble()

        // Calculate the first significant digit
        val firstDigit = floor(number / 10.0.pow(exponent)).toInt()

        // Return the number rounded to 1 significant figure
        return firstDigit * 10.0.pow(exponent)
    }

    fun getRandomAge():Double{
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

    fun getRandomStat(): Double{
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


//    var strength: Double = 60.0
//    var speed: Double = 60.0
//    var technique: Double = 60.0
//    var bloodlust: Double = 60.0
//    var equipment: Equipment = Equipment()
//    var humanControlled: Boolean = false

    const val TAG = "GladiatorGenerator"
}