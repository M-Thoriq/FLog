package com.thoriq.flog.ui.screen

import android.app.Application
import android.widget.EditText
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.thoriq.flog.R
import com.thoriq.flog.data.Fish
import com.thoriq.flog.viewModel.FishViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    fishViewModel: FishViewModel,
    fishes: List<Fish>
) {


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Fish",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            items(fishes) { fish ->
                Card(
                    modifier = modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(8f)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.pngtree_clown_fish_marine_fish_decoration_png_image_4278349),
                                contentDescription = null
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(15f)
                                .padding(start = 8.dp)

                        ) {
                            Row {
                                Text(
                                    " ${fish.nama}", style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(bottom = 12.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row {
                                Text(
                                    "Berat: ${fish.berat} kg",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 5.dp)
                                )
                            }
                            Row {
                                Text(
                                    "Harga: Rp ${fish.harga}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 5.dp)

                                )
                            }
                            Row {
                                Text(
                                    " ${fish.createdAt}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(start = 2.dp)
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.weight(3f)
                        ) {
                            Row(

                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Delete",
                                    tint = Color.Red
                                )
                            }
                            Row(

                            ) {
                                IconButton(onClick = { fishViewModel.deleteFish(fish) }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.Red
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

@Preview
@Composable
private fun FishSceenPrev() {
//    FishScreen()
}