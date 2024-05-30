package com.doorxii.ludus.data.models.animal

import kotlinx.serialization.Serializable

@Serializable
abstract class Animal {
    abstract val health: Double
}