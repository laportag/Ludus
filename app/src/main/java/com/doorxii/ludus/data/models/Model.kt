package com.doorxii.ludus.data.models

import kotlinx.serialization.Serializable

@Serializable
abstract class Model(){
    abstract val id: Int
}