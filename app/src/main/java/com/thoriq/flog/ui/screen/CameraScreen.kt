package com.thoriq.flog.ui.screen

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Precision
import com.thoriq.flog.viewModel.GeminiModel
import com.thoriq.flog.config.ImageInterpretationUiState
import com.thoriq.flog.viewModel.ImageInterpretationViewModel
import kotlinx.coroutines.launch

import android.content.ContentValues
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CameraScreen(
    question: String,
    onImageIdentified: (String) -> Unit, // Callback with the identified text
    viewModel: ImageInterpretationViewModel = viewModel(factory = GeminiModel)
) {
    val imageInterpretationUiState by viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val imageRequestBuilder = ImageRequest.Builder(context)
    val imageLoader = ImageLoader.Builder(context).build()

    // Mutable state for one image URI
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val captureSuccess = remember { mutableStateOf(false) } // Track capture success

    // Prepare the image Uri where the camera will save the captured image
    val contentResolver = context.contentResolver
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = TakePicture(),
        onResult = { success ->
            captureSuccess.value = success
            if (success) {
                imageUri.value?.let { uri ->
                    coroutineScope.launch {
                        val bitmap = imageUri.value?.let { currentUri ->
                            val imageRequest = imageRequestBuilder
                                .data(currentUri)
                                .size(768)
                                .precision(Precision.EXACT)
                                .build()
                            try {
                                val result = imageLoader.execute(imageRequest)
                                if (result is SuccessResult) {
                                    (result.drawable as BitmapDrawable).bitmap
                                } else null
                            } catch (e: Exception) {
                                null
                            }
                        }
                        if (bitmap != null) {
                            viewModel.reason(question, listOf(bitmap))
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Automatically launch the camera when the screen is loaded
    LaunchedEffect(Unit) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DISPLAY_NAME, "captured_image_${System.currentTimeMillis()}.jpg")
        }
        imageUri.value = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        imageUri.value?.let { uri ->
            cameraLauncher.launch(uri) // Launch the camera to take a picture
        }
    }
    Column(
        modifier = Modifier
            .padding(vertical = 36.dp)
    ) {
        Text("WOIII")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 48.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Display the captured image immediately after it's taken


        // Display UI State (Progress, Success, Error)
        when (imageInterpretationUiState) {
            ImageInterpretationUiState.Initial -> {
                // No output yet
            }

            ImageInterpretationUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    CircularProgressIndicator(color = Color(0XFF02D2A8))
                }
            }

            is ImageInterpretationUiState.Success -> {
                onImageIdentified((imageInterpretationUiState as ImageInterpretationUiState.Success).fish)
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {

                    Card(
                        modifier = Modifier
                            .padding(top = 128.dp)
                            .fillMaxWidth()
                            .height(2800.dp),
                        shape = MaterialTheme.shapes.large,
                    ) {
                        if (captureSuccess.value) {
                            imageUri.value?.let { uri ->

                                AsyncImage(
                                    model = uri,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .offset(x = 0.dp, y = -48.dp)
                                        .size(240.dp)
                                        .padding(20.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )


                            }
                        } else {
                            Text("No image captured yet",)
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(all = 16.dp)
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {

                            Text(
                                text = (imageInterpretationUiState as ImageInterpretationUiState.Success).fish, // Use fish as title
                                color = Color(0xFF000000),
                                fontSize = 20.sp, // Set the font size manually for the title
                                fontWeight = FontWeight.Bold, // Set font weight to bold for the title
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp)) // Add spacing between title and description

                            Text(
                                text = (imageInterpretationUiState as ImageInterpretationUiState.Success).description, // Use description as body text
                                color = Color(0xFF000000),
                                fontSize = 16.sp, // Set the font size manually for the description
                                fontWeight = FontWeight.Normal, // Normal font weight for the description
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

            }


            is ImageInterpretationUiState.Error -> {
                Card(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = Color(0XFF000000))
                ) {
                    Text(
                        text = (imageInterpretationUiState as ImageInterpretationUiState.Error).errorMessage,
                        color = Color(0XFFFF3D00),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}


