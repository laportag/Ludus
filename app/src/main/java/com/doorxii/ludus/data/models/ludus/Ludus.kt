package com.doorxii.ludus.data.models.ludus

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.doorxii.ludus.data.models.animal.Gladiator
import kotlinx.serialization.Serializable

@Entity(tableName = "ludus")
@Serializable
class Ludus(
    var name: String
) {
    @PrimaryKey(autoGenerate = true)
    var ludusId: Int = 0


    // Add an empty constructor
    constructor() : this("")

    companion object {
        const val TAG = "Ludus"
    }
}