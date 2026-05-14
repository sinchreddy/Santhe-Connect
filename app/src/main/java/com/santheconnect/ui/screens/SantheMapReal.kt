package com.santheconnect.ui.screens

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.fillMaxSize
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.santheconnect.data.SelectedSanthe
import com.santheconnect.data.Santhe

@Composable
fun SantheMapReal() {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val lat = SelectedSanthe.selectedLat.value
    val lng = SelectedSanthe.selectedLng.value
    val name = SelectedSanthe.selectedName.value

    var santhes by remember { mutableStateOf<List<Santhe>>(emptyList()) }
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }
    var markersAdded by remember { mutableStateOf(false) } // ✅ prevent loop

    val mapView = remember {
        MapView(context).apply { onCreate(Bundle()) }
    }

    // 🔥 Fetch Firestore data
    LaunchedEffect(Unit) {
        db.collection("santhes")
            .get()
            .addOnSuccessListener { result ->
                val list = result.map { it.toObject(Santhe::class.java) }
                println("🔥 FIRESTORE DATA: $list")
                santhes = list
            }
    }

    // ✅ Lifecycle
    DisposableEffect(Unit) {
        mapView.onStart()
        mapView.onResume()

        onDispose {
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize()
    ) {
        mapView.getMapAsync { map ->
            googleMap = map
        }
    }

    // ✅ Add markers ONLY ONCE
    LaunchedEffect(santhes) {

        val map = googleMap ?: return@LaunchedEffect
        if (santhes.isEmpty() || markersAdded) return@LaunchedEffect

        map.clear()

        val boundsBuilder = LatLngBounds.Builder()

        santhes.forEach { s ->

            val position = LatLng(s.lat, s.lng)

            val color = when (s.type.lowercase()) {
                "market" -> BitmapDescriptorFactory.HUE_GREEN
                "food"   -> BitmapDescriptorFactory.HUE_ORANGE
                "craft"  -> BitmapDescriptorFactory.HUE_BLUE
                else     -> BitmapDescriptorFactory.HUE_RED
            }

            map.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(s.name)
                    .snippet("Type: ${s.type} • Day: ${s.day}")
                    .icon(BitmapDescriptorFactory.defaultMarker(color))
            )

            boundsBuilder.include(position)
        }

        // ✅ Camera move ONLY once
        try {
            val bounds = boundsBuilder.build()
            map.animateCamera(
                CameraUpdateFactory.newLatLngBounds(bounds, 150)
            )
        } catch (e: Exception) {
            val first = santhes.first()
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(first.lat, first.lng), 12f
                )
            )
        }

        markersAdded = true // 🚀 STOP LOOP
    }
}