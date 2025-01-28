package cz.pef.TravelPlanner.Settings_profile_MOCK_DATA

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import cz.pef.TravelPlanner.MOCK_SETUP.MockNavigationRouter
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.models.UserSettings
import cz.pef.TravelPlanner.ui.elements.BaseScreen

@Composable
fun MockSettingsProfile(
    navigation: MockNavigationRouter,
    selectedIndex: Int = 3 // Index pro nastavení
) {
    val viewModel = hiltViewModel<MockSettingsProfileViewModel>()
    var data by remember {
        mutableStateOf(MockSettingsProfileData(UserSettings()))
    }

    val settings = viewModel.settingsProfileUIState.collectAsStateWithLifecycle()


    settings.value.let {
        when(it){
            is MockSettingsProfileUiState.ErrorMock -> {

            }
            is MockSettingsProfileUiState.LoadingMock -> {
                viewModel.loadSettings()
            }
            is MockSettingsProfileUiState.ScreenDataChangedMock -> {
                data = it.mockData
            }
            is MockSettingsProfileUiState.SuccessMock -> {

            }
        }
    }

    BaseScreen(
        topBarText = stringResource(id = R.string.profile),
        showLoading = false,
        navigationRouter = navigation,
        selectedIndex = selectedIndex
    ) {
        MockSettingsScreenContent(
            navigation = navigation,
            viewModel = viewModel,
            data = data

        )
    }
}

@Composable
fun MockSettingsScreenContent(
    viewModel: MockSettingsProfileViewModel,
    navigation: MockNavigationRouter,
    data: MockSettingsProfileData
) {
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
                // Text pro username
                Text(
                    text = stringResource(id = R.string.username),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextField(
                    value = data.userSettings.username,
                    onValueChange = { viewModel.usernameChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    placeholder = { Text(text = "Enter your username") }
                )

                // Text pro email
                Text(
                    text = stringResource(id = R.string.email),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextField(
                    value = data.userSettings.email,
                    onValueChange = { viewModel.emailChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.padding(bottom = 8.dp),
                    placeholder = { Text(text = "Enter your email") }
                )
            }

            Button(
                onClick = {
                    viewModel.saveSettings()
                    navigation.returnBack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF97316),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.save_changes))
            }

        }



        // Tlačítko "Back"
        Button(
            onClick = { navigation.returnBack() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF97316),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.back))
        }
    }
}