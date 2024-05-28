package com.doorxii.ludus.utils

import android.content.Context
import android.util.Log
import com.doorxii.ludus.combat.Combat
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object CombatSerialization {

    fun saveCombatJson(combat: Combat, combatFile: File) {
        Log.d(TAG, "saving combat to file...")
        val json = Json { prettyPrint = true }
        val jsonString = json.encodeToString(combat)
        combatFile.writeText(jsonString)
    }

    fun readCombatFromFile(file: File): Combat {
        Log.d(TAG, "reading combat from file...")
        val json = Json { ignoreUnknownKeys = true }
        val jsonString = file.readText()
        return json.decodeFromString(jsonString)
    }

    fun returnCombatFile(context: Context): File {
        val path = File(context.filesDir, "combat/combat.json")
        val exists = path.exists()
        if (!exists) {
            path.mkdirs()
            path.createNewFile()
        }
        return path
    }

    private const val TAG = "CombatSerialization"

}