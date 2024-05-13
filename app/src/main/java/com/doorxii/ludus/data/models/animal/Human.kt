package com.doorxii.ludus.data.models.animal

import android.util.Log

open class Human(
    val name: String,
    val age: Double,
    val height: Double

): Animal() {

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

    fun updateHealth(health: Double) {
        this.health = health
    }


    companion object {
        const val TAG = "Human"
    }
}