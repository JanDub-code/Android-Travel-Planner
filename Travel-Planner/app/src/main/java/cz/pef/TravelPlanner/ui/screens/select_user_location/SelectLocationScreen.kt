package cz.pef.TravelPlanner.ui.screens.select_user_location

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.navigation.INavigationRouter
import cz.pef.TravelPlanner.ui.elements.BaseScreen
import cz.pef.TravelPlanner.ui.screens.settings_interests.InterestsViewModel
import cz.pef.TravelPlanner.ui.screens.settings_proflle.SettingsProfileData
import cz.pef.TravelPlanner.ui.screens.settings_proflle.SettingsProfileUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLocationScreen(
    navigation: INavigationRouter,
) {
    val viewModel = hiltViewModel<SelectLocationViewModel>()
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    val mapUiSettings = MapUiSettings(
        zoomControlsEnabled = true,
        mapToolbarEnabled = false
    )

    var data by remember { mutableStateOf(SelectLocationData()) }
    val settings = viewModel.selectLocationUIState.collectAsStateWithLifecycle()

    settings.value.let {
        when (it) {
            is SelectLocationUIState.Error -> {
                // Handle error
            }
            is SelectLocationUIState.Loading -> {
                // Handle loading
            }
            is SelectLocationUIState.ScreenDataChanged -> {
                data = it.data
            }
            is SelectLocationUIState.Success -> {
                // Handle success
            }
        }
    }

    // Initialize cameraPositionState
    val cameraPositionState = rememberCameraPositionState()

    // Manually reset camera position when data changes
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

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.select_location),
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2563EB)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF2563EB))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Map
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        uiSettings = mapUiSettings,
                        cameraPositionState = cameraPositionState,
                        onMapClick = { latLng ->
                            selectedLocation = latLng
                        }
                    )
                }

                // Display selected location
                selectedLocation?.let {
                    Text(
                        text = stringResource(R.string.selected_location)+" \nLat: ${it.latitude}, Lng: ${it.longitude}",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Confirm selection button
                Button(
                    onClick = {
                        selectedLocation?.let {
                            viewModel.saveSelectedLocation(
                                lat = it.latitude,
                                lng = it.longitude
                            )
                            navigation.navigateToSearch()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF97316),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.confirm_selection))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Back button
                Button(
                    onClick = { navigation.navigateToSearch() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF97316),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.back))
                }
            }
        }
    }
}


