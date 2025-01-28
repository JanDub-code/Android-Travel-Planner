package cz.pef.TravelPlanner.ui.screens.Attraction_Detail
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import cz.pef.TravelPlanner.BuildConfig.API_KEY
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.navigation.INavigationRouter
import cz.pef.TravelPlanner.ui.elements.BaseScreen
import cz.pef.TravelPlanner.models.PlaceResultEntity
import cz.pef.TravelPlanner.models.SavedPlaceEntity
import cz.pef.TravelPlanner.ui.screens.attractions.AttractionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttractionDetailsScreen(
    placeId: Long,
    navigation: INavigationRouter,
    onBackClick: () -> Unit,
    viewModel: AttractionDetailsViewModel = hiltViewModel()
) {
    // Načtení dat podle ID
    LaunchedEffect(placeId) {
        viewModel.loadPlaceById(placeId)
        viewModel.initializeCustomMarker()
    }
    val customtravel by viewModel.customTravelStyle.collectAsState()
    val place by viewModel.place.collectAsState()
    val directions by viewModel.directions.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.attraction_details),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { onBackClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.back),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2563EB))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (place != null) {
                // Zobrazení dat uloženého místa
                Text(
                    text = place!!.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Obrázek
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.Gray)
                ) {
                    val photoReference = place!!.photo_reference
                    if (photoReference != null) {
                        val photoUrl =
                            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$photoReference&key=${API_KEY}"
                        Image(
                            painter = rememberAsyncImagePainter(photoUrl),
                            contentDescription = "Photo of ${place!!.name}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Další detaily
                Text(
                    text = stringResource(id = R.string.rating)+" ${place!!.rating ?: "--"}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(id = R.string.address)+"${place!!.vicinity ?: "Not available"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                val options = listOf("DRIVING", "WALKING", "TRANSIT")
                val zobrazeneoptins = listOf(stringResource(id = R.string.driving),stringResource(id = R.string.walking),stringResource(id = R.string.transit))
                var selectedOption by remember { mutableStateOf(options[0]) }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(id = R.string.select_your_transport_mode),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Spojení obou seznamů do seznamu párů (option, displayOption)
                    options.zip(zobrazeneoptins).forEach { (option, displayOption) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { // Umožňuje kliknutí na celý řádek
                                    selectedOption = option
                                    viewModel.setTravelStyle(selectedOption)
                                }
                        ) {
                            RadioButton(
                                selected = (option == selectedOption),
                                onClick = {
                                    selectedOption = option
                                    viewModel.setTravelStyle(selectedOption)
                                }
                            )
                            Text(
                                text = displayOption,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Potvrzovací tlačítko
                    Button(
                        onClick = { navigation.navigateToAttractionPlanScreen(placeId) }, // Volání potvrzovací akce
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.confirm))
                    }
                }
            } else {
                // Zobrazení načítání nebo chyby
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}