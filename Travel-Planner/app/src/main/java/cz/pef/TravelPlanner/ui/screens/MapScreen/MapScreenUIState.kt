package cz.pef.TravelPlanner.ui.screens.MapScreen

import cz.pef.TravelPlanner.models.UserSettings

sealed class MapScreenUIState {
    class ScreenDataChanged(val data: MapScreenData): MapScreenUIState()
    class Loading : MapScreenUIState()
    class Success(val settings: List<UserSettings>) : MapScreenUIState()
    class Error(val errorMessage: String) : MapScreenUIState()
}