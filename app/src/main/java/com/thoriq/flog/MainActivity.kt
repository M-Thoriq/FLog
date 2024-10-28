package com.thoriq.flog

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.thoriq.flog.data.Fish
import com.thoriq.flog.data.Weather
import com.thoriq.flog.repository.WeatherRepository
import com.thoriq.flog.ui.screen.AccountScreen
import com.thoriq.flog.ui.screen.HomeScreen
import com.thoriq.flog.ui.screen.CameraScreen
import com.thoriq.flog.ui.screen.FishScreen
import com.thoriq.flog.ui.screen.MapsScreen
import com.thoriq.flog.ui.theme.FLogTheme
import com.thoriq.flog.viewModel.FishViewModel
import com.thoriq.flog.viewModel.factory.ViewModelFactory

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null,
)

class MainActivity : ComponentActivity() {

    private val weatherRepository = WeatherRepository(this)
    private var currentScreen by mutableStateOf("Home")
    private lateinit var fishViewModel: FishViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var hasLocationPermission by remember { mutableStateOf(false) }
            val context = LocalContext.current

            val requestPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                hasLocationPermission = isGranted
            }

            val weathers = remember { mutableStateOf<List<Weather>>(emptyList()) }

            LaunchedEffect(Unit) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                } else {
                    hasLocationPermission = true
                }
                weatherRepository.fetchWeatherData(context) { weatherList ->
                    weathers.value = weatherList
                }
            }

            val homeTab = TabBarItem(
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            )
            val fishesTab = TabBarItem(
                title = "Fishes",
                selectedIcon = Icons.Filled.Build,
                unselectedIcon = Icons.Outlined.Build
            )
            val cameraTab = TabBarItem(
                title = "Camera",
                selectedIcon = Icons.Filled.Notifications,
                unselectedIcon = Icons.Outlined.Notifications
            )
            val mapsTab = TabBarItem(
                title = "Maps",
                selectedIcon = Icons.Filled.LocationOn,
                unselectedIcon = Icons.Outlined.LocationOn
            )
            val accountTab = TabBarItem(
                title = "Account",
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person
            )

            // creating a list of all the tabs
            val tabBarItems = listOf(homeTab, fishesTab, cameraTab, mapsTab, accountTab)

            // creating our navController
            val navController = rememberNavController()
            val sheetState = rememberModalBottomSheetState()
            var isSheetOpen by remember { mutableStateOf(false) }


            fishViewModel =
                ViewModelProvider(this, ViewModelFactory.getInstance(this.application)).get(
                    FishViewModel::class.java
                )

            if (isSheetOpen) {
                ModalBottomSheet(
                    sheetState = sheetState, onDismissRequest = { isSheetOpen = false }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = "Add Fish", style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Add EditText for fish name
                        var text by remember { mutableStateOf(TextFieldValue("")) }
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = text,
                            onValueChange = {
                                text = it
                            },
                            label = { Text(text = "Your Label") },
                            placeholder = { Text(text = "Your Placeholder/Hint") },
                        )
                        Button(onClick = {
                            val fish = Fish(
                                nama = text.text,
                                berat = 2.5, // Replace with actual value
                                harga = 20000.0, // Replace with actual value
                                createdAt = ""
                            )
                            fishViewModel.insertFish(fish)
                            isSheetOpen = false
                        }) {
                            Text("Add Fish")
                        }
                    }
                }
            }

            if (hasLocationPermission) {
                FLogTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Scaffold(
                            bottomBar = {
                                TabView(
                                    tabBarItems,
                                    navController,
                                    currentScreen, // Pass currentScreen as parameter
                                    { newScreen ->
                                        currentScreen = newScreen
                                    } // Pass update function
                                )
                            }, floatingActionButton = {
                                if (currentScreen == fishesTab.title) {
                                    FloatingActionButton(onClick = {
                                        isSheetOpen = true
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Add,
                                            contentDescription = "Add"
                                        )
                                    }
                                }
                            },
                            floatingActionButtonPosition = FabPosition.End
                        ) { paddingValues ->
                            NavHost(
                                navController = navController,
                                startDestination = homeTab.title
                            ) {
                                composable(homeTab.title) {
                                    HomeScreen()
                                }
                                composable(fishesTab.title) {
                                    val fishes = fishViewModel.getAllFish()
                                        .collectAsState(initial = emptyList()).value
                                    FishScreen(
                                        modifier = Modifier.padding(paddingValues),
                                        paddingValues = PaddingValues(0.dp),
                                        fishes = fishes,
                                        fishViewModel = fishViewModel
                                    )
                                }
                                composable(cameraTab.title) {
                                    CameraScreen(
                                        question = "What is this fish?",
                                        onImageIdentified = { identifiedText ->
                                            println("Identified Image: $identifiedText")
                                        }
                                    )
//                                    LoginScreen()
//                                TODO\("Tambahin Camera\(\)")
                                }
                                composable(mapsTab.title) {
                                    MapsScreen(
                                        modifier = Modifier.padding(paddingValues),
                                        paddingValues = PaddingValues(0.dp),
                                        latitude = WeatherRepository.latitude, // Your latitude
                                        longitude = WeatherRepository.longitude // Your longitude
                                    )
                                }
                                composable(accountTab.title) {
                                    AccountScreen()
                                }
                            }
                        }
                    }
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("Location permission is required")
                }
            }
        }
    }
}

@Composable
fun TabView(
    tabBarItems: List<TabBarItem>,
    navController: NavController,
    currentScreen: String, // Receive currentScreen as parameter
    updateCurrentScreen: (String) -> Unit // Receive update function
) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    // Update selectedTabIndex based on currentDestination
    LaunchedEffect(key1 = currentDestination) {
        tabBarItems.forEachIndexed { index, item ->
            if (item.title == currentDestination?.route) {
                selectedTabIndex = index
            }
        }
    }

    NavigationBar {
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    updateCurrentScreen(tabBarItem.title)
                    // Update currentScreen
                    navController.navigate(tabBarItem.title) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                icon = {
                    TabBarIconView(
                        selectedTabIndex == index,
                        tabBarItem.selectedIcon,
                        tabBarItem.unselectedIcon,
                        tabBarItem.title,
                        tabBarItem.badgeAmount
                    )
                },
                label = { Text(tabBarItem.title) }
            )
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
    badgeAmount: Int? = null,
) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            imageVector = if (isSelected) {
                selectedIcon
            } else {
                unselectedIcon
            },
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
