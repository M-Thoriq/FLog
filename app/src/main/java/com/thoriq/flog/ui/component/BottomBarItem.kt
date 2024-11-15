package com.thoriq.flog.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.thoriq.flog.data.BottomBarItemData

@Composable
fun BottomBarItem(
    button: BottomBarItemData,
    iconSize: Dp = 26.dp,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (button.route == ""){
            Icon(
                imageVector = button.iconUnselected,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(iconSize)
            )
        } else {
        Icon(
            imageVector = if (button.selected) button.iconSelected else button.iconUnselected,
            contentDescription = null,
            tint = if (button.selected) MaterialTheme.colorScheme.secondary else Color.Gray,
            modifier = Modifier.size(iconSize)
        )
        Text(
            text = button.route,
            style = MaterialTheme.typography.bodySmall,
            color = if (button.selected) MaterialTheme.colorScheme.secondary else Color.Gray
        )
        }
    }
}

