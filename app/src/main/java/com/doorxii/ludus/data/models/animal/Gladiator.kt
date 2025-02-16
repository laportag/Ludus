package com.doorxii.ludus.data.models.animal

import android.util.Log
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.doorxii.ludus.data.models.actions.combatactions.ChosenAction
import com.doorxii.ludus.data.db.ChosenActionTypeConverter
import com.doorxii.ludus.data.models.equipment.Equipment
import com.doorxii.ludus.data.models.ludus.Ludus
import kotlinx.serialization.Serializable

@Entity(
    foreignKeys = [ForeignKey(
        entity = Ludus::class,
        parentColumns = ["ludusId"],
        childColumns = ["ludusId"]
    )
    ],
    indices = [
        Index(value = ["ludusId"])
    ]
)
@Serializable
class Gladiator: Human() {
    @PrimaryKey(autoGenerate = true)
    var gladiatorId: Int = 0
    var ludusId: Int = -1
    var morale: Double = 100.0
    var stamina: Double = 100.0
    var strength: Double = 60.0
    var speed: Double = 60.0
    var technique: Double = 60.0
    var bloodlust: Double = 60.0

    @Embedded
    var equipment: Equipment = Equipment()
    var humanControlled: Boolean = false

    var attack = calculateAttack()
    var defence = calculateDefence()

    @TypeConverters(ChosenActionTypeConverter::class)
    var action: ChosenAction? = null

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
//        Log.d(TAG, "getEquipmentAttackBonus: $total")
        return total
    }

    private fun getEquipmentDefenceBonus(): Double {
        val total = equipment.getDefenceBonus()
//        Log.d(TAG, "getEquipmentDefenceBonus: $total")
        return total
    }

    fun hasNoMorale(): Boolean {
        if (morale <= 0) {
            Log.d(TAG, "$name has no morale")
            return true
        } else {
            return false
        }
    }

    companion object {
        private const val TAG = "Gladiator"
    }

    override var id: Int = 11

}