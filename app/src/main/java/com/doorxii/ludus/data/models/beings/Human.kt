package com.doorxii.ludus.data.models.beings

import android.util.Log

open class Human(
    val name: String,
    val age: Double,
    val height: Double

) {

    var health: Double = 100.0
        set(value) {
            field = if (value < 0.0) {
                0.0
            } else {
                value
            }
        }


    fun isAlive(): Boolean {
        if (health <= 0) {
            Log.d(TAG, "$name is dead")
            return false
        } else {
            return true
        }
    }

    companion object {
        const val TAG = "Human"
    }
}