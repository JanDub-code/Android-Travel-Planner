package cz.pef.TravelPlanner.ui.screens.attractions

import cz.pef.TravelPlanner.models.PlaceResultEntity

data class AttractionUIState(
    val isLoading: Boolean = true,
    val attractions: List<PlaceResultEntity> = emptyList(),
    val error: String? = null
)
