package com.thoriq.flog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.thoriq.flog.data.BottomBarItemData
import com.thoriq.flog.data.Fish
import com.thoriq.flog.data.Lokasi
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
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class MainActivity : ComponentActivity() {

    private val weatherRepository = WeatherRepository(this)
    private var currentScreen by mutableStateOf("Home")
    private lateinit var fishViewModel: FishViewModel
    private lateinit var weatherViewModel: WeatherViewModel
    val latitude: Double
        get() = WeatherRepository.latitude
    val longitude: Double
        get() = WeatherRepository.longitude

    val lokasi : Lokasi
        get() = weatherRepository.getLatLong()

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
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
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(android.Manifest.permission.CAMERA),
                        0
                    )
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
            val imageUri = remember { mutableStateOf<Uri?>(null) }


            fishViewModel =
                ViewModelProvider(this, ViewModelFactory.getInstance(this.application)).get(
                    FishViewModel::class.java
                )

            weatherViewModel =
                ViewModelProvider(this, ViewModelFactory.getInstance(this.application)).get(
                    WeatherViewModel::class.java
                )

//            if (isSheetOpen) {
//                FlogTheme {
//                    ModalBottomSheet(
//                        sheetState = sheetState,
//                        onDismissRequest = {
//                            isSheetOpen = false
//                            isSheetEdited = false
//                        },
//                    ) {
//
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp)
//                        ) {
//                            Text(text = if (isSheetEdited) "Edit Fish" else "Add Fish", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
//
//                            Spacer(modifier = Modifier.height(16.dp))
//                            // Add EditText for fish name
//
//                            val fishEdit by fishViewModel.getFishById(selectedFish).collectAsState(
//                                initial = Fish(
//                                    nama = "",
//                                    image= null,
//                                    berat = 0.0,
//                                    harga = 0.0,
//                                    createdAt = "",
//                                    latitude = 0.0,
//                                    longitude = 0.0
//                                )
//                            )
//                            var namaIkan by remember { mutableStateOf(TextFieldValue("")) }
//                            var beratIkan by remember { mutableStateOf(TextFieldValue("")) }
//                            var hargaIkan by remember { mutableStateOf(TextFieldValue("")) }
//                            val coroutineScope = rememberCoroutineScope()
//                            val pickMedia = rememberLauncherForActivityResult(
//                                ActivityResultContracts.PickVisualMedia()
//                            ) { uri ->
//                                uri?.let {
//                                    coroutineScope.launch {
//                                        val imageRequest = ImageRequest.Builder(context)
//                                            .data(uri)
//                                            .build()
//                                        val imageLoader = coil.ImageLoader(context)
//                                        val bitmap = (imageLoader.execute(imageRequest) as SuccessResult).drawable.toBitmap()
//                                        imageBitmap = bitmap
//                                    }
//                                }
//                            }
//
//                            LaunchedEffect(fishEdit) {
//                                if (isSheetEdited) {
//                                    namaIkan = TextFieldValue(fishEdit.nama!!)
//                                    beratIkan = TextFieldValue(fishEdit.berat.toString())
//                                    hargaIkan = TextFieldValue(fishEdit.harga.toString())
//
//                                }
//                            }
//
//
//
//                            Column(modifier = Modifier.fillMaxWidth()) {
//                                imageBitmap?.let { uri ->
//                                    AsyncImage(
//                                        model = uri,
//                                        contentDescription = null,
//                                        modifier = Modifier
//                                            .padding(4.dp)
//                                            .requiredSize(300.dp)
//                                    )
//                                }
//
//                                Button(
//                                    onClick = {
//                                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//                                    }
//                                ) {
//                                    Text("Upload Image")
//
//                                }
//                                //                                Text("Fish Name")
//                                //                                Spacer(modifier = Modifier.height(12.dp))
//                                TextField(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    value = namaIkan,
//                                    onValueChange = {
//                                        namaIkan = it
//                                    },
//                                    label = { Text(text = "Fish Name") },
//                                    placeholder = { Text(text = "Salmon") },
//                                )
//                            }
//                            Spacer(modifier = Modifier.height(16.dp))
//                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//                                    Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = "Ikan", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(32.dp))
//                                    TextField(
//                                        modifier = Modifier.width(120.dp),
//                                        value = hargaIkan,
//                                        onValueChange = {
//                                            hargaIkan = it
//                                        },
//                                        label = { Text(text = "Price/kg") },
//                                        placeholder = { Text(text = "0.0 Rp") },
//                                    )
//                                }
//
//                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//                                    Icon(imageVector = Icons.Default.Balance, contentDescription = "Ikan", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(32  .dp))
//                                    TextField(
//                                        modifier = Modifier.width(150.dp),
//                                        value = beratIkan,
//                                        onValueChange = {
//                                            beratIkan = it
//                                        },
//                                        label = { Text(text = "Weight") },
//                                        placeholder = { Text(text = "0.0 Kg") },
//                                    )
//                                }
//                            }
//                            Spacer(modifier = Modifier.height(16.dp))
//                            Button(modifier = Modifier.fillMaxWidth(),
//                                onClick = {
//                                    val fish = Fish(
//                                        nama = namaIkan.text,
//                                        image = imageBitmap?.let {
//                                            val stream = ByteArrayOutputStream()
//                                            it.compress(Bitmap.CompressFormat.PNG, 100, stream)
//                                            stream.toByteArray()
//                                        },
//                                        berat = beratIkan.text.toDouble(),
//                                        harga = hargaIkan.text.toDouble(),
//                                        createdAt = "",
//                                        latitude = lokasi.latitude,
//                                        longitude = lokasi.longitude
//                                    )
//                                    if (isSheetEdited) {
//                                        fishViewModel.updateFish(fish, fishEdit.id)
//                                    } else {
//                                        fishViewModel.insertFish(fish)
//                                    }
//                                    isSheetOpen = false
//                                    isSheetEdited = false
//                                }) {
//                                Text(
//                                    if (isSheetEdited) {
//                                        "Update Fish"
//                                    } else {
//                                        "Add Fish"
//                                    }
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//            else {
//                imageBitmap = null
//            }

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
            if (login &&  hasLocationPermission) {
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
                        ) { paddingValues ->
                            NavHost(
                                modifier = Modifier.padding(bottom = 72.dp),
                                navController = navController,
                                startDestination = Screen.Home.route
                            ) {
                                composable(Screen.Home.route) {
                                    val fishes = fishViewModel.getAllFish()
                                        .collectAsState(initial = emptyList()).value
                                    topBarTitle = "Flog"
                                    HomeScreen(
                                        weatherViewModel = weatherViewModel,
                                        fishes = fishes
                                    )
                                }
                                composable(Screen.Fishes.route) {
                                    topBarTitle = "Fish"
                                    val fishes = fishViewModel.getAllFish()
                                        .collectAsState(initial = emptyList()).value
                                    FishScreen(
                                        fishes = fishes,
                                        fishViewModel = fishViewModel,
                                        onAddButtonClick = {
                                            val intent = Intent(this@MainActivity, LogActivity::class.java).apply {
                                                putExtra("isEdit", false)
                                            }
                                            startActivity(intent)
                                        },
                                    ) {
                                        fishId ->
                                        val intent = Intent(this@MainActivity, LogActivity::class.java).apply {
                                            putExtra("isEdit", true)
                                            putExtra("fishId", fishId)
                                        }
                                        startActivity(intent)
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
                                    val fishes = fishViewModel.getAllFish()
                                        .collectAsState(initial = emptyList()).value
                                    MapsScreen(
                                        latitude = WeatherRepository.latitude,
                                        longitude = WeatherRepository.longitude,
                                        fishes = fishes,
                                        lokasi = weatherViewModel.lokasi
                                    )
                                }
                                composable(Screen.Account.route) {
                                    topBarTitle = "Account"
                                    val count = fishViewModel.getCount()
                                        .collectAsState(initial = 0).value
                                    AccountScreen(name, count = count){
                                        (context as Activity).let {
                                            val intent = it.intent
                                            it.finish()
                                            it.startActivity(intent)
                                        }
                                    }
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
