package cz.pef.TravelPlanner.ui.screens.settings_search

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.navigation.INavigationRouter
import cz.pef.TravelPlanner.ui.elements.BaseScreen
import cz.pef.TravelPlanner.ui.screens.settings_proflle.SettingsProfileData
import cz.pef.TravelPlanner.ui.screens.settings_proflle.SettingsProfileUiState
import cz.pef.TravelPlanner.ui.screens.settings_proflle.SettingsProfileViewModel

@Composable
fun SearchScreen(
    navigation: INavigationRouter,
    selectedIndex: Int = 3 // Index pro nastavení
) {
    val viewModel = hiltViewModel<SettingsSearchViewModel>()
    var data by remember {
        mutableStateOf(SettingsSearchData())
    }

    val settings = viewModel.settingsSearchUIState.collectAsStateWithLifecycle()


    settings.value.let {
        when(it){
            is SettingsSearchUIState.Error -> {

            }
            is SettingsSearchUIState.Loading -> {
                viewModel.loadSettings()
            }
            is SettingsSearchUIState.ScreenDataChanged -> {
                data = it.data
            }
            is SettingsSearchUIState.Success -> {

            }
        }
    }

    BaseScreen(
        topBarText = stringResource(id = R.string.search),
        showLoading = false,
        navigationRouter = navigation,
        onBackClick = { navigation.navigateToSettings() },
        selectedIndex = selectedIndex
    ) {
        SearchScreenContent(
            navigation = navigation,
            viewModel = viewModel,
            data = data

        )
    }
}

@Composable
fun SearchScreenContent(
    viewModel: SettingsSearchViewModel,
    navigation: INavigationRouter,
    data: SettingsSearchData
) {
    val customMarkerEnabled by viewModel.customMarkerEnabled.collectAsState(initial = false)
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
                        .padding(bottom = 16.dp),
                    placeholder = { Text(text = "Enter maximum attractions") }
                )

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
                        .padding(bottom = 16.dp),
                    placeholder = { Text(text = "Enter maximum attractions") }
                )

                Text(
                    text = stringResource(id = R.string.your_location),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = stringResource(id = R.string.latitude) + ": " + data.userSettings.latitude.toString(),
                    onValueChange = { /* Necháme prázdné, protože pole je read-only */ },
                    readOnly = true, // Zajišťuje, že text nelze upravit
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = stringResource(id = R.string.longitude) + ": " + data.userSettings.longitude.toString(),
                    onValueChange = { /* Necháme prázdné, protože pole je read-only */ },
                    readOnly = true, // Zajišťuje, že text nelze upravit
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Text(
                    text =stringResource(R.string.enable_custom_markers), //stringResource(id = R.string.enable_custom_markers),
                    style = MaterialTheme.typography.bodyLarge,

                )
                Switch(
                    checked = customMarkerEnabled,
                    onCheckedChange = { viewModel.toggleCustomMarker(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFF97316),
                        uncheckedThumbColor = Color.Gray
                    )
                )


            }






            Button(
                onClick = {
                    navigation.navigateToLocationSettings()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF97316),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.select_location))
            }

            Button(
                onClick = {
                    viewModel.saveSettings()
                    navigation.navigateToSettings()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF97316),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.save_settings))
            }

        }


/*
        Button(
            onClick = { navigation.navigateToSettings() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF97316),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.back))
        }*/
    }
}