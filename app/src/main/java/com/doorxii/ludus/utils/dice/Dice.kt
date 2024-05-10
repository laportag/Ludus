package com.doorxii.ludus.utils.dice

class Dice {
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

    fun totalRolls(rollList: List<Int>, modifier: Int): Int{
        var rollTotal = 0
        for (roll in rollList){
            rollTotal += roll
        }
        rollTotal += modifier
        return rollTotal
    }

    fun calculateModifier(): Int{
        val modifierTotal = 0


        return modifierTotal
    }
}
