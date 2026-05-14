package com.santheconnect.data

import androidx.compose.runtime.mutableStateOf

object SelectedSanthe {

    val selectedLat = mutableStateOf(0.0)

    val selectedLng = mutableStateOf(0.0)

    val selectedName = mutableStateOf("")
}