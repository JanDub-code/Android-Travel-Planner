package cz.pef.TravelPlanner

import cz.pef.TravelPlanner.ui.screens.settings.SettingsScreenContent

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cz.pef.TravelPlanner.MOCK_SETUP.MockNavigationRouter
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun test_settingsOptions_clickNavigation() {
        // Mock navigační router
        val mockNavigation = MockNavigationRouter()


        // Vytvoření obsahu obrazovky
        composeRule.setContent {
            SettingsScreenContent(navigation = mockNavigation)
        }

        // Test kliknutí na možnost "Interests"
        composeRule.onNodeWithText("Interests").performClick()
        assert(mockNavigation.navigateToInterestsCalled)

        // Test kliknutí na možnost "Profile"
        composeRule.onNodeWithText("Profile").performClick()
        assert(mockNavigation.navigateToProfileCalled)

        // Test kliknutí na možnost "Search"
        composeRule.onNodeWithText("Search").performClick()
        assert(mockNavigation.navigateToSearchCalled)
    }
}
