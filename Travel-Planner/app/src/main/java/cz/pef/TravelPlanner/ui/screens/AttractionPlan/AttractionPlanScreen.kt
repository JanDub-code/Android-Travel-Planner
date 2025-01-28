package cz.pef.TravelPlanner.ui.screens.AttractionPlan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.collections.PolylineManager
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import cz.pef.TravelPlanner.BuildConfig.API_KEY
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.navigation.INavigationRouter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttractionPlanScreen(
    placeId: Long,
    navigation: INavigationRouter,
    onBackClick: () -> Unit,
    viewModel: AttractionPlanViewModel = hiltViewModel()
) {
    LaunchedEffect(placeId) {
        viewModel.loadPlaceById(placeId)
        viewModel.loadPlaceById(placeId)
    }

    val settings = viewModel.selectUIState.collectAsStateWithLifecycle()
    var data by remember { mutableStateOf(AttractionPlanData()) }

    settings.value.let {
        when (it) {
            is AttractionPlanUIStates.Error -> {
            }
            is AttractionPlanUIStates.Loading -> {
            }
            is AttractionPlanUIStates.ScreenDataChanged -> {
                data = it.data
            }
            is AttractionPlanUIStates.Success -> {
            }
        }
    }

    val customtravel by viewModel.customTravelStyle.collectAsState()
    val coordinate = viewModel.coordinates
    val place by viewModel.place.collectAsState()
    val directions by viewModel.directions.collectAsState()

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = true,
                mapToolbarEnabled = true
            )
        )
    }

    var googleMap by remember {
        mutableStateOf<GoogleMap?>(null)
    }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(data.userSettings.latitude, data.userSettings.longitude) {
        val latitude = data.userSettings.latitude ?: 0.0
        val longitude = data.userSettings.longitude ?: 0.0
        cameraPositionState.move(
            com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                LatLng(latitude, longitude),
                10f
            )
        )
    }

    var polylineManager by remember {
        mutableStateOf<PolylineManager?>(null)
    }


    LaunchedEffect(coordinate) {
        if (googleMap != null && polylineManager != null && coordinate.isNotEmpty()) {
            // Pro jistotu můžeme nejdříve smazat staré čáry z mapy.
            googleMap?.clear()

            // Znovu přidáme marker pro Start
            data.userSettings.let { user ->
                if (user.latitude != null && user.longitude != null) {
                    googleMap?.addMarker(
                        com.google.android.gms.maps.model.MarkerOptions()
                            .position(LatLng(user.latitude!!, user.longitude!!))
                            .title("Start")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )
                }
            }

            // Znovu přidáme marker pro End (pokud máme platné place)
            place?.let { p ->
                googleMap?.addMarker(
                    com.google.android.gms.maps.model.MarkerOptions()
                        .position(LatLng(p.lat, p.lng))
                        .title("End")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                )
            }

            // Teď vykreslíme samotnou trasu
            coordinate.forEach { poly ->
                val options = PolylineOptions()
                    .addAll(poly.getLatLngList())
                    .width(10f)

                googleMap?.addPolyline(options)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.attraction_plan),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2563EB))
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navigation.navigateToItinerary()
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = stringResource(id = R.string.return_to_itinerary)
                    )
                },
                text = {
                    Text(stringResource(id = R.string.return_to_itinerary))
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Mapa
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { /* Ignorujeme kliknutí na mapu */ },
                    uiSettings = mapUiSettings
                ) {
                    MapEffect { map ->
                        // Uložíme si GoogleMap pouze při první inicializaci
                        if (googleMap == null) {
                            googleMap = map
                        }
                        // PolylineManager též pouze jednou
                        if (polylineManager == null) {
                            polylineManager = PolylineManager(map)
                            polylineManager?.newCollection("0")
                        }

                        // Marker pro Start (pokud známe uživatelovu lokaci)
                        data.userSettings.let { user ->
                            if (user.latitude != null && user.longitude != null) {
                                googleMap?.addMarker(
                                    com.google.android.gms.maps.model.MarkerOptions()
                                        .position(LatLng(user.latitude!!, user.longitude!!))
                                        .title("Start")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                )
                            }
                        }

                        // Marker pro End jen tehdy, když je `place` nenull
                        place?.let { p ->
                            googleMap?.addMarker(
                                com.google.android.gms.maps.model.MarkerOptions()
                                    .position(LatLng(p.lat, p.lng))
                                    .title("End")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            )
                        }

                        // Pokud už v době první kresby existují `coordinate`, můžeme vykreslit i polyline
                        if (coordinate.isNotEmpty()) {
                            coordinate.forEach { poly ->
                                val options = PolylineOptions()
                                    .addAll(poly.getLatLngList())
                                    .width(10f)

                                googleMap?.addPolyline(options)
                            }
                        }
                    }
                }
            }

            // Detaily místa
            if (place != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Doba cesty
                    Text(
                        text = stringResource(id = R.string.time) + " " +
                                (directions?.routes?.getOrNull(0)?.legs?.getOrNull(0)?.duration?.text ?: "--"),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    // Vzdálenost
                    Text(
                        text = stringResource(id = R.string.distance) + " " +
                                (directions?.routes?.getOrNull(0)?.legs?.getOrNull(0)?.distance?.text ?: "--"),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    // Adresa
                    Text(
                        text = stringResource(id = R.string.address) + " " +
                                (place?.vicinity ?: "Not available"),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            } else {
                Text(
                    text = stringResource(id = R.string.loading),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
