package cz.pef.TravelPlanner.Itinerary_MOCK_DATA
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.TravelPlanner.MOCK_SETUP.MockTravelRepository
import cz.pef.TravelPlanner.models.SavedPlaceEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MockItineraryViewModel : ViewModel()
{
    private val _savedPlaces = MutableStateFlow<List<SavedPlaceEntity>>(emptyList())
    val savedPlaces: StateFlow<List<SavedPlaceEntity>> = _savedPlaces.asStateFlow()

    init {
        loadSavedPlaces()
    }

    private fun loadSavedPlaces() {
        viewModelScope.launch {
            _savedPlaces.value = listOf(
                SavedPlaceEntity(
                    id = 1,
                    name = "Eiffel Tower",
                    photo_reference = "photo1",
                    rating = 4.7,
                    vicinity = "Paris",
                    lat = 48.8584,
                    lng = 2.2945,
                    icon = "eiffel_icon",
                    notes = "Must visit at night for the lights."
                ),
                SavedPlaceEntity(
                    id = 2,
                    name = "Statue of Liberty",
                    photo_reference = "photo2",
                    rating = 4.6,
                    vicinity = "New York",
                    lat = 40.6892,
                    lng = -74.0445,
                    icon = "liberty_icon",
                    notes = null
                ),
                SavedPlaceEntity(
                    id = 3,
                    name = "Great Wall",
                    photo_reference = "photo3",
                    rating = 4.5,
                    vicinity = "China",
                    lat = 40.4319,
                    lng = 116.5704,
                    icon = "greatwall_icon",
                    notes = "Bring comfortable shoes."
                )
            )
        }
    }

    fun deletePlace(place: SavedPlaceEntity) {
        viewModelScope.launch {
            _savedPlaces.value = _savedPlaces.value.filter { it.id != place.id }
        }
    }

}
