package com.thoriq.flog.data

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomBarItemData(
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector,
    val route: String,
    val selected: Boolean,
    val onClick: () -> Unit
)