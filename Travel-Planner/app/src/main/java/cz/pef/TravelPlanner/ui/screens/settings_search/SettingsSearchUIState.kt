package cz.pef.TravelPlanner.ui.screens.settings_search

import cz.pef.TravelPlanner.models.UserSettings
import cz.pef.TravelPlanner.ui.screens.settings_proflle.SettingsProfileData
import cz.pef.TravelPlanner.ui.screens.settings_proflle.SettingsProfileUiState

sealed class SettingsSearchUIState {
    class ScreenDataChanged(val data: SettingsSearchData): SettingsSearchUIState()
    class Loading : SettingsSearchUIState()
    class Success(val settings: List<UserSettings>) : SettingsSearchUIState()
    class Error(val errorMessage: String) : SettingsSearchUIState()
}