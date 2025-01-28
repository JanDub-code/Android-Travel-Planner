package cz.pef.TravelPlanner

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import cz.pef.TravelPlanner.MOCK_SETUP.MockNavigationRouter
import cz.pef.TravelPlanner.SettingsSearch_MOCK_DATA.MockSettingsSearch
import cz.pef.TravelPlanner.SettingsSearch_MOCK_DATAimport.MockSettingsSearchViewModel
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingsSearchTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun test_settingsSearch_changeMaxAttractions() {
        // Mock navigační router
        val mockNavigation = MockNavigationRouter()

        // Vytvoření Mock ViewModelu
        val mockViewModel = MockSettingsSearchViewModel()

        // Nastavení obrazovky s injektovaným ViewModelem
        composeRule.setContent {
            MockSettingsSearch(
                navigation = mockNavigation,
                viewModel = mockViewModel,
                selectedIndex = 3
            )
        }

        // Vyčištění textového pole a zadání nového textu
        composeRule.onNodeWithTag("maxAttractionsTextField").performTextReplacement("20")

        // Ověření, že textové pole bylo aktualizováno
        composeRule.onNodeWithTag("maxAttractionsTextField").assertTextContains("20")

        // Ověření, že ViewModel byl aktualizován
        assertTrue(mockViewModel.mockData.userSettings.maxAttractionsDisplayed == 20)
    }

    @Test
    fun test_settingsSearch_changeMaxDistance() {
        // Mock navigační router
        val mockNavigation = MockNavigationRouter()

        // Vytvoření Mock ViewModelu
        val mockViewModel = MockSettingsSearchViewModel()

        // Nastavení obrazovky s injektovaným ViewModelem
        composeRule.setContent {
            MockSettingsSearch(
                navigation = mockNavigation,
                viewModel = mockViewModel,
                selectedIndex = 3
            )
        }

        // Vyčištění textového pole a zadání nového textu
        composeRule.onNodeWithTag("maxDistanceTextField").performTextReplacement("25")

        // Ověření, že textové pole bylo aktualizováno
        composeRule.onNodeWithTag("maxDistanceTextField").assertTextContains("25")

        // Ověření, že ViewModel byl aktualizován
        assertTrue(mockViewModel.mockData.userSettings.maxLocationDistance == 25)
    }

    @Test
    fun test_settingsSearch_toggleCustomMarker() {
        // Mock navigační router
        val mockNavigation = MockNavigationRouter()

        // Vytvoření Mock ViewModelu
        val mockViewModel = MockSettingsSearchViewModel()

        // Nastavení obrazovky s injektovaným ViewModelem
        composeRule.setContent {
            MockSettingsSearch(
                navigation = mockNavigation,
                viewModel = mockViewModel,
                selectedIndex = 3
            )
        }

        // Test interakce s přepínačem pro vlastní markery
        composeRule.onNodeWithTag("customMarkerSwitch").performClick()

        // Ověření, že přepínač byl zapnut
        assertTrue(mockViewModel.isCustomMarkerEnabled())
    }

    @Test
    fun test_settingsSearch_saveSettings() {
        // Mock navigační router
        val mockNavigation = MockNavigationRouter()

        // Vytvoření Mock ViewModelu
        val mockViewModel = MockSettingsSearchViewModel()

        // Nastavení obrazovky s injektovaným ViewModelem
        composeRule.setContent {
            MockSettingsSearch(
                navigation = mockNavigation,
                viewModel = mockViewModel,
                selectedIndex = 3
            )
        }

        // Vyčištění textových polí a zadání nového textu
        composeRule.onNodeWithTag("maxAttractionsTextField").performTextReplacement("30")
        composeRule.onNodeWithTag("maxDistanceTextField").performTextReplacement("50")

        // Ověření, že ViewModel byl aktualizován
        assertTrue(mockViewModel.mockData.userSettings.maxAttractionsDisplayed == 30)
        assertTrue(mockViewModel.mockData.userSettings.maxLocationDistance == 50)

        // Test kliknutí na tlačítko "Save Settings"
        composeRule.onNodeWithTag("saveSettingsButton").performClick()

        // Ověření, že saveSettings byl volán a navigace zpět byla volána
        assertTrue(mockViewModel.saveCalled)
    }

    @Test
    fun test_settingsSearch_selectLocation() {
        // Mock navigační router
        val mockNavigation = MockNavigationRouter()

        // Vytvoření Mock ViewModelu
        val mockViewModel = MockSettingsSearchViewModel()

        // Nastavení obrazovky s injektovaným ViewModelem
        composeRule.setContent {
            MockSettingsSearch(
                navigation = mockNavigation,
                viewModel = mockViewModel,
                selectedIndex = 3
            )
        }

        // Test kliknutí na tlačítko "Select Location"
        composeRule.onNodeWithTag("selectLocationButton").performClick()

        // Ověření, že navigace na nastavení lokace byla volána
        assertTrue(mockViewModel.navigateToLocationSettingsCalled)
    }

    @Test
    fun test_settingsSearch_backButton() {
        // Mock navigační router
        val mockNavigation = MockNavigationRouter()

        // Vytvoření Mock ViewModelu
        val mockViewModel = MockSettingsSearchViewModel()

        // Nastavení obrazovky s injektovaným ViewModelem
        composeRule.setContent {
            MockSettingsSearch(
                navigation = mockNavigation,
                viewModel = mockViewModel,
                selectedIndex = 3
            )
        }

        // Test kliknutí na tlačítko "Back"
        composeRule.onNodeWithText("Back").performClick()

        // Ověření, že navigace zpět byla volána
        assertTrue(mockViewModel.navigateBackCalled)
    }
}
