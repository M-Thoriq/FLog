package com.thoriq.flog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.thoriq.flog.data.Weather
import com.thoriq.flog.data.WeatherPreview

//@Composable
//fun WeatherPreviewItem(weatherPreview: Weather) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth(),
//        verticalArrangement = Arrangement.SpaceBetween,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = weatherPreview.Time,
//            style = MaterialTheme.typography.bodyMedium,
//            color = Color.Gray,
//            modifier = Modifier.padding(bottom = 4.dp),
//        )
//        Icon(painter = painterResource(id = weatherPreview.image), contentDescription = "",
//            modifier = Modifier
//                .size(42.dp)
//                .clip(CircleShape)
//        )
//        Text(text = weatherPreview.Temp,
//            style = MaterialTheme.typography.labelLarge,
//        )
//
//    }
//}
