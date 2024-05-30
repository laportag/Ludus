package com.doorxii.ludus.data.models.animal

import android.util.Log


abstract class Human : Animal() {
    var name: String = ""
    var age: Double = 0.0
    var height: Double = 150.0
    override var health: Double = 100.0
        set(value) {
            field = if (value <= 0.0) {
                0.0
            } else {
                value
            }
            if (field <= 0.0) {
                Log.d(TAG, "$name is dead")
            }
        }
    abstract var id: Int


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