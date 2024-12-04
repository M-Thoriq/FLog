package com.thoriq.flog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.thoriq.flog.data.RecentCatch

@Composable
fun RecentCatchItem(recentCatch: RecentCatch) {
    Box(contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .width(256.dp)
            .padding(7.5.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_flog), contentDescription = "", tint = MaterialTheme.colorScheme.primary)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = Brush.verticalGradient(
                    colors = listOf(
                         // Start color (semi-transparent black)
                        Color.Transparent, // End color (transparent)
                        Color.Black.copy(alpha = 0.5f)
                    )
                ))
                .padding(8.dp)
            ) {
                Text(text = "Salmon", style = MaterialTheme.typography.titleLarge, color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
                Text(text = "1 DAYS AGO", style = MaterialTheme.typography.labelMedium, color = Color.White)

            }


    }
}