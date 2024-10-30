package com.thoriq.flog

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.thoriq.flog.data.RecentCatch

@Composable
fun RecentCatchItem(recentCatch: RecentCatch) {
    Image(painter = painterResource(id = recentCatch.image), contentDescription = "",)
}