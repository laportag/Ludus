package com.doorxii.ludus.data.models.ludus

import com.doorxii.ludus.data.models.Model
import com.doorxii.ludus.data.models.animal.Gladiator
import kotlinx.serialization.Serializable

@Serializable
class Ludus(): Model() {
    var name: String = "Ludus"
    override val id: Int = 60
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