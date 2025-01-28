package cz.pef.TravelPlanner.Attractions_MOCK_DATA

import cz.pef.TravelPlanner.models.PlaceResultEntity

data class MockAttractionUIState(
    val isLoading: Boolean = true,
    val attractions: List<PlaceResultEntity> = emptyList(),
    val error: String? = null
)
