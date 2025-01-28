package cz.pef.TravelPlanner.Settings_profile_MOCK_DATA

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.TravelPlanner.models.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MockSettingsProfileViewModel : ViewModel() {

    private val _settingsProfileUIState: MutableStateFlow<MockSettingsProfileUiState> = MutableStateFlow(
        MockSettingsProfileUiState.LoadingMock()
    )
    val settingsProfileUIState: StateFlow<MockSettingsProfileUiState> get() = _settingsProfileUIState

    val mockData = MockSettingsProfileData(
        userSettings = UserSettings(
            id = 1,
            username = "TestUser",
            email = "test@example.com",
            maxAttractionsDisplayed = 5,
            maxLocationDistance = 10
        )
    )

    var username: String = mockData.userSettings.username
    var email: String = mockData.userSettings.email
    var saveCalled: Boolean = false

    init {
        _settingsProfileUIState.update {
            MockSettingsProfileUiState.ScreenDataChangedMock(mockData)
        }
    }

    fun loadSettings() {
        viewModelScope.launch {
            _settingsProfileUIState.update {
                MockSettingsProfileUiState.ScreenDataChangedMock(mockData)
            }
        }
    }

    fun usernameChanged(newUsername: String) {
        username = newUsername
        mockData.userSettings.username = newUsername
        _settingsProfileUIState.update {
            MockSettingsProfileUiState.ScreenDataChangedMock(mockData)
        }
    }

    fun emailChanged(newEmail: String) {
        email = newEmail
        mockData.userSettings.email = newEmail
        _settingsProfileUIState.update {
            MockSettingsProfileUiState.ScreenDataChangedMock(mockData)
        }
    }

    fun saveSettings() {
        saveCalled = true
    }
}
