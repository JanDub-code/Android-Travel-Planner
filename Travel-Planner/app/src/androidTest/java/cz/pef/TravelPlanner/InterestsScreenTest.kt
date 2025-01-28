package cz.pef.TravelPlanner
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import cz.pef.TravelPlanner.MOCK_SETUP.MockNavigationRouter
import cz.pef.TravelPlanner.MOCK_SETUP.MockTravelRepository
import cz.pef.TravelPlanner.Settings_interests_MOCK_DATA.MockInterestsScreen
import cz.pef.TravelPlanner.Settings_interests_MOCK_DATA.MockInterestsViewModel
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test


class InterestsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun test_toggleInterest() {
        // Vytvoření Mock Repository
        val mockRepository = MockTravelRepository()

        // Vytvoření Mock ViewModelu
        val mockViewModel = MockInterestsViewModel(mockRepository)

        // Mock navigační router
        val mockNavigation = MockNavigationRouter()

        // Nastavení obrazovky s injektovaným ViewModelem
        composeRule.setContent {
            MockInterestsScreen(
                navigation = mockNavigation,
                viewModel = mockViewModel,
                selectedIndex = 3
            )
        }

        // Vybereme zájem "Nature"
        val interest = "Nature"
        composeRule.onNodeWithTag(interest).performClick()

        // Ověření, že "Nature" je vybrán ve ViewModelu
        assertTrue(mockViewModel.selectedInterests.value.contains(interest))

        // Ověření, že "Nature" je zobrazen jako vybraný chip
        composeRule.onNodeWithTag("selectedInterestChip_$interest").assertTextContains(interest)
    }

    @Test
    fun test_addInterestFromPhoto() {
        // Vytvoření Mock Repository
        val mockRepository = MockTravelRepository()

        // Vytvoření Mock ViewModelu
        val mockViewModel = MockInterestsViewModel(mockRepository)

        // Mock navigační router
        val mockNavigation = MockNavigationRouter()

        // Nastavení obrazovky s injektovaným ViewModelem
        composeRule.setContent {
            MockInterestsScreen(
                navigation = mockNavigation,
                viewModel = mockViewModel,
                selectedIndex = 3
            )
        }

        // Kliknutí na tlačítko "Add Interests from Photo"
        composeRule.onNodeWithTag("addInterestsFromPhotoButton").performClick()

        // Ověření, že navigace na Photo Scanner byla volána
        assertTrue(mockNavigation.navigateToPhotoScannerCalled)
    }

    @Test
    fun test_selectInterestChip() {
        // Vytvoření Mock Repository
        val mockRepository = MockTravelRepository()

        // Vytvoření Mock ViewModelu
        val mockViewModel = MockInterestsViewModel(mockRepository)

        // Mock navigační router
        val mockNavigation = MockNavigationRouter()

        // Nastavení obrazovky s injektovaným ViewModelem
        composeRule.setContent {
            MockInterestsScreen(
                navigation = mockNavigation,
                viewModel = mockViewModel,
                selectedIndex = 3
            )
        }

        // Přidáme zájem "Nature" a ověříme jeho zobrazení
        val interest = "Nature"
        composeRule.onNodeWithTag(interest).performClick()
        composeRule.onNodeWithTag("selectedInterestChip_$interest").assertTextContains(interest)

        // Ověření, že ViewModel byl aktualizován
        assertTrue(mockViewModel.selectedInterests.value.contains(interest))
    }

    @Test
    fun test_backButton() {
        // Vytvoření Mock Repository
        val mockRepository = MockTravelRepository()

        // Vytvoření Mock ViewModelu
        val mockViewModel = MockInterestsViewModel(mockRepository)

        // Mock navigační router
        val mockNavigation = MockNavigationRouter()

        // Nastavení obrazovky s injektovaným ViewModelem
        composeRule.setContent {
            MockInterestsScreen(
                navigation = mockNavigation,
                viewModel = mockViewModel,
                selectedIndex = 3
            )
        }

        // Kliknutí na tlačítko "Back"
        composeRule.onNodeWithTag("Back").performClick()

        // Ověření, že navigace zpět byla volána
        assertTrue(mockNavigation.navigateToSettingsCalled)
    }

    @Test
    fun test_saveSettings() {
        // Vytvoření Mock Repository
        val mockRepository = MockTravelRepository()

        // Vytvoření Mock ViewModelu
        val mockViewModel = MockInterestsViewModel(mockRepository)

        // Mock navigační router
        val mockNavigation = MockNavigationRouter()

        // Nastavení obrazovky s injektovaným ViewModelem
        composeRule.setContent {
            MockInterestsScreen(
                navigation = mockNavigation,
                viewModel = mockViewModel,
                selectedIndex = 3
            )
        }

        // Vybereme více zájmů
        val interests = listOf("Nature", "Culture")
        interests.forEach { interest ->
            composeRule.onNodeWithTag(interest).performClick()
        }

        // Ověření, že ViewModel byl aktualizován
        assertTrue(mockViewModel.selectedInterests.value.containsAll(interests))

        // Kliknutí na tlačítko "Save Settings"
        composeRule.onNodeWithTag("saveSettingsButton").performClick()

        assertTrue(mockViewModel.saveCalled)
    }
}
