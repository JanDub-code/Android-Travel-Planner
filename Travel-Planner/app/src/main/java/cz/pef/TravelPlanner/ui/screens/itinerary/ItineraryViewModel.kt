package cz.pef.TravelPlanner.ui.screens.itinerary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.TravelPlanner.database.ITravelRepository
import cz.pef.TravelPlanner.models.SavedPlaceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItineraryViewModel @Inject constructor(
    private val repository: ITravelRepository
) : ViewModel() {

    private val _savedPlaces = MutableStateFlow<List<SavedPlaceEntity>>(emptyList())
    val savedPlaces: StateFlow<List<SavedPlaceEntity>> = _savedPlaces.asStateFlow()

    init {
        loadSavedPlaces()
    }

    private fun loadSavedPlaces() {
        viewModelScope.launch {
            _savedPlaces.value = repository.getSavedPlaces()
        }
    }

    fun deletePlace(place: SavedPlaceEntity) {
        viewModelScope.launch {
            repository.deleteSavedPlace(place)
            loadSavedPlaces() // Aktualizace seznamu po smazání
        }
    }
}
