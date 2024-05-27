package com.doorxii.ludus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.doorxii.ludus.data.db.AppDatabase
import com.doorxii.ludus.ui.theme.LudusTheme

class LudusManagementActivity(db: AppDatabase): ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            LudusTheme {
                LudusManagementLayout()
            }
        }

    }


    @Composable
    fun LudusManagementLayout(){

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

        }
    }

    override fun finishActivity(requestCode: Int) {
        super.finishActivity(requestCode)
    }



}