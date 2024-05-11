package com.doorxii.ludus.data.models.beings

import android.util.Log
import android.widget.NumberPicker.OnValueChangeListener

open class Human(
    val name: String,
    val age: Double,
    val height: Double,
    var health: Double
) {

    fun isAlive(): Boolean {
        if (health <= 0) {
            Log.d(TAG, "$name is dead")
            return false
        }
        else { return true }
    }

    companion object {
        const val TAG = "Human"
    }
}