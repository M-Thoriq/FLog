package com.thoriq.flog.ui.screen

import android.app.Application
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.EditText
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.thoriq.flog.R
import com.thoriq.flog.data.Fish
import com.thoriq.flog.viewModel.FishViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishScreen(
    modifier: Modifier = Modifier,
    fishViewModel: FishViewModel,
    fishes: List<Fish>,
    onAddButtonClick: () -> Unit,
    onSelectedFish: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 36.dp)
            .padding(horizontal = 24.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    imageVector = Icons.Filled.Anchor,
                    contentDescription = "Anchor Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = "Catch", style = MaterialTheme.typography.titleLarge, modifier = Modifier
                        .padding(start = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp

                )
            }

            IconButton(onClick = { onAddButtonClick() }, modifier = Modifier.clip(CircleShape).background(MaterialTheme.colorScheme.primary)) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Icon Add", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }

        if (fishes.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = "No fishing logs! Add one now!",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier


            ) {
                items(fishes) { fish ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer // Set the card's background color
                        ),

                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.weight(8f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                if (fish.image != null) {
                                    val imageBitmap = BitmapFactory.decodeByteArray(fish.image, 0, fish.image!!.size).asImageBitmap()
                                    Image(
                                        bitmap = imageBitmap,
                                        contentDescription = "Fish Image",
                                        modifier = Modifier
                                            .size(116.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_flog),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(15f)
                                    .padding(start = 8.dp),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Row {
                                    Text(
                                        " ${fish.nama}",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier
                                            .padding(bottom = 12.dp),
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                                Row {
                                    Text(
                                        "Berat: ${fish.berat} kg",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(start = 5.dp)
                                    )
                                }
                                Row {
                                    Text(
                                        "Harga: Rp ${fish.harga}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(start = 5.dp)

                                    )
                                }
                                Row {
                                    Text(
                                        " ${fish.createdAt}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(start = 2.dp)
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier.weight(3f)
                            ) {
                                Row(

                                ) {
                                    IconButton(onClick = { onSelectedFish(fish.id) }) {
                                        Icon(
                                            imageVector = Icons.Filled.Edit,
                                            contentDescription = "Update",

                                            )
                                    }
                                }
                                Row(

                                ) {
                                    IconButton(onClick = { fishViewModel.deleteFish(fish) }) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Delete",
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }

                            }

                        }
                    }
                }
            }
        }

    }


}

@Preview
@Composable
private fun FishSceenPrev() {
//    FishScreen()
}