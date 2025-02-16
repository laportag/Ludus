package com.doorxii.ludus.utils.combat

import android.content.Context
import android.util.Log
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
        val path = File(context.filesDir, "combat")
        val file = File(path, "combat.json")

        // Check if the path exists and if it's a directory
        if (path.exists()) {
            if (!path.isDirectory) {
                Log.e(TAG, "Error: 'combat' path exists but is NOT a directory!")
                // You might need to delete the invalid file or handle it differently
                path.delete() // Deleting it so the code can recreate the folder
            } else {
                Log.d(TAG, "'combat' directory exists.")
            }
        }

        // Check if we need to create the path
        if (!path.exists()) {
            Log.d(TAG, "'combat' directory does not exist, creating it...")
            if (path.mkdirs()) {
                Log.d(TAG, "'combat' directory created successfully.")
            } else {
                Log.e(TAG, "Error: Failed to create 'combat' directory!")
            }
        }

        // Check if the file exists and if it's a directory
        if (file.exists()) {
            if (file.isDirectory) {
                Log.e(TAG, "Error: 'combat.json' exists but is a directory!")
                file.delete() // Deleting it so the code can recreate the file
            } else {
                Log.d(TAG, "'combat.json' file exists.")
            }
        }

        // Create the file if it doesn't exist
        if (!file.exists()) {
            Log.d(TAG, "'combat.json' file does not exist, creating it...")
            try {
                if (file.createNewFile()) {
                    Log.d(TAG, "'combat.json' file created successfully.")
                    Thread.sleep(100) // Add a small delay (see explanation below)
                } else {
                    Log.e(TAG, "Error: Failed to create 'combat.json' file!")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception creating 'combat.json': ${e.message}")
            }
        }

        Log.d(TAG, "Returning file: ${file.absolutePath}")
        return file
    }

    private const val TAG = "CombatSerialization"

}