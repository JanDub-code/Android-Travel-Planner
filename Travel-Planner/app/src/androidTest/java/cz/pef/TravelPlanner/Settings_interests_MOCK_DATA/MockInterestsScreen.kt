package cz.pef.TravelPlanner.Settings_interests_MOCK_DATA
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.navigation.INavigationRouter
import cz.pef.TravelPlanner.ui.elements.BaseScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
@Composable
fun MockInterestsScreen (
    navigation: INavigationRouter,
    viewModel: MockInterestsViewModel = hiltViewModel(),
    selectedIndex: Int = 3 // Index pro zájmy
) {
    val selectedInterests by viewModel.selectedInterests.collectAsState()
    val interests = listOf(
        "Nature", "Culture", "Adventure", "Food"
    )

    BaseScreen(
        topBarText = stringResource(id = R.string.choose_your_interests),
        showLoading = false,
        onBackClick = { navigation.navigateToSettings() },
        navigationRouter = navigation,
        selectedIndex = selectedIndex
    ) {
        MockInterestsScreenContent(
            interests = interests,
            selectedInterests = selectedInterests,
            onInterestToggle = { viewModel.toggleInterest(it) },
            onAddPhotoClick = { viewModel.addInterestsFromPhoto() },
            navigation = navigation,
            viewModel = viewModel

        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MockInterestsScreenContent(
    interests: List<String>,
    selectedInterests: List<String>,
    onInterestToggle: (String) -> Unit,
    onAddPhotoClick: () -> Unit,
    navigation: INavigationRouter,
    viewModel: MockInterestsViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Grid zájmů
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            items(interests) { interest ->
                InterestToggleButton(
                    text = interest,
                    isSelected = selectedInterests.contains(interest),
                    onToggle = { onInterestToggle(interest) },
                    tag = interest // Přidáme tag pro testování
                )
            }
        }

        // Tlačítko pro přidání fotky
        Button(
            onClick = { navigation.navigateToPhotoScanner() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF97316),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .testTag("addInterestsFromPhotoButton") // Přidáme testTag
        ) {
            Text(text = stringResource(id = R.string.add_interests_from_photo))
        }

        // Sekce dalších vybraných zájmů
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.other_selected_interests),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(selectedInterests) { interest ->
                InterestChip(
                    text = interest,
                    onClick = { onInterestToggle(interest) },
                    tag = "selectedInterestChip_$interest" // Přidáme unikátní tag
                )
            }
        }

        // Tlačítko "Save Settings"
        Button(
            onClick = { viewModel.saveSettings() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF97316),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .testTag("saveSettingsButton") // Přidáme testTag
        ) {
            Text(text = stringResource(id = R.string.save_settings))
        }
    }
}

@Composable
fun InterestToggleButton(
    text: String,
    isSelected: Boolean,
    onToggle: () -> Unit,
    tag: String
) {
    Button(
        onClick = onToggle,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF34C759) else Color(0xFF1E88E5),
            contentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .testTag(tag) // Přidáme testTag
    ) {
        Text(text = text, textAlign = TextAlign.Center)
    }
}

@Composable
fun InterestChip(
    text: String,
    onClick: () -> Unit,
    tag: String
) {
    Box(
        modifier = Modifier
            .background(Color(0xFF34C759), shape = MaterialTheme.shapes.small)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() }
            .testTag(tag) // Přidáme testTag
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}