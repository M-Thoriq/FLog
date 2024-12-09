package com.thoriq.flog.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thoriq.flog.viewModel.GeminiModel
import com.thoriq.flog.viewModel.ImageInterpretationViewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.thoriq.flog.config.ImageInterpretationUiState
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    question: String,
    onImageIdentified: (String) -> Unit, // Callback with the identified text
    viewModel: ImageInterpretationViewModel = viewModel(factory = GeminiModel)
)

{
    //make a cameraX
    val imageInterpretationUiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var capturedImageBitmap: Bitmap? by remember { mutableStateOf(null) } // Store the captured image

    Box(modifier = Modifier.fillMaxSize()) {
        val previewView = remember { PreviewView(context) }

        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        ) { previewViewRef ->
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewViewRef.surfaceProvider)
                }

                imageCapture = ImageCapture.Builder().build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(context))
        }

        FloatingActionButton(
            onClick = {
                val photoFile = File(
                    context.cacheDir,
                    "${System.currentTimeMillis()}.jpg"
                )

                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                imageCapture?.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            // Load the image as Bitmap after capture
                            val bitmap = BitmapFactory.decodeFile(photoFile.path)

                            if (bitmap != null) {
                                capturedImageBitmap = bitmap // Save the captured image
                                // Call reason with valid bitmap
                                viewModel.reason(question, listOf(capturedImageBitmap))
                            } else {
                                // Handle the error case where the bitmap is null
                                Log.e("CameraCapture", "Failed to decode bitmap")
                            }

                        }

                        override fun onError(exception: ImageCaptureException) {
                            exception.printStackTrace()
                        }
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Camera, contentDescription = "Take Picture")
        }

        Text(
            text = question,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(8.dp),
            color = Color.White
        )

        // Show the captured image in a Card after it is captured
        capturedImageBitmap?.let { bitmap ->
            Log.d("bitmap", bitmap.toString())
            // Check the state safely before accessing it
            when (val state = imageInterpretationUiState) {
                is ImageInterpretationUiState.Loading -> {
                    // Handle loading state if necessary, maybe show a loading spinner
                    Log.d("loadings", bitmap.toString())

                }
                is ImageInterpretationUiState.Success -> {
                    // Trigger the callback with the fish result (only if Success)
                    Log.d("suksed", bitmap.toString())

                    onImageIdentified(state.fish)

                    // Show the image in the card
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp), // Padding around the Row
                            verticalAlignment = Alignment.CenterVertically // Align vertically in the center
                        ) {
                            // Image on the left
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Captured Image",
                                modifier = Modifier
                                    .size(100.dp) // Adjust the size of the image
                                    .padding(end = 16.dp), // Padding between image and text
                                contentScale = ContentScale.Crop
                            )

                            // Column for Name and Description on the right
                            Column(
                                modifier = Modifier
                                    .weight(1f) // Take up remaining space
                            ) {
                                // Name text
                                Text(
                                    text = state.fish, // Using `fish` from Success
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                // Description text
                                Text(
                                    text = "Description", // You can replace this with actual description data
                                )
                            }
                        }
                    }
                }
                is ImageInterpretationUiState.Error -> {
                    Log.d("errors", bitmap.toString())
                    Log.d("error", (state as ImageInterpretationUiState.Error).errorMessage)
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = (state as ImageInterpretationUiState.Error).errorMessage,
                            modifier = Modifier.padding(16.dp)
                        )

                    }
                }

                ImageInterpretationUiState.Initial -> TODO()
            }
        }
    }
}






