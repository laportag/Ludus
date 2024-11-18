package com.doorxii.ludus.utils.dice

object Dice {
    fun roll(numberofDice: Int, diceType: DiceTypes): List<Int>{
        val diceSize = when(diceType){
            DiceTypes.D6 -> 6
            DiceTypes.D20 -> 20
        }
        val rollResultList = mutableListOf<Int>()

        for (die in 1..numberofDice) {
            val res = (1..diceSize).random()
            rollResultList.add(res)
        }
        return rollResultList
    }

    fun totalRolls(rollList: List<Int>, modifier: Double = 0.0): Double{
        var rollTotal = 0.0
        for (roll in rollList){
            rollTotal += roll
        }
        rollTotal += modifier
        return rollTotal
    }

    fun calculateModifier(modifier: Double): Double{
        var modifierTotal = 0.0
        modifierTotal += modifier

        return modifierTotal
    }
}
