package com.example.weatheronrouteapp.ui.MainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatheronrouteapp.ui.MapViewModel

@Composable
fun LocationInputBox(startLocation: String, endLocation: String, viewModel: MapViewModel) {
    Column(
        modifier = Modifier
            .wrapContentHeight(Alignment.Top)
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
    ) {
        TextField(
            modifier = Modifier.padding(8.dp).wrapContentHeight(Alignment.Top).fillMaxWidth(),
            value = startLocation,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Black,
                disabledIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                backgroundColor = Color.White
            ),
            onValueChange = { changedValue ->
                viewModel.setUiStateData(changedValue, endLocation)
            }

        )
        TextField(
            modifier = Modifier.padding(8.dp).wrapContentHeight(Alignment.Top).fillMaxWidth(),
            value = endLocation,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Black,
                disabledIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                backgroundColor = Color.White
            ),
            onValueChange = { changedValue ->
                viewModel.setUiStateData(startLocation, changedValue)
            }

        )
    }
}
