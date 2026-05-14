package com.santheconnect.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

fun isNearby(
    user: LatLng,
    place: LatLng,
    radiusKm: Double = 2.0
): Boolean {

    val results = FloatArray(1)

    Location.distanceBetween(
        user.latitude,
        user.longitude,
        place.latitude,
        place.longitude,
        results
    )

    return (results[0] / 1000.0) <= radiusKm
}

@SuppressLint("MissingPermission")
fun getCurrentLocation(
    context: Context,
    onResult: (LatLng) -> Unit
) {
    val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            location?.let {
                onResult(LatLng(it.latitude, it.longitude))
            }
        }
}
