package com.doorxii.ludus.combat

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

class CombatContract: ActivityResultContract<Unit, Uri?>() {
    override fun createIntent(context: Context, input: Unit): Intent {

        return Intent(Intent.ACTION_PICK, )
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {

        if (resultCode == RESULT_OK) {
            return intent?.data
        }
        return null

    }

}