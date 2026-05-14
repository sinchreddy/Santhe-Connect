package com.santheconnect.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
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
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun EatLocalScreen() {

    val db = FirebaseFirestore.getInstance()

    var places by remember {
        mutableStateOf(listOf<Map<String, Any>>())
    }

    var selectedFilter by remember {
        mutableStateOf("All")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    // 🎯 FILTERS
    val filterCategories = listOf(
        "All",
        "Eatery",
        "Market",
        "Home-Stay",
        "Crafts",
        "Temple",
        "Must Visit"
    )

    // 🔄 FETCH FIRESTORE
    LaunchedEffect(Unit) {

        db.collection("eatery_locations")
            .addSnapshotListener { snapshot, _ ->

                places =
                    snapshot?.documents?.mapNotNull {
                        it.data
                    } ?: emptyList()
            }
    }

    // ✅ FILTER LOGIC
    val filteredPlaces = places.filter { place ->

        if (selectedFilter == "All") {

            true

        } else {

            val categories =
                place["categories"] as? List<*>

            categories?.contains(selectedFilter) == true
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0F14))
    ) {

        // 🌅 HERO SECTION
        item {

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
                        "🍛 FIND LOCAL",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        "Authentic Karnataka\nExperiences",
                        color = Color.White,
                        fontSize = 30.sp,
                        lineHeight = 36.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        "Discover hidden eateries, temples,\ncrafts & village gems near you.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        MiniInfoCard(
                            emoji = "📍",
                            value = "${filteredPlaces.size}",
                            label = "Places"
                        )

                        MiniInfoCard(
                            emoji = "✨",
                            value = selectedFilter,
                            label = "Filter"
                        )
                    }
                }
            }
        }

        // 🎯 FILTER SECTION
        item {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        "Nearby Places",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        "Filter: $selectedFilter",
                        color = Color(0xFFFFA726),
                        fontSize = 12.sp
                    )
                }

                Box {

                    IconButton(
                        onClick = {
                            expanded = true
                        }
                    ) {

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF161B22))
                                .padding(10.dp)
                        ) {

                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter",
                                tint = Color(0xFFFFA726)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                        modifier = Modifier
                            .background(Color(0xFF161B22))
                    ) {

                        filterCategories.forEach { category ->

                            DropdownMenuItem(
                                text = {

                                    Text(
                                        text = category,
                                        color =
                                            if (selectedFilter == category)
                                                Color(0xFFFFA726)
                                            else
                                                Color.White
                                    )
                                },
                                onClick = {

                                    selectedFilter = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(18.dp))
        }

        // 📋 PLACES LIST
        items(filteredPlaces) { place ->

            PlaceCard(place)

            Spacer(Modifier.height(14.dp))
        }

        // ❌ EMPTY STATE
        if (filteredPlaces.isEmpty()) {

            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFF161B22))
                        .padding(30.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            "😔",
                            fontSize = 40.sp
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            "No Places Found",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            "Try selecting another category",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
fun PlaceCard(
    place: Map<String, Any>
) {

    val context = LocalContext.current

    val latitude = place["latitude"] as? Double
    val longitude = place["longitude"] as? Double

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xFF161B22))
            .clickable {

                if (latitude != null && longitude != null) {

                    val uri = Uri.parse(
                        "geo:$latitude,$longitude?q=$latitude,$longitude"
                    )

                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        uri
                    )

                    context.startActivity(intent)
                }
            }
            .padding(20.dp)
    ) {

        // 🏷 TITLE + RATING
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    place["name"].toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    place["specialty"].toString(),
                    color = Color(0xFFB0BEC5),
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFFA726).copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFA726),
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        "4.8",
                        color = Color(0xFFFFA726),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // 🏷 CATEGORIES
        val categories =
            place["categories"] as? List<*>

        if (!categories.isNullOrEmpty()) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                categories.forEach { cat ->

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Color(0xFFFFA726).copy(alpha = 0.12f)
                            )
                            .padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            )
                    ) {

                        Text(
                            text = cat.toString(),
                            color = Color(0xFFFFA726),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }

        // 📍 LOCATION CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF0B0F14))
                .padding(14.dp)
        ) {

            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFFFF7043),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(Modifier.width(6.dp))

                    Text(
                        place["village"].toString(),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    "📅 ${place["openDays"]}",
                    color = Color(0xFF81C784),
                    fontSize = 12.sp
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    "🗺 Tap to open in Google Maps",
                    color = Color(0xFF64B5F6),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun MiniInfoCard(
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