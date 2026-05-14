package com.santheconnect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santheconnect.data.Santhe
import com.santheconnect.ui.theme.SantheColors

fun typeColor(type: String): Color = when (type.lowercase()) {
    "market" -> SantheColors.Leaf
    "craft"  -> SantheColors.Earth
    else     -> SantheColors.Saffron
}

@Composable
fun SantheCard(santhe: Santhe) {
    val color = typeColor(santhe.type)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            // Left accent border via inner box trick
            .padding(start = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Accent bar
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(90.dp)
                .background(color)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Emoji icon
        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(santhe.emoji, fontSize = 22.sp)
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Info
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(
                text = santhe.name,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = SantheColors.Charcoal
            )
            Text(
                text = "📍 ${santhe.lat} • ${santhe.lng} away",
                fontSize = 11.sp,
                color = SantheColors.LightGrey,
                modifier = Modifier.padding(top = 2.dp)
            )
            Text(
                text = "\"${santhe.specialty}\"",
                fontSize = 11.sp,
                color = SantheColors.Earth,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 3.dp)
            )
            Row(modifier = Modifier.padding(top = 6.dp)) {
                Chip(label = santhe.type.replaceFirstChar { it.uppercase() }, bgColor = color.copy(alpha = 0.15f), textColor = color)
                Spacer(modifier = Modifier.width(6.dp))
                Chip(label = "📅 ${santhe.day}", bgColor = Color(0xFFFFF3E0), textColor = SantheColors.Saffron)
            }
        }
    }
}

@Composable
fun Chip(label: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(label, fontSize = 10.sp, color = textColor, fontWeight = FontWeight.SemiBold)
    }
}
