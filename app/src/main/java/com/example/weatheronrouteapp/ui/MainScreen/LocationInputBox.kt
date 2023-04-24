package com.example.weatheronrouteapp.ui.MainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.weatheronrouteapp.ui.MapViewModel

@Composable
fun LocationInputBox(
    viewModel: MapViewModel,
    navController: NavHostController
) {
    val locationFields by viewModel.locationFields.collectAsState()
    val addressList by viewModel.addressList.collectAsState()
    val startLocation = locationFields.originString
    val endLocation = locationFields.destinationString
    val lastModifiedFieldWasStart by viewModel.lastEditedWasStart.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight(Alignment.Top)
                .padding(8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentHeight(Alignment.Top)
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                    },
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
                    viewModel.setUiStateData(changedValue, endLocation,true)
                    viewModel.getGeocoderData(changedValue)
                }
            )
            TextField(
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentHeight(Alignment.Top)
                    .fillMaxWidth(),
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
                    viewModel.setUiStateData(startLocation, changedValue,false)
                    viewModel.getGeocoderData(changedValue)
                }
            )
            LazyColumn(modifier = Modifier.padding(12.dp)) {
                items(addressList) {address ->
                    Card(
                        modifier = Modifier.clickable {
                            lastModifiedFieldWasStart?.let { start ->
                                if (start){
                                    viewModel.setUiStateData(address,endLocation,start)
                                }
                                else{
                                    viewModel.setUiStateData(startLocation,address,start)
                                }
                            }

                        }
                    ){
                        Text(text = address)
                    }
                }
            }
        }
        Button(
            onClick = {
                viewModel.getDirections()
                navController.popBackStack()
                viewModel.resetLocationFields()
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Bottom)
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(18.dp)
                ),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
        ) {
            Text(
                text = "Show Timeline",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
