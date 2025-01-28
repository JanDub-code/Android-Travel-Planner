package cz.pef.TravelPlanner.Settings_profile_MOCK_DATA


import cz.pef.TravelPlanner.models.UserSettings

sealed class MockSettingsProfileUiState  {
    class ScreenDataChangedMock(val mockData: MockSettingsProfileData) : MockSettingsProfileUiState()
    class LoadingMock : MockSettingsProfileUiState()
    class SuccessMock(val mockSettings: List<UserSettings>) : MockSettingsProfileUiState()
    class ErrorMock(val mockErrorMessage: String) : MockSettingsProfileUiState()
}
