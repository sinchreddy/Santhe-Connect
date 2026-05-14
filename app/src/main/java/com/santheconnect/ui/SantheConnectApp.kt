package com.santheconnect.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santheconnect.data.SampleData
import com.santheconnect.ui.screens.*
import com.santheconnect.ui.theme.SantheColors
import java.util.*

sealed class AppTab(val route: String, val label: String, val emoji: String) {
    object Home : AppTab("home", "Home", "🏠")
    object Map : AppTab("map", "Santhe Map", "🗺️")
    object Eat : AppTab("eat", "Eat Local", "🍽️")
    object Add : AppTab("add", "Add", "➕")
    object Review : AppTab("review", "Review", "📝")
}

val ALL_TABS = listOf(
    AppTab.Home,
    AppTab.Map,
    AppTab.Eat,
    AppTab.Review,
    AppTab.Add
)

@Composable
fun SantheConnectApp() {

    var userRole by remember {
        mutableStateOf<String?>(null)
    }

    // 🔐 AUTH SCREEN
    if (userRole == null) {

        AuthScreen { role ->
            userRole = role
        }

        return
    }

    // 📅 TODAY
    val todayIndex =
        Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1

    val today =
        SampleData.DAYS[todayIndex]

    // 📌 TAB STATE
    var currentTab by remember {
        mutableStateOf<AppTab>(AppTab.Home)
    }

    val activeSanthesToday =
        SampleData.SANTHES.count {
            it.day == today
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SantheColors.Cream)
    ) {

        // 🛖 FIXED TOP HEADER ONLY
        TopBrandHeader(
            onLogout = {
                userRole = null
            }
        )

        // 🔽 EVERYTHING BELOW MOVES / SCROLLS
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            // 🚀 TAB BAR
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SantheColors.Charcoal)
            ) {

                TabBar(
                    currentTab = currentTab,
                    onTabSelected = {
                        currentTab = it
                    }
                )
            }

            // 📱 SCREEN CONTENT
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                when (currentTab) {

                    AppTab.Home ->
                        HomeScreen(
                            today = today,
                            onNavigate = {
                                currentTab = it
                            }
                        )

                    AppTab.Map ->
                        SantheMapReal()

                    AppTab.Eat ->
                        EatLocalScreen()

                    AppTab.Add -> {

                        if (userRole == "local") {

                            AddLocationScreen()

                        } else {

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF0B0F14)),
                                contentAlignment = Alignment.Center
                            ) {

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .padding(24.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(Color(0xFF161B22))
                                        .padding(24.dp)
                                ) {

                                    Text(
                                        "🔒",
                                        fontSize = 40.sp
                                    )

                                    Spacer(Modifier.height(14.dp))

                                    Text(
                                        "Access Restricted",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        "Only local vendors can add new locations.",
                                        color = Color.LightGray,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    AppTab.Review ->
                        ReviewScreen()
                }
            }
        }
    }
}

/* ===================================================== */
/* 🛖 FIXED TOP BRAND HEADER */
/* ===================================================== */

@Composable
fun TopBrandHeader(
    onLogout: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0B3D2E),
                        Color(0xFF145A32),
                        Color(0xFF1E8449)
                    )
                )
            )
            .padding(
                top = 30.dp,
                start = 15.dp,
                end = 10.dp,
                bottom = 10.dp
            )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Color.White.copy(alpha = 0.14f)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        "🛖",
                        fontSize = 30.sp
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {

                    Text(
                        text = "ಸಂತೆ-ಕನೆಕ್ಟ್",
                        fontSize = 30.sp,
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

                    Spacer(modifier = Modifier.height(1.dp))

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

            // 🚪 LOGOUT
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.14f))
            ) {

                TextButton(
                    onClick = {
                        onLogout()
                    }
                ) {

                    Text(
                        "🚪Logout",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

/* ===================================================== */
/* 🚀 TAB BAR */
/* ===================================================== */

@Composable
fun TabBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF161B22))
            .padding(vertical = 6.dp)
    ) {

        ALL_TABS.forEach { tab ->

            val isActive = currentTab == tab

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (isActive)
                            SantheColors.Saffron
                        else
                            Color.Transparent
                    )
            ) {

                TextButton(
                    onClick = {
                        onTabSelected(tab)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            tab.emoji,
                            fontSize = 20.sp
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = tab.label,
                            color =
                                if (isActive)
                                    Color.White
                                else
                                    Color.White.copy(alpha = 0.55f),
                            fontSize = 10.sp,
                            fontWeight =
                                if (isActive)
                                    FontWeight.Bold
                                else
                                    FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            lineHeight = 12.sp
                        )
                    }
                }
            }
        }
    }
}