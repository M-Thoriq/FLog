package com.thoriq.flog

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thoriq.flog.data.Weather
import com.thoriq.flog.repository.WeatherRepository
import com.thoriq.flog.ui.component.WeathersCard
import com.thoriq.flog.ui.screen.HomeScreen
import com.thoriq.flog.ui.screen.CameraScreen
import com.thoriq.flog.ui.theme.FLogTheme

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

class MainActivity : ComponentActivity() {

    private val weatherRepository = WeatherRepository(this)

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val weathers = remember { mutableStateOf<List<Weather>>(emptyList()) }
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                weatherRepository.fetchWeatherData(context) { weatherList ->
                    weathers.value = weatherList
                }
            }

            val homeTab = TabBarItem(title = "Home", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home)
            val fishesTab = TabBarItem(title = "Alerts", selectedIcon = Icons.Filled.Notifications, unselectedIcon = Icons.Outlined.Notifications)
            val cameraTab = TabBarItem(title = "Camera", selectedIcon = Icons.Filled.Notifications, unselectedIcon = Icons.Outlined.Notifications)
            val settingsTab = TabBarItem(title = "Settings", selectedIcon = Icons.Filled.Settings, unselectedIcon = Icons.Outlined.Settings)
            val accountTab = TabBarItem(title = "More", selectedIcon = Icons.Filled.List, unselectedIcon = Icons.Outlined.List)
            
            // creating a list of all the tabs
            val tabBarItems = listOf(homeTab, fishesTab,cameraTab, settingsTab, accountTab)

            // creating our navController
            val navController = rememberNavController()

            FLogTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(bottomBar = { TabView(tabBarItems, navController) }) {
                        NavHost(navController = navController, startDestination = homeTab.title) {
                            composable(homeTab.title) {
                                HomeScreen()
                            }
                            composable(fishesTab.title) {
                                Text(fishesTab.title)
//                                TODO("Tambahin Alert()")
                            }
                            composable(cameraTab.title) {
                                CameraScreen(
                                    question = "What is this fish?",
                                    onImageIdentified = { identifiedText ->
                                        // Handle the identified text
                                        println("Identified Image: $identifiedText")
                                    }
                                )
//                                TODO("Tambahin Camera()")
                            }
                            composable(settingsTab.title) {
                                Text(settingsTab.title)
//                                TODO("Tambahin HomeScreen()")
                            }
                            composable(accountTab.title) {
                                MoreView()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabView(tabBarItems: List<TabBarItem>, navController: NavController) {
    var selectedTabIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar {
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(tabBarItem.title)
                },
                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = {Text(tabBarItem.title)})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            imageVector = if (isSelected) {selectedIcon} else {unselectedIcon},
            contentDescription = title
        )
    }
}

@Composable
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}

@Composable
fun MoreView() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Thing 1")
        Text("Thing 2")
        Text("Thing 3")
        Text("Thing 4")
        Text("Thing 5")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FLogTheme {
        HomeScreen()
    }
}
