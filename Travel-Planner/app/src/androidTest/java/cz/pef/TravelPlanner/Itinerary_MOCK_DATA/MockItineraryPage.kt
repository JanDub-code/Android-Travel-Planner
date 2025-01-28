package cz.pef.TravelPlanner.Itinerary_MOCK_DATA

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
import cz.pef.TravelPlanner.BuildConfig.API_KEY
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.navigation.INavigationRouter
import cz.pef.TravelPlanner.ui.elements.BaseScreen
import cz.pef.TravelPlanner.models.SavedPlaceEntity

@Composable
fun MockItineraryScreen(
    navigation: INavigationRouter,
    selectedIndex: Int = 1 // Index for selection
) {
    val viewModel: MockItineraryViewModel = hiltViewModel() // Initialize ViewModel
    val savedPlaces by viewModel.savedPlaces.collectAsState()

    BaseScreen(
        topBarText = stringResource(id = R.string.itinerary),
        showLoading = false,
        navigationRouter = navigation,
        selectedIndex = selectedIndex
    ) {
        MockItineraryScreenContent(
            savedPlaces = savedPlaces,
            onDeleteClick = { place -> viewModel.deletePlace(place) },
            onNavigateClick = { placeId -> navigation.navigateToAttractionDetailScreen(placeId) }
        )
    }
}

@Composable
fun MockItineraryScreenContent(
    savedPlaces: List<SavedPlaceEntity>,
    onDeleteClick: (SavedPlaceEntity) -> Unit,
    onNavigateClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("ItineraryLazyColumn") // Tag for the list
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
            .testTag("ItineraryCard_${place.id}") // Tag for the entire card
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image Box
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Gray)
                    .testTag("PlaceImage_${place.id}") // Tag for the image
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

            // Place Information
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.testTag("PlaceName_${place.id}") // Tag for the name
                )
                Text(
                    text = "Rating: ${place.rating ?: "--"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.testTag("PlaceRating_${place.id}") // Tag for the rating
                )
                Text(
                    text = place.vicinity ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.testTag("PlaceVicinity_${place.id}") // Tag for the vicinity
                )
                // Display user notes if available
                place.notes?.let { notes ->
                    Text(
                        text = "Notes: $notes",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray,
                        modifier = Modifier.testTag("PlaceNotes_${place.id}") // Tag for notes
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Navigate Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onNavigateClick(place.id.toLong()) }
                    .testTag("NavigateButton_${place.id}") // Tag for navigate button
            ) {
                Image(
                    painter = painterResource(id = R.drawable.travel), // Replace with the correct icon
                    contentDescription = "Navigate to ${place.name}",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Delete Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onDeleteClick(place) }
                    .testTag("DeleteButton_${place.id}") // Tag for delete button
            ) {
                Image(
                    painter = painterResource(id = R.drawable.delete), // Replace with the correct icon
                    contentDescription = "Delete ${place.name}",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}
