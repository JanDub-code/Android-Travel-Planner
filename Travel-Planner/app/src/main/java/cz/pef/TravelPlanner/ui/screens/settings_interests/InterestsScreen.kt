package cz.pef.TravelPlanner.ui.screens.settings_interests

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.navigation.INavigationRouter
import cz.pef.TravelPlanner.ui.elements.BaseScreen
@Composable
fun InterestsScreen(
    navigation: INavigationRouter,
    selectedIndex: Int = 3 // Index pro zájmy
) {
    val viewModel = hiltViewModel<InterestsViewModel>()
    val selectedInterests by viewModel.selectedInterests.collectAsStateWithLifecycle()
    val interests = listOf(
        InterestOption(
            value = "Nature",
            displayText = stringResource(id = R.string.nature)
        ),
        InterestOption(
            value = "Culture",
            displayText = stringResource(id = R.string.culture)
        ),
        InterestOption(
            value = "Adventure",
            displayText = stringResource(id = R.string.adventure)
        ),
        InterestOption(
            value = "Food",
            displayText = stringResource(id = R.string.food)
        )
    )

    val zobrazeneInterests = listOf(stringResource(id = R.string.nature),
        stringResource(id = R.string.culture),
        stringResource(id = R.string.adventure),
        stringResource(id = R.string.food)
    )

    BaseScreen(
        topBarText = stringResource(id = R.string.choose_your_interests),
        showLoading = false,
        onBackClick = { navigation.navigateToSettings() },
        navigationRouter = navigation,
        selectedIndex = selectedIndex
    ) {
        InterestsScreenContent(
            interests = interests,
            selectedInterests = selectedInterests,
            onInterestToggle = { viewModel.toggleInterest(it) },
            onAddPhotoClick = { viewModel.addInterestsFromPhoto() },
            navigation = navigation
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestsScreenContent(
    interests: List<InterestOption>,
    selectedInterests: List<String>,
    onInterestToggle: (String) -> Unit,
    onAddPhotoClick: () -> Unit,
    navigation: INavigationRouter
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
            items(interests) { interestOption ->
                InterestToggleButton(
                    text = interestOption.displayText,
                    isSelected = selectedInterests.contains(interestOption.value),
                    onToggle = { onInterestToggle(interestOption.value) }
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
                    onClick = {
                        onInterestToggle(interest)
                    }
                )
            }
        }
    }
}

@Composable
fun InterestToggleButton(
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
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = text, textAlign = TextAlign.Center)
    }
}

@Composable
fun InterestChip(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color(0xFF34C759), shape = MaterialTheme.shapes.small)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() } // Klikací akce
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EnhancedInterestChip(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFF34C759), shape = MaterialTheme.shapes.medium) // Kulaté rohy
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

data class InterestOption(
    val value: String,
    val displayText: String
)
