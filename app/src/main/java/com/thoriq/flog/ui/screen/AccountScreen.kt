package com.thoriq.flog.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thoriq.flog.R

@Composable
fun AccountScreen(name:String,modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(7f)
            ) {
                Image(
                    painterResource(R.drawable.pngtree_clown_fish_marine_fish_decoration_png_image_4278349),
                    contentDescription = "Back",
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
            Column(
                Modifier.weight(10f)
            ) {
                Row {
                    Text(
                        name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
                Row {
                    Text(
                        name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }


        }
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
            modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        tint = Color.Red,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Column(
                    Modifier.weight(5f)
                ) {
                    Text(
                        "Favorite",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "More",
                        tint = Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Favorite",
                        tint = Color.Yellow,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Column(
                    Modifier.weight(5f)
                ) {
                    Text(
                        "Notifications",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "More",
                        tint = Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                HorizontalDivider()
            }
            Row(
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorite",
                        tint = Color.Yellow,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Column(
                    Modifier.weight(5f)
                ) {
                    Text(
                        "Statistic",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "More",
                        tint = Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Favorite",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Column(
                    Modifier.weight(5f)
                ) {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "More",
                        tint = Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.padding(bottom = 48.dp)
            ) {
                HorizontalDivider()
            }
            Row(
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Favorite",
                        tint = Color.Red,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Column(
                    Modifier.weight(6f)
                ) {
                    Text(
                        "Log Out",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                }

            }
        }
    }
}