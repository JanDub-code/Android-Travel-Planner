package cz.pef.TravelPlanner

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import cz.pef.TravelPlanner.MOCK_SETUP.MockNavigationRouter
import cz.pef.TravelPlanner.Settings_profile_MOCK_DATA.MockSettingsProfileViewModel
import cz.pef.TravelPlanner.Settings_profile_MOCK_DATA.MockSettingsScreenContent
import org.junit.Rule
import org.junit.Test

class SettingsProfileTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun test_settingsProfile_saveChanges() {
        // Mock navigační router
        val mockNavigation = MockNavigationRouter()

        // Mock view model
        val mockViewModel = MockSettingsProfileViewModel()

        // Nastavení obrazovky
        composeRule.setContent {
            MockSettingsScreenContent(
                navigation = mockNavigation,
                viewModel = mockViewModel,
                data = mockViewModel.mockData
            )
        }

        // Test interakce s textovým polem pro username
        composeRule.onNodeWithText("Enter your username").performTextInput("NewUsername")
        assert(mockViewModel.username == "NewUsername")

        // Test interakce s textovým polem pro email
        composeRule.onNodeWithText("Enter your email").performTextInput("test@example.com")
        assert(mockViewModel.email == "test@example.com")

        // Test kliknutí na tlačítko "Save Changes"
        composeRule.onNodeWithText("Save Changes").performClick()
        assert(mockViewModel.saveCalled)
        assert(mockNavigation.returnBackCalled)
    }

    @Test
    fun test_settingsProfile_backButton() {
        // Mock navigační router
        val mockNavigation = MockNavigationRouter()

        // Mock view model
        val mockViewModel = MockSettingsProfileViewModel()

        // Nastavení obrazovky
        composeRule.setContent {
            MockSettingsScreenContent(
                navigation = mockNavigation,
                viewModel = mockViewModel,
                data = mockViewModel.mockData
            )
        }

        // Test kliknutí na tlačítko "Back"
        composeRule.onNodeWithText("Back").performClick()
        assert(mockNavigation.returnBackCalled)
    }
}
