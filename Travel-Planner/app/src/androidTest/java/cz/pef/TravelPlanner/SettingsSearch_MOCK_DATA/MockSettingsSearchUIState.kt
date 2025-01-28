package cz.pef.TravelPlanner.SettingsSearch_MOCK_DATA

import cz.pef.TravelPlanner.models.UserSettings

sealed class MockSettingsSearchUIState {
    class ScreenDataChangedMock(val mockData: MockSettingsSearchData) : MockSettingsSearchUIState()
    class LoadingMock : MockSettingsSearchUIState()
    class SuccessMock(val mockSettings: List<UserSettings>) : MockSettingsSearchUIState()
    class ErrorMock(val mockErrorMessage: String) : MockSettingsSearchUIState()
}