//{
//    val imageInterpretationUiState by viewModel.uiState.collectAsState()
//
//    val coroutineScope = rememberCoroutineScope()
//    val context = LocalContext.current
//    val imageRequestBuilder = ImageRequest.Builder(context)
//    val imageLoader = ImageLoader.Builder(context).build()
//
//    // Mutable state for one image URI
//    val imageUri = remember { mutableStateOf<Uri?>(null) }
//    val captureSuccess = remember { mutableStateOf(false) } // Track capture success
//
//    // Prepare the image Uri where the camera will save the captured image
//    val contentResolver = context.contentResolver
//    val cameraLauncher = rememberLauncherForActivityResult(
//        contract = TakePicture(),
//        onResult = { success ->
//            captureSuccess.value = success
//            if (success) {
//                imageUri.value?.let { uri ->
//                    coroutineScope.launch {
//                        val bitmap = imageUri.value?.let { currentUri ->
//                            val imageRequest = imageRequestBuilder
//                                .data(currentUri)
//                                .size(768)
//                                .precision(Precision.EXACT)
//                                .build()
//                            try {
//                                val result = imageLoader.execute(imageRequest)
//                                if (result is SuccessResult) {
//                                    (result.drawable as BitmapDrawable).bitmap
//                                } else null
//                            } catch (e: Exception) {
//                                null
//                            }
//                        }
//                        if (bitmap != null) {
//                            viewModel.reason(question, listOf(bitmap))
//                        }
//                    }
//                }
//            } else {
//                Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
//            }
//        }
//    )
//
//    // Automatically launch the camera when the screen is loaded
//    LaunchedEffect(Unit) {
//        val values = ContentValues().apply {
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//            put(MediaStore.Images.Media.DISPLAY_NAME, "captured_image_${System.currentTimeMillis()}.jpg")
//        }
//        imageUri.value = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//        imageUri.value?.let { uri ->
//            cameraLauncher.launch(uri) // Launch the camera to take a picture
//        }
//    }
//    Row(
//        modifier = Modifier
//            .padding(24.dp)
//            .padding(top = 16.dp)
//    ) {
////        Column {
////            Icon(
////                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
////                contentDescription = "",
////            )
////        }
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            modifier = Modifier
//                .padding(start = 16.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Default.Info,
//                contentDescription = "",
//
//                )
//            Text("Fish Info",
//                style = MaterialTheme.typography.headlineLarge,
//                fontWeight = FontWeight.Bold,
//
//                )
//        }
//
//
//
//    }
//    Box(
//        modifier = Modifier
//            .padding(top = 12.dp)
//            .verticalScroll(rememberScrollState())
//    ) {
//        // Display the captured image immediately after it's taken
//
//
//        // Display UI State (Progress, Success, Error)
//        when (imageInterpretationUiState) {
//            ImageInterpretationUiState.Initial -> {
//                // No output yet
//            }
//
//            ImageInterpretationUiState.Loading -> {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center,
//                    modifier = Modifier
//                        .padding(top = 390.dp)
//                        .fillMaxSize()
//                        .padding(8.dp)
//
//                ) {
//                    CircularProgressIndicator(color = Color(0XFF02D2A8))
//                }
//            }
//
//            is ImageInterpretationUiState.Success -> {
//                onImageIdentified((imageInterpretationUiState as ImageInterpretationUiState.Success).fish)
//                Box(
//                    modifier = Modifier.fillMaxSize()
//                ) {
//                    if (captureSuccess.value) {
//                        imageUri.value?.let { uri ->
//                            // AsyncImage centered and overlapping the top of the Card
//                            AsyncImage(
//                                model = uri,
//
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .zIndex(100.0f)
//                                    .padding(top = 100.dp)
//                                    .size(180.dp)
//                                    .align(Alignment.TopCenter) // Center horizontally within the Card
//                                    .offset(y = 16.dp) // Move up to overlap the Card
//                                    .clip(CircleShape),
//                                contentScale = ContentScale.Crop
//                            )
//                        }
//                    } else {
//                        Text(
//                            "No image captured yet",
//                            modifier = Modifier.align(Alignment.Center)
//                        )
//                    }
//                    Card(
//                        modifier = Modifier
//                            .padding(top = 180.dp)
//                            .padding(horizontal = 16.dp)
//                                .fillMaxWidth(),
//
//                        shape = MaterialTheme.shapes.large,
//                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
//                    ) {
//                        Box {
//
//
//                            Column(
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                modifier = Modifier
//                                    .padding(top = 145.dp, bottom = 16.dp)
//                                    .padding(horizontal = 32.dp)// Adjust padding to avoid overlapping AsyncImage
//                                    .fillMaxSize()
//                            ) {
//                                Text(
//                                    text = (imageInterpretationUiState as ImageInterpretationUiState.Success).fish,
//                                    color = Color(0xFF000000),
//                                    fontSize = 20.sp,
//                                    fontWeight = FontWeight.Bold,
//                                )
//
//                                Spacer(modifier = Modifier.height(8.dp))
//
//                                Text(
//                                    text = (imageInterpretationUiState as ImageInterpretationUiState.Success).description,
//                                    color = Color(0xFF000000),
//                                    fontSize = 16.sp,
//                                    fontWeight = FontWeight.Normal,
//                                    modifier = Modifier.fillMaxWidth()
//                                )
//                            }
//                        }
//                    }
//
//                }
//
//            }
//
//
//            is ImageInterpretationUiState.Error -> {
//                Card(
//                    modifier = Modifier
//                        .padding(vertical = 16.dp)
//                        .fillMaxWidth(),
//                    shape = MaterialTheme.shapes.large,
//                    colors = CardDefaults.cardColors(containerColor = Color(0XFF000000))
//                ) {
//                    Text(
//                        text = (imageInterpretationUiState as ImageInterpretationUiState.Error).errorMessage,
//                        color = Color(0XFFFF3D00),
//                        modifier = Modifier.padding(16.dp)
//                    )
//                }
//            }
//        }
//    }
//}


