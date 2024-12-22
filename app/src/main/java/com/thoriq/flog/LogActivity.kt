package com.thoriq.flog

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.thoriq.flog.data.Fish
import com.thoriq.flog.ui.theme.FlogTheme
import com.thoriq.flog.viewModel.FishViewModel
import com.thoriq.flog.viewModel.factory.ViewModelFactory
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class LogActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isEdit = intent.getBooleanExtra("isEdit", false)
        val fishId = intent.getIntExtra("fishId", -1)

        setContent {
            FlogTheme {
                FishFormScreen(
                    isEdit = isEdit,
                    fishId = if (fishId != -1) fishId else null,
                    fishViewModel = ViewModelProvider(
                        this,
                        ViewModelFactory.getInstance(application)
                    ).get(FishViewModel::class.java)
                ) {
                    finish() // Close this activity and return
                }
            }
        }
    }
}


@Composable
fun FishFormScreen(
    isEdit: Boolean,
    fishId: Int? = null,
    fishViewModel: FishViewModel,
    onNavigateBack: () -> Unit
) {
    var fish by remember {
        mutableStateOf(
            Fish(
                nama = "",
                image = null,
                berat = 0.0,
                harga = 0.0,
                createdAt = "",
                latitude = 0.0,
                longitude = 0.0
            )
        )
    }
    var namaIkan by remember { mutableStateOf(TextFieldValue("")) }
    var beratIkan by remember { mutableStateOf(TextFieldValue("")) }
    var hargaIkan by remember { mutableStateOf(TextFieldValue("")) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            coroutineScope.launch {
                val imageRequest = ImageRequest.Builder(context)
                    .data(uri)
                    .build()
                val imageLoader = coil.ImageLoader(context)
                val bitmap = (imageLoader.execute(imageRequest) as SuccessResult).drawable.toBitmap()
                imageBitmap = bitmap
            }
        }
    }

    LaunchedEffect(isEdit, fishId) {
        if (isEdit && fishId != null) {
            fishViewModel.getFishById(fishId).collect { loadedFish ->
                fish = loadedFish
                namaIkan = TextFieldValue(loadedFish.nama ?: "")
                beratIkan = TextFieldValue(loadedFish.berat.toString())
                hargaIkan = TextFieldValue(loadedFish.harga.toString())
                loadedFish.image?.let {
                    imageBitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = if (isEdit) "Edit Fish" else "Add Fish",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.border(1.dp, Color(0xFF51565E), RoundedCornerShape(4.dp)).fillMaxWidth().height(150.dp).padding(2.dp)){

            imageBitmap?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            }
        }


        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }) {
            Text("Upload Image")
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = namaIkan,
            onValueChange = { namaIkan = it },
            label = { Text(text = "Fish Name") },
            placeholder = { Text(text = "Salmon") }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = hargaIkan,
                onValueChange = { hargaIkan = it },
                label = { Text(text = "Price/kg") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = beratIkan,
                onValueChange = { beratIkan = it },
                label = { Text(text = "Weight") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val fishToSave = fish.copy(
                    nama = namaIkan.text,
                    berat = beratIkan.text.toDoubleOrNull() ?: 0.0,
                    harga = hargaIkan.text.toDoubleOrNull() ?: 0.0,
                    image = imageBitmap?.let {
                        ByteArrayOutputStream().apply {
                            it.compress(Bitmap.CompressFormat.PNG, 100, this)
                        }.toByteArray()
                    }
                )
                if (isEdit) {
                    fishViewModel.updateFish(fishToSave, fish.id)
                } else {
                    fishViewModel.insertFish(fishToSave)
                }
                onNavigateBack()
            }
        ) {
            Text(if (isEdit) "Update Fish" else "Add Fish")
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlogTheme {
        Greeting("Android")
    }
}