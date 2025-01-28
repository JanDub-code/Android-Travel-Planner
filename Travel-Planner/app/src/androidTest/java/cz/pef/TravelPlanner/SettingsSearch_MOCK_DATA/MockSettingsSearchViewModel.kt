package cz.pef.TravelPlanner.SettingsSearch_MOCK_DATAimport

import androidx.lifecycle.ViewModel
import cz.pef.TravelPlanner.SettingsSearch_MOCK_DATA.MockSettingsSearchUIState

import androidx.lifecycle.viewModelScope
import cz.pef.TravelPlanner.SettingsSearch_MOCK_DATA.MockSettingsSearchData
import cz.pef.TravelPlanner.models.UserSettings
import cz.pef.TravelPlanner.ui.screens.settings_search.SettingsSearchData
import cz.pef.TravelPlanner.ui.screens.settings_search.SettingsSearchUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MockSettingsSearchViewModel : ViewModel() {

    private val _settingsSearchUIState: MutableStateFlow<MockSettingsSearchUIState> = MutableStateFlow(
        MockSettingsSearchUIState.LoadingMock()
    )
    val settingsSearchUIState: StateFlow<MockSettingsSearchUIState> get() = _settingsSearchUIState

    val mockData = MockSettingsSearchData(
        userSettings = UserSettings(
            id = 1,
            username = "TestUser",
            email = "test@example.com",
            maxAttractionsDisplayed = 5,
            maxLocationDistance = 15
        )
    )

    private val _customMarkerEnabled = MutableStateFlow(false)
    val customMarkerEnabled: StateFlow<Boolean> get() = _customMarkerEnabled

    var saveCalled: Boolean = false
    var navigateToLocationSettingsCalled: Boolean = false
    var navigateBackCalled: Boolean = false

    init {
        _settingsSearchUIState.update {
            MockSettingsSearchUIState.ScreenDataChangedMock(mockData)
        }
    }

    fun loadSettings() {
        viewModelScope.launch {
            _settingsSearchUIState.update {
                MockSettingsSearchUIState.ScreenDataChangedMock(mockData)
            }
        }
    }

    fun attractionChanged(newAttractions: String) {
        val updatedAttractions = newAttractions.toIntOrNull()
        if (updatedAttractions != null) {
            mockData.userSettings.maxAttractionsDisplayed = updatedAttractions
            _settingsSearchUIState.update {
                MockSettingsSearchUIState.ScreenDataChangedMock(mockData)
            }
        }
    }

    fun distanceChanged(newDistance: String) {
        val updatedDistance = newDistance.toIntOrNull()
        if (updatedDistance != null) {
            mockData.userSettings.maxLocationDistance = updatedDistance
            _settingsSearchUIState.update {
                MockSettingsSearchUIState.ScreenDataChangedMock(mockData)
            }
        }
    }

    fun toggleCustomMarker(enabled: Boolean) {
        _customMarkerEnabled.value = enabled
        // Aktualizace stavu bez způsobení nekonečných změn
        _settingsSearchUIState.update {
            MockSettingsSearchUIState.ScreenDataChangedMock(mockData)
        }
    }

    fun saveSettings() {
        saveCalled = true
        // Simulace uložení nastavení
    }

    fun navigateToLocationSettings() {
        navigateToLocationSettingsCalled = true
    }

    fun navigateBack() {
        navigateBackCalled = true
    }

    fun isCustomMarkerEnabled(): Boolean {
        return _customMarkerEnabled.value
    }
}