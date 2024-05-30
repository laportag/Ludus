package com.doorxii.ludus.data.models.ludus

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "ludus")
@Serializable
class Ludus(
    var name: String
) {
    @PrimaryKey(autoGenerate = true)
    var ludusId: Int = 0
    var playerLudus = false

    // Add an empty constructor
    constructor() : this("")

    companion object {
        const val TAG = "Ludus"
    }
}