package cz.pef.TravelPlanner.ui.screens.settings_proflle

import cz.pef.TravelPlanner.models.UserSettings

sealed class SettingsProfileUiState {
    class ScreenDataChanged(val data: SettingsProfileData): SettingsProfileUiState()
    class Loading : SettingsProfileUiState()
    class Success(val settings: List<UserSettings>) : SettingsProfileUiState()
    class Error(val errorMessage: String) : SettingsProfileUiState()
}