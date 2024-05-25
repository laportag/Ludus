package com.doorxii.ludus.data.models.ludus

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.doorxii.ludus.data.models.Model
import com.doorxii.ludus.data.models.animal.Gladiator
import kotlinx.serialization.Serializable

@Entity(tableName = "ludus")
@Serializable
class Ludus(): Model() {
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 60
    var name: String = "Ludus"
    val barracks: Barracks = Barracks()

    companion object {
        const val TAG = "Ludus"

        fun init(name: String, gladaiatorList: List<Gladiator>): Ludus{
            val ludus = Ludus()
            ludus.name = name
            ludus.barracks.gladiators = gladaiatorList.toMutableList()
            return ludus
        }
    }
}