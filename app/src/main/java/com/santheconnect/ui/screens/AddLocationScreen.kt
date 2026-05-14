// =======================================
// AddLocationScreen.kt (MODERN UI)
// =======================================

package com.santheconnect.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.santheconnect.ui.components.MiniInfoCard

@Composable
fun AddLocationScreen() {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    var placeName by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf("") }
    var openDays by remember { mutableStateOf("") }

    var selectedCats by remember {
        mutableStateOf(setOf<String>())
    }

    var selectedLocation by remember {
        mutableStateOf<LatLng?>(null)
    }

    var isSubmitting by remember {
        mutableStateOf(false)
    }

    var showSuccess by remember {
        mutableStateOf(false)
    }

    // =========================
    // MAP CAMERA
    // =========================

    val cameraPositionState =
        rememberCameraPositionState {

            position = CameraPosition.fromLatLngZoom(
                LatLng(12.9716, 77.5946),
                10f
            )
        }

    // =========================
    // LOCATION PERMISSION
    // =========================

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                val fusedLocationClient =
                    LocationServices
                        .getFusedLocationProviderClient(context)

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->

                        location?.let {

                            val latLng = LatLng(
                                it.latitude,
                                it.longitude
                            )

                            selectedLocation = latLng

                            cameraPositionState.position =
                                CameraPosition.fromLatLngZoom(
                                    latLng,
                                    15f
                                )
                        }
                    }
            }
        }

    // =========================
    // MAIN UI
    // =========================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0F14))
            .verticalScroll(scrollState)
    ) {

        // ====================================
        // HERO SECTION
        // ====================================

        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFFFA726),
                            Color(0xFFFF7043),
                            Color(0xFF5D4037)
                        )
                    )
                )
                .padding(24.dp)
        ) {

            Column {

                Text(
                    "📍 ADD NEW SPOT",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    "Help Tourists\nDiscover Karnataka",
                    color = Color.White,
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    "Add local food places, village markets,\ncrafts & hidden gems to Santhe Connect.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(18.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    MiniInfoCard(
                        emoji = "🛒",
                        value = "50+",
                        label = "Markets"
                    )

                    MiniInfoCard(
                        emoji = "🍛",
                        value = "120+",
                        label = "Food Spots"
                    )
                }
            }
        }

        // ====================================
        // FORM CARD
        // ====================================

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFF161B22))
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.05f),
                    RoundedCornerShape(28.dp)
                )
                .padding(18.dp)
        ) {

            SectionTitle("🏷 Place Information")

            ModernField(
                label = "Place Name",
                value = placeName,
                placeholder = "Amma's Khana-Vali"
            ) {
                placeName = it
            }

            ModernField(
                label = "Village / Area",
                value = village,
                placeholder = "Mysuru"
            ) {
                village = it
            }

            ModernField(
                label = "Specialty",
                value = specialty,
                placeholder = "Best Ragi Mudde"
            ) {
                specialty = it
            }

            ModernField(
                label = "Open Days",
                value = openDays,
                placeholder = "Mon - Sat"
            ) {
                openDays = it
            }

            Spacer(Modifier.height(20.dp))

            // ====================================
            // CATEGORY SECTION
            // ====================================

            SectionTitle("🗂 Select Categories")

            Spacer(Modifier.height(14.dp))

            FlowRowCustom {

                CategoryChip(
                    "🍛 Eatery",
                    selectedCats.contains("Eatery")
                ) {

                    selectedCats =
                        if (selectedCats.contains("Eatery"))
                            selectedCats - "Eatery"
                        else
                            selectedCats + "Eatery"
                }

                CategoryChip(
                    "🛒 Market",
                    selectedCats.contains("Market")
                ) {

                    selectedCats =
                        if (selectedCats.contains("Market"))
                            selectedCats - "Market"
                        else
                            selectedCats + "Market"
                }

                CategoryChip(
                    "🏺 Crafts",
                    selectedCats.contains("Crafts")
                ) {

                    selectedCats =
                        if (selectedCats.contains("Crafts"))
                            selectedCats - "Crafts"
                        else
                            selectedCats + "Crafts"
                }

                CategoryChip(
                    "🏡 Home-Stay",
                    selectedCats.contains("Home-Stay")
                ) {

                    selectedCats =
                        if (selectedCats.contains("Home-Stay"))
                            selectedCats - "Home-Stay"
                        else
                            selectedCats + "Home-Stay"
                }

                CategoryChip(
                    "⭐ Must Visit",
                    selectedCats.contains("Must Visit")
                ) {

                    selectedCats =
                        if (selectedCats.contains("Must Visit"))
                            selectedCats - "Must Visit"
                        else
                            selectedCats + "Must Visit"
                }
            }

            Spacer(Modifier.height(24.dp))

            // ====================================
            // MAP SECTION
            // ====================================

            SectionTitle("🗺 Select Location")

            Spacer(Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng ->

                        selectedLocation = latLng
                    }
                ) {

                    selectedLocation?.let {

                        Marker(
                            state = MarkerState(position = it),
                            title = placeName.ifEmpty {
                                "Selected Spot"
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(14.dp)
                        .align(Alignment.TopEnd)
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF161B22))
                        .clickable {

                            when {

                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED -> {

                                    val fusedLocationClient =
                                        LocationServices
                                            .getFusedLocationProviderClient(context)

                                    fusedLocationClient.lastLocation
                                        .addOnSuccessListener { location ->

                                            location?.let {

                                                val latLng = LatLng(
                                                    it.latitude,
                                                    it.longitude
                                                )

                                                selectedLocation = latLng

                                                cameraPositionState.position =
                                                    CameraPosition
                                                        .fromLatLngZoom(
                                                            latLng,
                                                            15f
                                                        )
                                            }
                                        }
                                }

                                else -> {

                                    permissionLauncher.launch(
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    )
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = null,
                        tint = Color(0xFFFFA726)
                    )
                }
            }

            // ====================================
            // LOCATION STATUS
            // ====================================

            selectedLocation?.let {

                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF1B4332))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF95D5B2)
                    )

                    Spacer(Modifier.width(10.dp))

                    Column {

                        Text(
                            "Location Selected",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )

                        Spacer(Modifier.height(2.dp))

                        Text(
                            "Lat: ${it.latitude}\nLng: ${it.longitude}",
                            color = Color(0xFFB7E4C7),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ====================================
            // SUCCESS MESSAGE
            // ====================================

            if (showSuccess) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF1B4332))
                        .padding(14.dp)
                ) {

                    Text(
                        "✅ Location Added Successfully",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(14.dp))
            }

            // ====================================
            // LOADING
            // ====================================

            if (isSubmitting) {

                CircularProgressIndicator(
                    color = Color(0xFFFFA726)
                )

                Spacer(Modifier.height(14.dp))
            }

            // ====================================
            // SUBMIT BUTTON
            // ====================================

            Button(
                onClick = {

                    if (
                        placeName.isEmpty() ||
                        village.isEmpty() ||
                        selectedCats.isEmpty() ||
                        selectedLocation == null
                    ) {
                        return@Button
                    }

                    isSubmitting = true

                    val locationData = hashMapOf(
                        "name" to placeName,
                        "village" to village,
                        "specialty" to specialty,
                        "openDays" to openDays,
                        "categories" to selectedCats.toList(),
                        "latitude" to selectedLocation!!.latitude,
                        "longitude" to selectedLocation!!.longitude,
                        "timestamp" to System.currentTimeMillis()
                    )

                    db.collection("eatery_locations")
                        .add(locationData)
                        .addOnSuccessListener {

                            isSubmitting = false
                            showSuccess = true

                            placeName = ""
                            village = ""
                            specialty = ""
                            openDays = ""
                            selectedCats = emptySet()
                            selectedLocation = null

                            scope.launch {

                                delay(2500)
                                showSuccess = false
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFFFA726),
                                    Color(0xFFFF7043)
                                )
                            ),
                            RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        "🚀 Submit Location",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(30.dp))
    }
}

// =======================================
// MODERN FIELD
// =======================================

@Composable
fun ModernField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {

    Spacer(Modifier.height(12.dp))

    Text(
        label,
        color = Color.White,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp
    )

    Spacer(Modifier.height(8.dp))

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {

            Text(
                placeholder,
                color = Color.Gray
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White,
            focusedBorderColor = Color(0xFFFFA726),
            unfocusedBorderColor = Color(0xFF2A2F36),
            focusedContainerColor = Color(0xFF1E242C),
            unfocusedContainerColor = Color(0xFF1E242C)
        )
    )
}

// =======================================
// CATEGORY CHIP
// =======================================

@Composable
fun CategoryChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .padding(end = 10.dp, bottom = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (selected)
                    Color(0xFFFFA726)
                else
                    Color(0xFF1E242C)
            )
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {

        Text(
            text,
            color =
                if (selected)
                    Color.Black
                else
                    Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

// =======================================
// SECTION TITLE
// =======================================

@Composable
fun SectionTitle(text: String) {

    Text(
        text,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
}

// =======================================
// SIMPLE FLOW ROW
// =======================================

@Composable
fun FlowRowCustom(
    content: @Composable () -> Unit
) {

    Column {
        content()
    }
}
