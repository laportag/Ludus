package com.doorxii.ludus.data.models.ludus

import com.doorxii.ludus.data.models.Model
import com.doorxii.ludus.data.models.animal.Gladiator
import kotlinx.serialization.Serializable

@Serializable
class Barracks(

): Model() {
    override val id: Int =55
    var gladiators: MutableList<Gladiator> = mutableListOf()
}