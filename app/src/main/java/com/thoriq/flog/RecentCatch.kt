package com.thoriq.flog

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.thoriq.flog.R
import com.thoriq.flog.data.Fish

@Composable
fun RecentCatchItem(Fish: Fish) {
    Box(contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .width(256.dp)
            .padding(7.5.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        if (Fish.image != null) {
            val imageBitmap = BitmapFactory.decodeByteArray(Fish.image, 0, Fish.image!!.size).asImageBitmap()
            Image(
                bitmap = imageBitmap,
                contentDescription = "Fish Image",
                modifier = Modifier
                    .size(240.dp)
                    .clip(RectangleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_flog),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
//        Icon(painter = painterResource(Fish.image), contentDescription = "", tint = MaterialTheme.colorScheme.primary)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.5f)
                    )
                ))
                .padding(8.dp)
            ) {
                Text(text = Fish.nama!!, style = MaterialTheme.typography.titleLarge, color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
                Text(text = Fish.createdAt, style = MaterialTheme.typography.labelMedium, color = Color.White)

            }


    }
}