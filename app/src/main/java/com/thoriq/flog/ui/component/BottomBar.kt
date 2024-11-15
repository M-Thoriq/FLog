package com.thoriq.flog.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.thoriq.flog.data.BottomBarItemData


@Composable
fun BottomBar(
    barHeight: Dp = 72.dp,
    fabColor: Color = MaterialTheme.colorScheme.secondary,
    fabSize: Dp = 64.dp,
    fabIconSize: Dp = 32.dp,
    cardTopCornerSize: Dp = 0.dp,
    cardElevation: Dp = 8.dp,
    fabDrawableId: ImageVector = Icons.Default.PhotoCamera,
    fabRoute: String,
    fabOnClick: () -> Unit = {},
    buttons: List<BottomBarItemData>
) {
    require(buttons.size == 5) { "BottomBar must have exactly 4 buttons" }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(barHeight + fabSize / 2)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .align(Alignment.BottomCenter),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
            shape = RoundedCornerShape(
                topStart = cardTopCornerSize,
                topEnd = cardTopCornerSize,
                bottomEnd = 0.dp,
                bottomStart = 0.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(start = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                buttons.forEach { button ->
                    BottomBarItem(
                        button = button,
                        onClick = button.onClick
                    )
                }
            }
        }
        LargeFloatingActionButton(
            modifier = Modifier
                .size(fabSize)
                .align(Alignment.TopCenter),
            onClick = {
                fabOnClick()
            },
            shape = CircleShape,
            containerColor = fabColor,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(defaultElevation = 0.dp)
        ) {
            Icon(
                imageVector = fabDrawableId,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(fabIconSize)
            )
        }
    }
}

