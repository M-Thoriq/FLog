package com.thoriq.flog

import RegisterScreen
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.loopj.android.http.AsyncHttpClient.log
import com.thoriq.flog.data.BottomBarItemData
import com.thoriq.flog.data.Fish
import com.thoriq.flog.data.Screen
import com.thoriq.flog.data.Weather
import com.thoriq.flog.repository.WeatherRepository
import com.thoriq.flog.ui.component.BottomBar
import com.thoriq.flog.ui.screen.AccountScreen
import com.thoriq.flog.ui.screen.CameraScreen
import com.thoriq.flog.ui.screen.FishScreen
import com.thoriq.flog.ui.screen.HomeScreen
import com.thoriq.flog.ui.screen.LoginScreen
import com.thoriq.flog.ui.screen.MapsScreen
import com.thoriq.flog.ui.theme.FlogTheme
import com.thoriq.flog.viewModel.FishViewModel
import com.thoriq.flog.viewModel.WeatherViewModel
import com.thoriq.flog.viewModel.factory.ViewModelFactory



class MainActivity : ComponentActivity() {

    private val weatherRepository = WeatherRepository(this)
    private var currentScreen by mutableStateOf("Home")
    val fishdata =
        "[{'title':'Salmon','snippet':'Fish 1','latitude':3.885,'longitude':98.6656},{'title':'salmon','snippet':'Fish 2','latitude':3.5839,'longitude':98.67},{'title':'ikan salmon','snippet':'Fish 2','latitude':3.5877,'longitude':98.66},{'title':'hiu','snippet':'Fish 2','latitude':3.5739,'longitude':98.57},{'title':'hiu','snippet':'Fish 2','latitude':3.5849,'longitude':98.77}]"
    private lateinit var fishViewModel: FishViewModel
    private lateinit var weatherViewModel: WeatherViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            var hasLocationPermission by remember { mutableStateOf(false) }
            var login by remember { mutableStateOf(false) }
            val context = LocalContext.current
            var name by remember { mutableStateOf("") }

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
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val bottomBarItems = listOf(
                    BottomBarItemData(
                        iconSelected = Icons.Default.Home,
                        iconUnselected = Icons.Outlined.Home,
                        route = Screen.Home.route,
                        selected = currentRoute == Screen.Home.route,
                        onClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    ),
                    BottomBarItemData(
                        iconSelected = Icons.Default.Explore,
                        iconUnselected = Icons.Outlined.Explore,
                        route = Screen.Fishes.route,
                        selected = currentRoute == Screen.Fishes.route,
                        onClick = {
                            navController.navigate(Screen.Fishes.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    ),
                    BottomBarItemData(
                        iconSelected = Icons.Default.Home,
                        iconUnselected = Icons.Outlined.Home,
                        route = "",
                        selected = false,
                        onClick = {}
                    ),
                    BottomBarItemData(
                        iconSelected = Icons.Default.Map,
                        iconUnselected = Icons.Outlined.Map,
                        route = Screen.Maps.route,
                        selected = currentRoute == Screen.Maps.route,
                        onClick = {
                            navController.navigate(Screen.Maps.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    ),
                    BottomBarItemData(
                        iconSelected = Icons.Default.Person,
                        iconUnselected = Icons.Outlined.Person,
                        route = Screen.Account.route,
                        selected = currentRoute == Screen.Account.route,
                        onClick = {
                            navController.navigate(Screen.Account.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
            )

            // creating our navController

            val sheetState = rememberModalBottomSheetState()
            var isSheetOpen by remember { mutableStateOf(false) }
            var selectedFish by remember { mutableIntStateOf(0) }
            var isSheetEdited by remember { mutableStateOf(false) }
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            var topBarTitle by remember { mutableStateOf("Flog") }

            fishViewModel =
                ViewModelProvider(this, ViewModelFactory.getInstance(this.application)).get(
                    FishViewModel::class.java
                )

            weatherViewModel =
                ViewModelProvider(this, ViewModelFactory.getInstance(this.application)).get(
                    WeatherViewModel::class.java
                )

            if (isSheetOpen) {
                FlogTheme {
                    ModalBottomSheet(
                        sheetState = sheetState,
                        onDismissRequest = {
                            isSheetOpen = false
                            isSheetEdited = false
                        },
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(text = "Add Fish", style = MaterialTheme.typography.bodyMedium)

                            Spacer(modifier = Modifier.height(16.dp))

                            // Add EditText for fish name

                            val fishEdit by fishViewModel.getFishById(selectedFish).collectAsState(
                                initial = Fish(
                                    nama = "",
                                    berat = 0.0,
                                    harga = 0.0,
                                    createdAt = ""
                                )
                            )
                            var text by remember { mutableStateOf(TextFieldValue("")) }
                            LaunchedEffect(fishEdit) {
                                if (isSheetEdited) {
                                    text = TextFieldValue(fishEdit.nama!!)
                                }
                            }
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
                                if (isSheetEdited) {
                                    fishViewModel.updateFish(fish, fishEdit.id)
                                } else {
                                    fishViewModel.insertFish(fish)
                                }
                                isSheetOpen = false
                                isSheetEdited = false
                            }) {
                                Text(
                                    if (isSheetEdited) {
                                        "Update Fish"
                                    } else {
                                        "Add Fish"
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (!login) {
                FlogTheme {
                    LoginScreen { success,username ->
                        if (success) {
                            login = true
                            name = username
                        }
                    }
                }
            }

            if (login && hasLocationPermission) {
                FlogTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Scaffold(
                            bottomBar = {
                                BottomBar(
                                    buttons = bottomBarItems,
                                    fabRoute = Screen.Camera.route,
                                    fabOnClick = {
                                        navController.navigate(Screen.Camera.route) {
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            },
//                            floatingActionButton = {
//                                if   (currentScreen == Screen.Camera.route) {
//                                    FloatingActionButton(onClick = {
//                                        isSheetOpen = true
//                                    }) {
//                                        Icon(
//                                            imageVector = Icons.Filled.Add,
//                                            contentDescription = "Add"
//                                        )
//                                    }
//                                }
//                            },
//                            floatingActionButtonPosition = FabPosition.End,
//                            topBar = {
//                                TopAppBar(
//                                    title = {
//                                        Text(
//                                            text = topBarTitle,
//                                            fontSize = 25.sp,
//                                        )
//                                    },
//                                    scrollBehavior = scrollBehavior
//                                )
//                            },
//                            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                        ) { paddingValues ->
                            NavHost(
                                modifier = Modifier.padding(bottom = 72.dp),
                                navController = navController,
                                startDestination = Screen.Home.route
                            ) {
                                composable(Screen.Home.route) {
                                    topBarTitle = "Flog"
                                    HomeScreen(weatherViewModel = weatherViewModel)
                                }
                                composable(Screen.Fishes.route) {
                                    topBarTitle = "Fish"
                                    val fishes = fishViewModel.getAllFish()
                                        .collectAsState(initial = emptyList()).value
                                    FishScreen(
                                        fishes = fishes,
                                        fishViewModel = fishViewModel
                                    ) {
                                        isSheetOpen = true
                                        selectedFish = it
                                        isSheetEdited = true

                                    }
                                }
                                composable(Screen.Camera.route) {
                                    topBarTitle = "Camera"
                                    CameraScreen(
                                        question = "What is this fish?",
                                        onImageIdentified = { identifiedText ->
                                            println("Identified Image: $identifiedText")
                                        }
                                    )
                                }
                                composable(Screen.Maps.route) {
                                    topBarTitle = "Maps"
                                    MapsScreen(
                                        latitude = WeatherRepository.latitude,
                                        longitude = WeatherRepository.longitude,
                                        jsonData = fishdata
                                    )
                                }
                                composable(Screen.Account.route) {
                                    topBarTitle = "Account"
//                                    AccountScreen()
                                    AccountScreen(name)
                                }
                            }
                        }
                    }
                }
            }

            else if(!hasLocationPermission){
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

