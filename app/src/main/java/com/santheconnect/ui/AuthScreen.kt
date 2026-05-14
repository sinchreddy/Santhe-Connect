package com.santheconnect.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AuthScreen(
    onLoginSuccess: (String) -> Unit
) {

    val context = LocalContext.current

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var isLogin by remember {
        mutableStateOf(true)
    }

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var role by remember {
        mutableStateOf("tourist")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    // 🌿 LOCAL CULTURE GRADIENT
    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF041B15),
            Color(0xFF0B3D2E),
            Color(0xFF145A32),
            Color(0xFF0A2A1F),
            Color(0xFF041B15)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.Center
    ) {

        /* ===================================================== */
        /* 🌾 CULTURAL HEADER */
        /* ===================================================== */

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(34.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0B3D2E),
                            Color(0xFF145A32),
                            Color(0xFF1B5E20),
                            Color(0xFF2E7D32)
                        )
                    )
                )
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.08f),
                    RoundedCornerShape(34.dp)
                )
                .padding(28.dp)
        ) {

            Column {

                // 🛖 TOP ICON ROW
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF2E7D32),
                                        Color(0xFF43A047),
                                        Color(0xFF66BB6A)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            "🛖",
                            fontSize = 34.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {

                        Text(
                            text = "ಸಂತೆ-ಕನೆಕ್ಟ್",
                            fontSize = 34.sp,
                            fontWeight = FontWeight.ExtraBold,
                            style = LocalTextStyle.current.copy(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFFFE082),
                                        Color(0xFFFFC107),
                                        Color(0xFFFFA000)
                                    )
                                )
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Santhe-Connect",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            style = LocalTextStyle.current.copy(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFFFF3E0),
                                        Color(0xFFFFD54F),
                                        Color(0xFFFFA000)
                                    )
                                )
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 🌱 LOCAL CULTURE TAGS
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    CultureTag("🌾 Santhe")
                    CultureTag("🍲 Local Food")
                }
                Spacer(modifier = Modifier.height(10.dp))

                // 🌱 LOCAL CULTURE TAGS
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CultureTag("🏠 Local Crafts")
                    CultureTag("🏡 Home-stays")
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text =
                        if (isLogin)
                            "Reconnect with Karnataka's vibrant local markets, authentic food traditions, handmade crafts, and hidden village experiences."
                        else
                            "Join the platform that digitally connects tourists with Karnataka's real local culture and rural communities.",
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .align(Alignment.CenterHorizontally),
                    color = Color.White.copy(alpha = 0.92f),
                    textAlign = TextAlign.Justify,
                    fontSize = 14.sp,
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        /* ===================================================== */
        /* 🔐 AUTH CARD */
        /* ===================================================== */

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0xFF101820))
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.05f),
                    RoundedCornerShape(30.dp)
                )
                .padding(24.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF43A047))
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text =
                        if (isLogin)
                            "Login to Continue"
                        else
                            "Create Your Account",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 👤 NAME FIELD
            if (!isLogin) {

                AuthField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = "Full Name",
                    placeholder = "Enter your full name"
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // 📧 EMAIL
            AuthField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = "Email Address",
                placeholder = "Enter your email"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔑 PASSWORD
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = {
                    Text("Password")
                },
                placeholder = {
                    Text(
                        "Enter password",
                        color = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation =
                    if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                trailingIcon = {

                    TextButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {

                        Text(
                            text =
                                if (passwordVisible)
                                    "Hide"
                                else
                                    "Show",
                            color = Color(0xFF66BB6A)
                        )
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color(0xFF43A047),
                    unfocusedBorderColor = Color(0xFF2A3744),
                    focusedContainerColor = Color(0xFF17212B),
                    unfocusedContainerColor = Color(0xFF17212B),
                    focusedLabelColor = Color(0xFF66BB6A),
                    unfocusedLabelColor = Color.Gray
                )
            )

            // 🎯 ROLE CHIPS
            if (!isLogin) {

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Choose Role",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    RoleChip(
                        title = "🌍 Tourist",
                        selected = role == "tourist"
                    ) {
                        role = "tourist"
                    }

                    RoleChip(
                        title = "🏪 Local",
                        selected = role == "local"
                    ) {
                        role = "local"
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ⏳ LOADING
            if (isLoading) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    CircularProgressIndicator(
                        color = Color(0xFF43A047)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // 🚀 LOGIN BUTTON
            Button(
                onClick = {

                    if (
                        email.isEmpty() ||
                        password.isEmpty()
                    ) {

                        Toast.makeText(
                            context,
                            "Please fill all fields",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    isLoading = true

                    // 🔐 LOGIN
                    if (isLogin) {

                        auth.signInWithEmailAndPassword(
                            email,
                            password
                        )
                            .addOnSuccessListener {

                                val uid =
                                    auth.currentUser?.uid!!

                                db.collection("users")
                                    .document(uid)
                                    .get()
                                    .addOnSuccessListener {

                                        isLoading = false

                                        val userRole =
                                            it.getString("role")
                                                ?: "tourist"

                                        onLoginSuccess(userRole)
                                    }
                            }
                            .addOnFailureListener {

                                isLoading = false

                                Toast.makeText(
                                    context,
                                    it.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                    } else {

                        // ✨ SIGNUP
                        auth.createUserWithEmailAndPassword(
                            email,
                            password
                        )
                            .addOnSuccessListener {

                                val uid =
                                    auth.currentUser?.uid!!

                                val user = hashMapOf(
                                    "uid" to uid,
                                    "name" to name,
                                    "email" to email,
                                    "role" to role
                                )

                                db.collection("users")
                                    .document(uid)
                                    .set(user)

                                isLoading = false

                                Toast.makeText(
                                    context,
                                    "Welcome to Santhe-Connect 🌾",
                                    Toast.LENGTH_SHORT
                                ).show()

                                isLogin = true
                            }
                            .addOnFailureListener {

                                isLoading = false

                                Toast.makeText(
                                    context,
                                    it.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(20.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF1B5E20),
                                    Color(0xFF2E7D32),
                                    Color(0xFF43A047)
                                )
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text =
                            if (isLogin)
                                "Login"
                            else
                                "Create Account",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 🔄 TOGGLE
            TextButton(
                onClick = {
                    isLogin = !isLogin
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        if (isLogin)
                            "New here? Create account"
                        else
                            "Already have an account? Login",
                    color = Color(0xFFB8C4CC),
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Experience Karnataka's local traditions, flavours & village culture 🌿",
            color = Color(0xFFB0BEC5),
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/* ===================================================== */
/* 🌿 CULTURE TAG */
/* ===================================================== */

@Composable
fun CultureTag(text: String) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(
                Color.White.copy(alpha = 0.14f)
            )
            .padding(
                horizontal = 14.dp,
                vertical = 8.dp
            )
    ) {

        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/* ===================================================== */
/* 🔤 AUTH FIELD */
/* ===================================================== */

@Composable
fun AuthField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        placeholder = {
            Text(
                placeholder,
                color = Color.Gray
            )
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White,
            focusedBorderColor = Color(0xFF43A047),
            unfocusedBorderColor = Color(0xFF2A3744),
            focusedContainerColor = Color(0xFF17212B),
            unfocusedContainerColor = Color(0xFF17212B),
            focusedLabelColor = Color(0xFF66BB6A),
            unfocusedLabelColor = Color.Gray
        )
    )
}

/* ===================================================== */
/* 🎯 ROLE CHIP */
/* ===================================================== */

@Composable
fun RoleChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {

            Text(
                title,
                fontWeight = FontWeight.SemiBold
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF2E7D32),
            selectedLabelColor = Color.White,
            containerColor = Color(0xFF17212B),
            labelColor = Color.White
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = Color.Transparent,
            selectedBorderColor = Color.Transparent
        )
    )
}