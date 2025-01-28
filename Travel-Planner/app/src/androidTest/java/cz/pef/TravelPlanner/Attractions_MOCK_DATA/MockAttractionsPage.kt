package cz.pef.TravelPlanner.Attractions_MOCK_DATA

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import cz.pef.TravelPlanner.MOCK_SETUP.MockNavigationRouter
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.navigation.INavigationRouter
import cz.pef.TravelPlanner.ui.elements.BaseScreen
import cz.pef.TravelPlanner.models.PlaceResultEntity
import cz.pef.TravelPlanner.ui.screens.attractions.AttractionsViewModel
@Composable
fun MockAttractionsScreen(
    navigation: MockNavigationRouter,
    selectedIndex: Int = 2 // Index pro nastavení
) {
    val viewModel: MockAttractionsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    BaseScreen(
        topBarText = stringResource(id = R.string.attractions),
        showLoading = uiState.isLoading,
        navigationRouter = navigation,
        selectedIndex = selectedIndex
    ) {
        MockAttractionsScreenContent(
            navigation = navigation,
            uiState = uiState,
            onSaveClick = { /*place -> viewModel.savePlace(place) */}
        )
    }
}

@Composable
fun MockAttractionsScreenContent(
    navigation: INavigationRouter,
    uiState: MockAttractionUIState,
    onSaveClick: (PlaceResultEntity) -> Unit
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("LoadingBox"),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading...")
            }
        }
        uiState.error != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("ErrorBox"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error ?: "An unknown error occurred",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red
                )
            }
        }
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("AttractionsLazyColumn")
            ) {
                items(uiState.attractions) { attraction ->
                    AttractionCard(attraction, onSaveClick)
                }
            }
        }
    }
}

@Composable
fun AttractionCard(attraction: PlaceResultEntity, onSaveClick: (PlaceResultEntity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("AttractionCard_${attraction.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Box s obrázkem ikony
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Gray)
                    .testTag("AttractionIcon_${attraction.id}")
            ) {
                Image(
                    painter = rememberAsyncImagePainter(attraction.icon),
                    contentDescription = "Icon of ${attraction.name}",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = attraction.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.testTag("PlaceName_${attraction.id}")
                )
                Text(
                    text = "Rating: ${attraction.rating ?: "--"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.testTag("PlaceRating_${attraction.id}")
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Ikona pro uložení
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onSaveClick(attraction) }
                    .testTag("SaveButton_${attraction.id}")
            ) {
                Image(
                    painter = painterResource(id = R.drawable.add), // Vložte svůj zdroj pro ikonu
                    contentDescription = "Save ${attraction.name}",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}
