package cz.pef.TravelPlanner.ui.screens.MapScreen

import android.graphics.Bitmap.Config
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.GridBasedAlgorithm
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import cz.pef.TravelPlanner.BuildConfig.API_KEY
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.communication.PlaceResult
import cz.pef.TravelPlanner.models.PlaceResultEntity
import cz.pef.TravelPlanner.navigation.INavigationRouter
import cz.pef.TravelPlanner.ui.elements.BaseScreen
import cz.pef.TravelPlanner.ui.screens.select_user_location.SelectLocationData
import cz.pef.TravelPlanner.ui.screens.select_user_location.SelectLocationUIState
import cz.pef.TravelPlanner.ui.screens.select_user_location.SelectLocationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navigation: INavigationRouter) {
    val praguePosition = LatLng(49.1951, 16.6068)

    val viewModel = hiltViewModel<MapViewModel>()
    val customMarkerEnabled by viewModel.customMarkerEnabled.collectAsState(initial = false)
    LaunchedEffect(Unit) {
        viewModel.initializeCustomMarker()
    }

    val settings = viewModel.selectUIState.collectAsStateWithLifecycle()
    var data by remember { mutableStateOf(MapScreenData()) }

    LaunchedEffect(settings.value) {
        when (val currentState = settings.value) {
            is MapScreenUIState.ScreenDataChanged -> {
                data = currentState.data
            }
            is MapScreenUIState.Error -> {
                Log.e("MapScreen", "Error: ${currentState.errorMessage}")
            }
            is MapScreenUIState.Loading -> {
                Log.d("MapScreen", "Loading data...")
            }
            is MapScreenUIState.Success -> {
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState()

    // Aktualizace pozice kamery při změně uživatelských nastavení
    LaunchedEffect(data.userSettings.latitude, data.userSettings.longitude) {
        val latitude = data.userSettings.latitude.takeIf { it != 0.0 } ?: praguePosition.latitude
        val longitude = data.userSettings.longitude.takeIf { it != 0.0 } ?: praguePosition.longitude
        cameraPositionState.move(
            com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                LatLng(latitude, longitude),
                10f
            )
        )
    }

    val mapUiSettings = MapUiSettings(
        zoomControlsEnabled = true,
        mapToolbarEnabled = true
    )

    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }
    val context = LocalContext.current

    var selectedPlace by remember { mutableStateOf<PlaceResult?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Reference na ClusterManager
    val clusterManagerRef = remember { mutableStateOf<ClusterManager<PlaceResult>?>(null) }

    // Aktualizace ClusterManager při změně dat nebo nastavení
    LaunchedEffect(data.places, customMarkerEnabled, googleMap) {
        googleMap?.let { map ->
            if (clusterManagerRef.value == null) {
                val clusterManager = ClusterManager<PlaceResult>(context, map).apply {
                    algorithm = GridBasedAlgorithm()
                    renderer = MapRenderer(context, map, this, isCustomMarkerEnabled = customMarkerEnabled)

                    setOnClusterItemClickListener { place ->
                        selectedPlace = place
                        showBottomSheet = true
                        true
                    }

                    // Nastavení ClusterManager jako listeneru pro idle stav kamery
                    map.setOnCameraIdleListener(this)
                }
                clusterManagerRef.value = clusterManager
            }

            clusterManagerRef.value?.let { clusterManager ->
                clusterManager.clearItems()
                clusterManager.addItems(data.places)
                clusterManager.cluster()
            }
        }
    }

    BaseScreen(
        topBarText = stringResource(id = R.string.map),
        onBackClick = {
            viewModel.refreshResults()
        },
        navigationRouter = navigation,
        selectedIndex = 0
    ) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Zobrazení mapy
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                uiSettings = mapUiSettings,
                cameraPositionState = cameraPositionState,
                onMapClick = {
                    selectedPlace = null
                    showBottomSheet = false
                }
            ) {
                // MapEffect pro inicializaci ClusterManager
                MapEffect { map ->
                    googleMap = map
                    // Inicializace ClusterManager, pokud ještě není nastaven
                    if (clusterManagerRef.value == null) {
                        val clusterManager = ClusterManager<PlaceResult>(context, map).apply {
                            algorithm = GridBasedAlgorithm()
                            renderer = MapRenderer(context, map, this, isCustomMarkerEnabled = customMarkerEnabled)

                            setOnClusterItemClickListener { place ->
                                selectedPlace = place
                                showBottomSheet = true
                                true
                            }

                            // Nastavení ClusterManager jako listeneru pro idle stav kamery
                            map.setOnCameraIdleListener(this)
                        }
                        clusterManagerRef.value = clusterManager
                    }

                    // Aktualizace ClusterManager s novými místy
                    clusterManagerRef.value?.let { clusterManager ->
                        clusterManager.clearItems()
                        clusterManager.addItems(data.places)
                        clusterManager.cluster()
                    }
                }
            }

            // Zobrazení `ModalBottomSheet` pro vybrané místo
            if (showBottomSheet && selectedPlace != null) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                text = "${stringResource(id = R.string.place)} ${selectedPlace?.name ?: "Neznámý"}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(text = "${stringResource(id = R.string.address)} ${selectedPlace?.vicinity ?: "Neznámá"}")
                            Text(text = "${stringResource(id = R.string.rating)} ${selectedPlace?.rating ?: "--"}")

                            Spacer(modifier = Modifier.height(8.dp))

                            // Ikona pro uložení
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable {
                                        selectedPlace?.let { place ->
                                            viewModel.savePlace(
                                                PlaceResultEntity(
                                                    name = place.name,
                                                    vicinity = place.vicinity,
                                                    rating = place.rating,
                                                    lat = place.geometry.location.lat,
                                                    lng = place.geometry.location.lng,
                                                    photo_reference = place.photos?.firstOrNull()?.photo_reference,
                                                    icon = place.icon
                                                )
                                            )
                                            showBottomSheet = false
                                        }
                                    }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.add), // Vložte svůj zdroj pro ikonu
                                    contentDescription = "Save ${selectedPlace?.name}",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
