package cz.pef.TravelPlanner.ui.screens.photo_scanner

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.navigation.INavigationRouter
import cz.pef.TravelPlanner.ui.screens.settings_interests.InterestChip
import cz.pef.TravelPlanner.ui.screens.settings_interests.InterestsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoScannerScreen(
    navigation: INavigationRouter
) {
    val viewModel = hiltViewModel<InterestsViewModel>()
    val viewModelPhoto = hiltViewModel<PhotoScannerViewModel>()
    val selectedInterests by viewModel.selectedInterests.collectAsStateWithLifecycle()

    var isLoading by remember { mutableStateOf(false) }

    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    val detectedInterests = remember { mutableStateListOf<String>() }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> selectedPhotoUri = uri }
    )

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.photo_scanner),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }, navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { navigation.navitageToInterests() } // Kliknutelný obsah
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Color.White // Barva šipky
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Mezera mezi šipkou a textem
                        Text(
                            text = stringResource(id = R.string.back),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2563EB))
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF2563EB))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Zobrazení vybraného obrázku
                if (selectedPhotoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = selectedPhotoUri),
                        contentDescription = "Selected picture",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.your_picture),
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tlačítko pro výběr fotky z galerie
                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF97316),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.select_picture))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tlačítko pro analýzu obrázku
                val coroutineScope = rememberCoroutineScope()

                if (selectedPhotoUri != null) {
                    Button(
                        onClick = {
                            isLoading = true
                            coroutineScope.launch {
                                viewModelPhoto.analyzeImage(
                                    context = context,
                                    photoUri = selectedPhotoUri!!
                                ) { detectedLabels ->
                                    detectedInterests.clear()
                                    detectedInterests.addAll(detectedLabels)
                                    isLoading = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF97316),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.scan_picture_for_interests))
                    }
                }



                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }

                // Detekované zájmy
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(detectedInterests) { interest ->
                        ScannerToggleButton(
                            text = interest,
                            isSelected = selectedInterests.contains(interest),
                            onToggle = {
                                coroutineScope.launch {
                                    viewModel.toggleInterest(interest) // Přidání nebo odstranění zájmu
                                }
                            }
                        )
                    }
                }
/*
                Spacer(modifier = Modifier.height(16.dp))

                // Tlačítko pro návrat zpět
                Button(
                    onClick = { navigation.navitageToInterests() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF97316),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.back))
                }*/
            }
        }
    }
}

@Composable
fun ScannerToggleButton(
    text: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Button(
        onClick = onToggle,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF34C759) else Color(0xFF1E88E5),
            contentColor = Color.White
        ),
        modifier = Modifier
            //.fillMaxWidth(0.3f) // Zajistí tři tlačítka vedle sebe
            .padding(4.dp) // Menší padding
    ) {
        Text(text = text, textAlign = TextAlign.Center)
    }
}