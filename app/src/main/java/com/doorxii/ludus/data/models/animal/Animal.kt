package com.doorxii.ludus.data.models.animal

import com.doorxii.ludus.data.models.Model
import kotlinx.serialization.Serializable

@Serializable
abstract class Animal(): Model() {
    abstract val health: Double
}