package com.thoriq.flog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.thoriq.flog.data.RecentCatch

@Composable
fun RecentCatchItem(recentCatch: RecentCatch) {
    Box(modifier = Modifier
        .padding(7.5.dp)
        .aspectRatio(1f)
        .clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colorScheme.secondary)
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_flog), contentDescription = "", tint = MaterialTheme.colorScheme.onSecondary)
    }
}