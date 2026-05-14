package com.santheconnect.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Photo
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
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.santheconnect.util.AudioRecorder
import java.io.File

// 📸 IMAGE UPLOAD
fun uploadImage(
    uri: Uri,
    onResult: (String) -> Unit
) {

    val storageRef = FirebaseStorage.getInstance()
        .reference
        .child("reviews/${System.currentTimeMillis()}.jpg")

    storageRef.putFile(uri)
        .continueWithTask {
            storageRef.downloadUrl
        }
        .addOnSuccessListener { downloadUri ->

            onResult(downloadUri.toString())
        }
}

// 🎤 AUDIO UPLOAD
fun uploadAudio(
    path: String,
    onResult: (String) -> Unit
) {

    val fileUri = Uri.fromFile(File(path))

    val storageRef = FirebaseStorage.getInstance()
        .reference
        .child("reviews/audio_${System.currentTimeMillis()}.3gp")

    storageRef.putFile(fileUri)
        .continueWithTask {
            storageRef.downloadUrl
        }
        .addOnSuccessListener { downloadUri ->

            onResult(downloadUri.toString())
        }
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

            if (location != null) {

                onResult(
                    LatLng(
                        location.latitude,
                        location.longitude
                    )
                )

            } else {

                Toast.makeText(
                    context,
                    "Please turn ON GPS",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}

@Composable
fun ReviewScreen() {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    val recorder = remember {
        AudioRecorder()
    }

    val audioPath =
        context.filesDir.absolutePath + "/audio.3gp"

    var reviews by remember {
        mutableStateOf(listOf<Map<String, Any>>())
    }

    var reviewText by remember {
        mutableStateOf("")
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var isRecording by remember {
        mutableStateOf(false)
    }

    var hasRecordedAudio by remember {
        mutableStateOf(false)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var userLocation by remember {
        mutableStateOf<LatLng?>(null)
    }

    var locationAdded by remember {
        mutableStateOf(false)
    }

    // 📍 LOCATION PERMISSION
    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                getCurrentLocation(context) { location ->

                    userLocation = location
                    locationAdded = true
                }
            }
        }

    // 📸 IMAGE PICKER
    val imageLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            selectedImageUri = uri
        }

    // 🔄 FETCH REVIEWS
    LaunchedEffect(Unit) {

        db.collection("reviews")
            .addSnapshotListener { snapshot, _ ->

                reviews =
                    snapshot?.documents?.mapNotNull { doc ->

                        doc.data?.apply {
                            put("docId", doc.id)
                        }
                    } ?: emptyList()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0F14))
            .verticalScroll(rememberScrollState())
    ) {

        // 🌟 HERO SECTION
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
                    "🌟 COMMUNITY WALL",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    "Share Your\nSanthe Experience",
                    color = Color.White,
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    "Upload photos, voice notes & travel experiences from Karnataka.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }

        // ✍️ CREATE REVIEW CARD
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF161B22))
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.05f),
                    RoundedCornerShape(24.dp)
                )
                .padding(18.dp)
        ) {

            Text(
                "✍️ Add Review",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = reviewText,
                onValueChange = {
                    reviewText = it
                },
                placeholder = {
                    Text(
                        "Share your experience...",
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color(0xFFFFA726),
                    unfocusedBorderColor = Color.Gray
                ),
                shape = RoundedCornerShape(18.dp)
            )

            Spacer(Modifier.height(16.dp))

            // ACTION BUTTONS
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ActionButton(
                    modifier = Modifier.weight(1f),
                    icon = "📸",
                    text = "Photo",
                    bg = Color(0xFF1B4332)
                ) {

                    imageLauncher.launch("image/*")
                }

                ActionButton(
                    modifier = Modifier.weight(1f),
                    icon = if (isRecording) "⏹" else "🎤",
                    text =
                        if (isRecording)
                            "Stop"
                        else
                            "Voice",
                    bg =
                        if (isRecording)
                            Color.Red
                        else
                            Color(0xFF003049)
                ) {

                    if (!isRecording) {

                        recorder.start(audioPath)
                        isRecording = true

                    } else {

                        recorder.stop()
                        isRecording = false
                        hasRecordedAudio = true
                    }
                }

                ActionButton(
                    modifier = Modifier.weight(1f),
                    icon = "📍",
                    text = "Location",
                    bg = Color(0xFF5F0F40)
                ) {

                    when {

                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED -> {

                            getCurrentLocation(context) { location ->

                                userLocation = location
                                locationAdded = true
                            }
                        }

                        else -> {

                            locationPermissionLauncher.launch(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        }
                    }
                }
            }

            // STATUS
            if (locationAdded || hasRecordedAudio) {

                Spacer(Modifier.height(12.dp))

                Column {

                    if (locationAdded) {

                        Text(
                            "📍 Current location added",
                            color = Color(0xFF81C784),
                            fontSize = 12.sp
                        )
                    }

                    if (hasRecordedAudio) {

                        Spacer(Modifier.height(4.dp))

                        Text(
                            "🎤 Voice note recorded",
                            color = Color(0xFF64B5F6),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // IMAGE PREVIEW
            selectedImageUri?.let {

                Spacer(Modifier.height(14.dp))

                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clip(RoundedCornerShape(18.dp))
                )
            }

            Spacer(Modifier.height(18.dp))

            if (isLoading) {

                CircularProgressIndicator(
                    color = Color(0xFFFFA726)
                )

                Spacer(Modifier.height(12.dp))
            }

            Button(
                onClick = {

                    if (reviewText.isEmpty()) return@Button

                    isLoading = true

                    fun saveReview(
                        imageUrl: String? = null,
                        audioUrl: String? = null
                    ) {

                        val review = hashMapOf(
                            "name" to "Traveler",
                            "text" to reviewText,
                            "imageUrl" to imageUrl,
                            "audioUrl" to audioUrl,
                            "latitude" to userLocation?.latitude,
                            "longitude" to userLocation?.longitude,
                            "timestamp" to System.currentTimeMillis()
                        )

                        db.collection("reviews")
                            .add(review)

                        reviewText = ""
                        selectedImageUri = null
                        hasRecordedAudio = false
                        locationAdded = false
                        userLocation = null
                        isLoading = false
                    }

                    if (selectedImageUri != null) {

                        uploadImage(selectedImageUri!!) { imageUrl ->

                            if (hasRecordedAudio) {

                                uploadAudio(audioPath) { audioUrl ->

                                    saveReview(
                                        imageUrl,
                                        audioUrl
                                    )
                                }

                            } else {

                                saveReview(
                                    imageUrl,
                                    null
                                )
                            }
                        }

                    } else if (hasRecordedAudio) {

                        uploadAudio(audioPath) { audioUrl ->

                            saveReview(
                                null,
                                audioUrl
                            )
                        }

                    } else {

                        saveReview(null, null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA726)
                )
            ) {

                Text(
                    "Post Review",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(Modifier.height(22.dp))

        // 📋 REVIEWS
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {

            Text(
                "🌍 Traveler Stories",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            reviews.forEach { review ->

                ReviewCard(
                    review = review,
                    context = context,
                    db = db
                )

                Spacer(Modifier.height(14.dp))
            }
        }

        Spacer(Modifier.height(30.dp))
    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    icon: String,
    text: String,
    bg: Color,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(bg)
            .clickable {
                onClick()
            }
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            icon,
            fontSize = 24.sp
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

@Composable
fun ReviewCard(
    review: Map<String, Any>,
    context: Context,
    db: FirebaseFirestore
) {

    var isPlaying by remember {
        mutableStateOf(false)
    }

    var mediaPlayer by remember {
        mutableStateOf<MediaPlayer?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF161B22))
            .border(
                1.dp,
                Color.White.copy(alpha = 0.05f),
                RoundedCornerShape(22.dp)
            )
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFA726)),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    "👤",
                    fontSize = 22.sp
                )
            }

            Spacer(Modifier.width(12.dp))

            Column {

                Text(
                    review["name"].toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Text(
                    "Local Traveler",
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Text(
            review["text"].toString(),
            color = Color.White,
            fontSize = 14.sp,
            lineHeight = 22.sp
        )

        // IMAGE
        review["imageUrl"]?.let {

            Spacer(Modifier.height(14.dp))

            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(18.dp))
            )
        }

        // LOCATION
        val lat = review["latitude"] as? Double
        val lng = review["longitude"] as? Double

        if (lat != null && lng != null) {

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.clickable {

                    val uri =
                        "geo:$lat,$lng?q=$lat,$lng".toUri()

                    val intent =
                        Intent(
                            Intent.ACTION_VIEW,
                            uri
                        )

                    context.startActivity(intent)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFFFA726)
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    "View Shared Location",
                    color = Color(0xFFFFA726),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }

        // AUDIO
        review["audioUrl"]?.let { audioUrl ->

            Spacer(Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Button(
                    onClick = {

                        if (mediaPlayer == null) {

                            val player = MediaPlayer()

                            player.setDataSource(audioUrl.toString())

                            player.setOnPreparedListener {
                                it.start()
                                isPlaying = true
                            }

                            player.setOnCompletionListener {
                                isPlaying = false
                                mediaPlayer = null
                            }

                            player.prepareAsync()

                            mediaPlayer = player
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF003049)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Text(
                        if (isPlaying)
                            "Playing..."
                        else
                            "▶ Play Voice"
                    )
                }

                if (isPlaying) {

                    Button(
                        onClick = {

                            mediaPlayer?.stop()
                            mediaPlayer?.release()

                            mediaPlayer = null
                            isPlaying = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Text("Stop")
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {

                val docId =
                    review["docId"].toString()

                db.collection("reviews")
                    .document(docId)
                    .delete()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8B0000)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Color.White
            )

            Spacer(Modifier.width(6.dp))

            Text(
                "Delete",
                color = Color.White
            )
        }
    }
}