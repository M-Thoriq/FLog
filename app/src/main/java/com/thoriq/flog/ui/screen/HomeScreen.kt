package com.thoriq.flog.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thoriq.flog.CustomItem
import com.thoriq.flog.R
import com.thoriq.flog.data.Weatheribra
import com.thoriq.flog.repository.Weatheribrabesar
import com.thoriq.flog.ui.theme.BlueF
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        GreetingSection()
        WeatherCard(
            temperature = "25°C",
            condition = "Sunny",
            location = "Jakarta",
            highTemp = "H: 30°C",
            lowTemp = "L: 20°C",
            time = "9 AM",
            icon = R.drawable.ic_launcher_background
        )
    }
}

@Composable
fun GreetingSection(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
    ) {
        Box(modifier = Modifier.padding(8.dp)){
            Image(painter = painterResource(id = R.drawable.akun), contentDescription = "Image Akun",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 15.dp)
        ) {
            Text(text = "Good Morning", fontSize = 24.sp)
            Text(text = "Have a nice day!")
        }
    }
}

@Composable
fun WeatherCard(
    modifier: Modifier = Modifier,
    temperature: String,
    condition: String,
    location: String,
    highTemp: String,
    lowTemp: String,
    time: String,
    icon: Int // Resource ID for the weather icon
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Weather Icon
            Icon(
                painter = painterResource(id = R.drawable.snow_showers),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
//                tint = Color.Gray // Adjust color as needed
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Weather Information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = location,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = condition,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // Temperature
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = temperature,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    Text(
                        text = highTemp,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = lowTemp,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 12.dp),
            thickness = 1.dp,
            color = Color.LightGray
        )
        val weatheribraRepository = Weatheribrabesar()
        val getAllData = weatheribraRepository.getAllData()
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(29.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            items(items = getAllData){Weatheribra ->
                CustomItem(weatheribra = Weatheribra)
            }
    }
}}
@Preview
@Composable
private fun HomeScreenPrev() {
    WeatherCard(
        temperature = "25°C",
        condition = "Sunny",
        location = "Jakarta",
        highTemp = "H: 30°C",
        lowTemp = "L: 20°C",
        time = "9 AM",
        icon = R.drawable.ic_launcher_background // Replace with your weather icon resource
    )
}