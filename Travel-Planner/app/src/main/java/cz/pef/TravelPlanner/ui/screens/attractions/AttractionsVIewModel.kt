package cz.pef.TravelPlanner.ui.screens.attractions

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

@HiltViewModel
class AttractionsViewModel @Inject constructor(
    private val repository: ITravelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AttractionUIState())
    val uiState: StateFlow<AttractionUIState> = _uiState.asStateFlow()

    init {
        loadAttractions()
    }

    private fun loadAttractions() {
        viewModelScope.launch {
            try {
                val attractions = repository.getAllPlaces()
                _uiState.value = AttractionUIState(
                    isLoading = false,
                    attractions = attractions
                )
            } catch (e: Exception) {
                _uiState.value = AttractionUIState(
                    isLoading = false,
                    error = e.message ?: "An unknown error occurred"
                )
            }
        }
    }
    fun savePlace(place: PlaceResultEntity) {
        viewModelScope.launch {
            try {
                repository.insertSavedPlace(
                    SavedPlaceEntity(
                        name = place.name,
                        vicinity = place.vicinity,
                        rating = place.rating,
                        lat = place.lat,
                        lng = place.lng,
                        photo_reference = place.photo_reference,
                        icon = place.icon,
                        notes = null
                    )
                )
            } catch (e: Exception) {
                // Můžete přidat chybové hlášení, pokud uložení selže
            }
        }
    }

}
