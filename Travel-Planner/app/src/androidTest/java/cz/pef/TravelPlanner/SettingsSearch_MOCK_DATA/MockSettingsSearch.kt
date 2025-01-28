package cz.pef.TravelPlanner.SettingsSearch_MOCK_DATA

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.MOCK_SETUP.MockNavigationRouter
import cz.pef.TravelPlanner.ui.elements.BaseScreen
import androidx.compose.runtime.mutableStateOf
import cz.pef.TravelPlanner.SettingsSearch_MOCK_DATAimport.MockSettingsSearchViewModel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun MockSettingsSearch(
    navigation: MockNavigationRouter,
    viewModel: MockSettingsSearchViewModel,
    selectedIndex: Int = 3 // Index pro nastavení
) {
    var data by remember { mutableStateOf(viewModel.mockData) }

    val uiState by viewModel.settingsSearchUIState.collectAsState()

    when(uiState){
        is MockSettingsSearchUIState.ErrorMock -> {
            // Můžete přidat UI pro zobrazení chyby
        }
        is MockSettingsSearchUIState.LoadingMock -> {
            LaunchedEffect(Unit) { viewModel.loadSettings() }
        }
        is MockSettingsSearchUIState.ScreenDataChangedMock -> {
            data = (uiState as MockSettingsSearchUIState.ScreenDataChangedMock).mockData
        }
        is MockSettingsSearchUIState.SuccessMock -> {
            // Můžete přidat UI pro zobrazení úspěchu
        }
    }

    BaseScreen(
        topBarText = stringResource(id = R.string.search),
        showLoading = false,
        navigationRouter = navigation,
        onBackClick = { viewModel.navigateBack() },
        selectedIndex = selectedIndex
    ) {
        MockSearchScreenContent(
            navigation = navigation,
            viewModel = viewModel,
            data = data
        )
    }
}@Composable
fun MockSearchScreenContent(
    navigation: MockNavigationRouter,
    viewModel: MockSettingsSearchViewModel,
    data: MockSettingsSearchData
) {
    // Správné použití collectAsState
    val customMarkerEnabled by viewModel.customMarkerEnabled.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Bílý box kolem vstupních polí
        androidx.compose.material3.Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Text pro max attractions displayed
                Text(
                    text = stringResource(id = R.string.max_attractions_displayed),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextField(
                    value = data.userSettings.maxAttractionsDisplayed.toString(),
                    onValueChange = { viewModel.attractionChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .testTag("maxAttractionsTextField"),
                    placeholder = { Text(text = "Enter maximum attractions") }
                )

                // Text pro max distance
                Text(
                    text = stringResource(id = R.string.maximum_distance),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextField(
                    value = data.userSettings.maxLocationDistance.toString(),
                    onValueChange = { viewModel.distanceChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .testTag("maxDistanceTextField"),
                    placeholder = { Text(text = "Enter maximum distance") }
                )

                // Text pro location
                Text(
                    text = stringResource(id = R.string.your_location),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = stringResource(id = R.string.latitude) + ": " + data.userSettings.latitude.toString(),
                    onValueChange = { /* Necháme prázdné, protože pole je read-only */ },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .testTag("latitudeTextField")
                )

                OutlinedTextField(
                    value = stringResource(id = R.string.longitude) + ": " + data.userSettings.longitude.toString(),
                    onValueChange = { /* Necháme prázdné, protože pole je read-only */ },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .testTag("longitudeTextField")
                )

                // Přepínač pro vlastní markery
                Text(
                    text = stringResource(R.string.enable_custom_markers),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Switch(
                    checked = customMarkerEnabled,
                    onCheckedChange = { viewModel.toggleCustomMarker(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFF97316),
                        uncheckedThumbColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("customMarkerSwitch")
                )
            }

            // Tlačítko "Select Location"
            Button(
                onClick = { viewModel.navigateToLocationSettings() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF97316),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("selectLocationButton")
            ) {
                Text(text = stringResource(id = R.string.select_location))
            }

            // Tlačítko "Save Settings"
            Button(
                onClick = {
                    viewModel.saveSettings()
                    viewModel.navigateBack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF97316),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("saveSettingsButton")
            ) {
                Text(text = stringResource(id = R.string.save_settings))
            }

        }
    }
}
