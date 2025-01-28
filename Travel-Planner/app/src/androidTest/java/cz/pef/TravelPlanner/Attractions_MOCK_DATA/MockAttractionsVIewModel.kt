package cz.pef.TravelPlanner.Attractions_MOCK_DATA

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.TravelPlanner.database.ITravelRepository
import cz.pef.TravelPlanner.models.PlaceResultEntity
import cz.pef.TravelPlanner.models.SavedPlaceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MockAttractionsViewModel () : ViewModel() {

    private val _savedPlaces = MutableStateFlow<List<SavedPlaceEntity>>(emptyList())
    val savedPlaces: StateFlow<List<SavedPlaceEntity>> = _savedPlaces.asStateFlow()
    private val _uiState = MutableStateFlow(MockAttractionUIState())
    val uiState: StateFlow<MockAttractionUIState> = _uiState.asStateFlow()

    init {
        loadAttractions()
    }
    private fun loadAttractions() {
        viewModelScope.launch {
            val attractions = listOf(
                PlaceResultEntity(
                    id = 1,
                    name = "Eiffel Tower",
                    photo_reference = "photo1",
                    rating = 4.7,
                    vicinity = "Paris",
                    lat = 48.8584,
                    lng = 2.2945,
                    icon = "eiffel_icon"
                ),
                PlaceResultEntity(
                    id = 2,
                    name = "Statue of Liberty",
                    photo_reference = "photo2",
                    rating = 4.6,
                    vicinity = "New York",
                    lat = 40.6892,
                    lng = -74.0445,
                    icon = "liberty_icon"
                ),
                PlaceResultEntity(
                    id = 3,
                    name = "Great Wall",
                    photo_reference = "photo3",
                    rating = 4.5,
                    vicinity = "China",
                    lat = 40.4319,
                    lng = 116.5704,
                    icon = "greatwall_icon"
                )
            )

            _uiState.value = MockAttractionUIState(
                isLoading = false,
                attractions = attractions
            )
        }

    }
}
