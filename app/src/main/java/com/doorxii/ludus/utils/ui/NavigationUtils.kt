package com.doorxii.ludus.utils.ui

import android.content.Context
import android.content.Intent
import com.doorxii.ludus.ui.activities.StartActivity

object NavigationUtils {
    // Function to navigate to the start activity
    fun navigateToStartActivity(context: Context) {
        // Replace StartActivity::class.java with the actual class of your start activity
        val intent = Intent(context, StartActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }
}