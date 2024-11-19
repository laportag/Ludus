package com.doorxii.ludus.data.db

import androidx.room.TypeConverter
import com.doorxii.ludus.actions.combatactions.ChosenAction
import kotlinx.serialization.json.Json

class ChosenActionTypeConverter {
    @TypeConverter
    fun fromChosenAction(chosenAction: ChosenAction?): String? {
        return chosenAction?.let { Json.encodeToString(ChosenAction.serializer(), it) }
    }

    @TypeConverter
    fun toChosenAction(chosenActionString: String?): ChosenAction? {
        return chosenActionString?.let { Json.decodeFromString(ChosenAction.serializer(), it) }
    }
}