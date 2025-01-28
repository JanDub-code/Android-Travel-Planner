package cz.pef.TravelPlanner.ui.screens.select_user_location

import cz.pef.TravelPlanner.models.UserSettings
import cz.pef.TravelPlanner.ui.screens.settings_proflle.SettingsProfileData
import cz.pef.TravelPlanner.ui.screens.settings_proflle.SettingsProfileUiState

sealed class SelectLocationUIState {
    class ScreenDataChanged(val data: SelectLocationData): SelectLocationUIState()
    class Loading : SelectLocationUIState()
    class Success(val settings: List<UserSettings>) : SelectLocationUIState()
    class Error(val errorMessage: String) : SelectLocationUIState()
}