package com.doorxii.ludus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class Colour {

    @Preview
    @Composable
    fun ColourTest(){
        Box(
            modifier = Modifier
                .aspectRatio(9f / 16f)
                .background(color = Color(0xCC1D1D20), RoundedCornerShape(8.dp))
                .padding(4.dp)
        ) {


        }
    }

}