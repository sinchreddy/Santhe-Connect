package com.santheconnect.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.santheconnect.data.SelectedSanthe
import com.santheconnect.ui.AppTab
import kotlinx.coroutines.tasks.await
import kotlin.math.*

data class SantheItem(
    val id: String = "",
    val name: String = "",
    val day: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val type: String = "",
    val distance: String = ""
)

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen(
    today: String,
    onNavigate: (AppTab) -> Unit
) {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    var currentLat by remember { mutableStateOf(0.0) }
    var currentLng by remember { mutableStateOf(0.0) }

    var todaySanthes by remember {
        mutableStateOf(listOf<SantheItem>())
    }

    // 📍 GET CURRENT LOCATION
    LaunchedEffect(Unit) {

        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context)

            val location =
                fusedLocationClient.lastLocation.await()

            location?.let {

                currentLat = it.latitude
                currentLng = it.longitude
            }
        }

        // 🔥 FETCH SANTHES FROM FIRESTORE
        db.collection("santhes")
            .addSnapshotListener { snapshot, _ ->

                val list = mutableListOf<SantheItem>()

                snapshot?.documents?.forEach { doc ->

                    val name =
                        doc.getString("name") ?: ""

                    val day =
                        doc.getString("day") ?: ""

                    val lat =
                        doc.getDouble("lat") ?: 0.0

                    val lng =
                        doc.getDouble("lng") ?: 0.0

                    val type =
                        doc.getString("type") ?: ""

                    // 📏 CALCULATE REAL DISTANCE
                    val results = FloatArray(1)

                    android.location.Location.distanceBetween(
                        currentLat,
                        currentLng,
                        lat,
                        lng,
                        results
                    )

                    val distanceInKm = results[0] / 1000

                    val distanceText =
                        if (distanceInKm < 1) {
                            "${(distanceInKm * 1000).toInt()} m away"
                        } else {
                            String.format("%.1f km away", distanceInKm)
                        }

                    if (day == today) {

                        list.add(
                            SantheItem(
                                id = doc.id,
                                name = name,
                                day = day,
                                lat = lat,
                                lng = lng,
                                type = type,
                                distance = distanceText
                            )
                        )
                    }
                }

                todaySanthes = list
            }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0F14))
            .verticalScroll(scrollState)
    ) {

        // 🌅 HERO SECTION
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
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
                    "🌾 SANTHE CONNECT",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    "Explore the\nReal Karnataka",
                    color = Color.White,
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    "Discover local markets, authentic food,\nrural culture & hidden gems.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    MiniStatCard(
                        emoji = "🛒",
                        value = "${todaySanthes.size}",
                        label = "Today's Markets"
                    )

                    MiniStatCard(
                        emoji = "📍",
                        value = "Live",
                        label = "Location Enabled"
                    )
                }
            }
        }

        // 🚀 QUICK ACCESS
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {

            Text(
                "✨ Quick Explore",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(14.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    FeatureCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🗺️",
                        title = "Santhe Map",
                        desc = "Weekly village markets",
                        bgColor = Color(0xFF1B4332),
                        iconBg = Color(0xFF2D6A4F)
                    ) {
                        onNavigate(AppTab.Map)
                    }

                    FeatureCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🍛",
                        title = "Find Local",
                        desc = "Authentic food spots",
                        bgColor = Color(0xFF5F0F40),
                        iconBg = Color(0xFF9A031E)
                    ) {
                        onNavigate(AppTab.Eat)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    FeatureCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🏺",
                        title = "Crafts",
                        desc = "Handmade traditions",
                        bgColor = Color(0xFF3C096C),
                        iconBg = Color(0xFF7B2CBF)
                    ) {
                        onNavigate(AppTab.Map)
                    }

                    FeatureCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🏡",
                        title = "Home-Stays",
                        desc = "Village experiences",
                        bgColor = Color(0xFF003049),
                        iconBg = Color(0xFF0077B6)
                    ) {
                        onNavigate(AppTab.Map)
                    }
                }
            }
        }

        Spacer(Modifier.height(26.dp))

        // 📅 TODAY'S SANTHE
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "📅 Today's Santhe",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    today,
                    color = Color(0xFFFFA726),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(14.dp))

            if (todaySanthes.isEmpty()) {

                EmptyStateCard()

            } else {

                todaySanthes.forEach { santhe ->

                    TodaySantheCard(
                        santhe = santhe,
                        onClick = {
                            SelectedSanthe.selectedLat.value = santhe.lat

                            SelectedSanthe.selectedLng.value = santhe.lng

                            SelectedSanthe.selectedName.value = santhe.name

                            onNavigate(AppTab.Map)
                        }
                    )

                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ⭐ COMMUNITY REVIEWS
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFF161B22))
                .padding(20.dp)
        ) {

            Column {

                Text(
                    "🌟 Community Reviews",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "Read tourist experiences and hidden gems shared by the community.",
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        onNavigate(AppTab.Review)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFA726)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {

                    Text(
                        "Open Reviews",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(30.dp))
    }
}

@Composable
fun TodaySantheCard(
    santhe: SantheItem,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF161B22))
            .clickable {
                onClick()
            }
            .padding(18.dp)
    ) {

        Text(
            santhe.name,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "📍 ${santhe.distance} away",
            color = Color(0xFFFFA726),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(6.dp))

        Text(
            "🏷 ${santhe.type}",
            color = Color.LightGray,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(12.dp))

        Text(
            "🗺 Tap to open in Santhe Map",
            color = Color(0xFF64B5F6),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun FeatureCard(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    desc: String,
    bgColor: Color,
    iconBg: Color,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
            .height(150.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(bgColor)
            .clickable {
                onClick()
            }
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {

            Text(
                emoji,
                fontSize = 24.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(4.dp))

        Text(
            desc,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun MiniStatCard(
    emoji: String,
    value: String,
    label: String
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.15f))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                emoji,
                fontSize = 18.sp
            )

            Spacer(Modifier.width(10.dp))

            Column {

                Text(
                    value,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Text(
                    label,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun EmptyStateCard() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF161B22))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "😔",
                fontSize = 34.sp
            )

            Spacer(Modifier.height(10.dp))

            Text(
                "No Santhe Today",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "Check back tomorrow for local market updates.",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

// 📏 DISTANCE CALCULATOR
fun calculateDistance(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {

    val earthRadius = 6371.0

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) *
            cos(Math.toRadians(lat2)) *
            sin(dLon / 2) *
            sin(dLon / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c
}