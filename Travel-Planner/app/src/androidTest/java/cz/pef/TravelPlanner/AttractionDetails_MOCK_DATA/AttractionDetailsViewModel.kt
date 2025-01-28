package cz.pef.TravelPlanner.AttractionDetails_MOCK_DATA

import androidx.lifecycle.ViewModel
import cz.pef.TravelPlanner.models.SavedPlaceEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockAttractionDetailsViewModel : ViewModel() {
    private val _place = MutableStateFlow(
        SavedPlaceEntity(
            id = 1,
            name = "Mock Place",
            photo_reference = "mock_photo",
            rating = 4.5,
            vicinity = "Mock Vicinity",
            lat = 0.0,
            lng = 0.0,
            icon = "mock_icon",
            notes = "Mock Notes"
        )
    )
    val place: StateFlow<SavedPlaceEntity?> = _place.asStateFlow()

    private val _customTravelStyle = MutableStateFlow("DRIVING")
    val customTravelStyle: StateFlow<String> = _customTravelStyle.asStateFlow()

    fun setTravelStyle(style: String) {
        _customTravelStyle.value = style
    }
}
