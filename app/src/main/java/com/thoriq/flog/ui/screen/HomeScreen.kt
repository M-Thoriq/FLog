package com.thoriq.flog.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
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
import com.thoriq.flog.R
import com.thoriq.flog.ui.theme.BlueF

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
        WeatherCard()
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
fun WeatherCard(modifier: Modifier = Modifier) {
    val degree = "23" + "°C"
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(BlueF)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 24.dp)
        ) {
    //        TODO("BRA NANTI INI IMAGENYA BUAT KONDISIONAL GITU JADI KALO HUJANNYA DERAS BEDA FOTO ETC")
            Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "Gaada")
            Text(degree, fontSize = 48.sp, color = Color.White, fontWeight = FontWeight.Medium)
        }

        }
}

@Preview
@Composable
private fun HomeScreenPrev() {
    WeatherCard()
}