package cz.pef.TravelPlanner.ui.screens.itinerary

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import cz.pef.TravelPlanner.BuildConfig.API_KEY
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.navigation.INavigationRouter
import cz.pef.TravelPlanner.ui.elements.BaseScreen
import cz.pef.TravelPlanner.models.SavedPlaceEntity

@Composable
fun ItineraryScreen(
    navigation: INavigationRouter,
    selectedIndex: Int = 1 // Index pro nastavení
) {
    val viewModel: ItineraryViewModel = hiltViewModel() // Inicializace ViewModelu
    val savedPlaces by viewModel.savedPlaces.collectAsState()

    BaseScreen(
        topBarText = stringResource(id = R.string.itinerary),
        showLoading = false,
        navigationRouter = navigation,
        selectedIndex = selectedIndex
    ) {
        ItineraryScreenContent(
            savedPlaces = savedPlaces,
            onDeleteClick = { place -> viewModel.deletePlace(place) },
            onNavigateClick = {place -> navigation.navigateToAttractionDetailScreen(place)}
        )
    }
}

@Composable
fun ItineraryScreenContent(
    savedPlaces: List<SavedPlaceEntity>,
    onDeleteClick: (SavedPlaceEntity) -> Unit,
    onNavigateClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(savedPlaces) { place ->
            ItineraryCard(
                place = place,
                onDeleteClick = onDeleteClick,
                onNavigateClick = onNavigateClick
            )
        }
    }
}

@Composable
fun ItineraryCard(
    place: SavedPlaceEntity,
    onDeleteClick: (SavedPlaceEntity) -> Unit,
    onNavigateClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Box s obrázkem fotky
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Gray)
            ) {
                val photoReference = place.photo_reference
                if (photoReference != null) {
                    val photoUrl =
                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=80&photoreference=$photoReference&key=${API_KEY}"
                    Image(
                        painter = rememberAsyncImagePainter(photoUrl),
                        contentDescription = "Photo of ${place.name}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Informace o místě
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(id = R.string.rating) + " ${place.rating ?: "--"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = place.vicinity ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Tlačítko pro navigaci na detail
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onNavigateClick(place.id.toLong()) }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.travel), // Nahraďte správnou ikonou
                    contentDescription = "Navigate to ${place.name}",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onDeleteClick(place) }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.delete), // Nahraďte správnou ikonou
                    contentDescription = "Delete ${place.name}",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}
