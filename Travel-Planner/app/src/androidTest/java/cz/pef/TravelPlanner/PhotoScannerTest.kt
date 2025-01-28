package cz.pef.TravelPlanner

import android.net.Uri
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import cz.pef.TravelPlanner.MOCK_SETUP.MockNavigationRouter
import cz.pef.TravelPlanner.Photo_scanner_MOCK_DATA.MockPhotoScannerScreen
import cz.pef.TravelPlanner.Photo_scanner_MOCK_DATA.MockPhotoScannerViewModel
import cz.pef.TravelPlanner.R
import cz.pef.TravelPlanner.Settings_profile_MOCK_DATA.MockSettingsProfileViewModel
import cz.pef.TravelPlanner.Settings_profile_MOCK_DATA.MockSettingsScreenContent
import cz.pef.TravelPlanner.ui.activity.MainActivity
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class MockPhotoScannerScreenTest {

    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun selectPictureButton_isDisplayedAndClickable() {
        val mockNavigation = MockNavigationRouter()
        val mockViewModel = MockPhotoScannerViewModel()

        // Nastavení obrazovky s mock routerem a mock view model
        composeRule.setContent {
            MockPhotoScannerScreen(
                navigation = mockNavigation
            )
        }

        // Najít tlačítko pro výběr obrázku pomocí test tagu
        val selectPictureButton = composeRule.onNodeWithTag("SelectPictureButton")
            .assertIsDisplayed()

        // Kliknout na tlačítko
        selectPictureButton.performClick()


    }

    @Test
    fun backButton_navigatesBack() {
        val mockNavigation = MockNavigationRouter()
        val mockViewModel = MockPhotoScannerViewModel()

        // Nastavení obrazovky s mock routerem a mock view model
        composeRule.setContent {
            MockPhotoScannerScreen(
                navigation = mockNavigation
            )
        }

        // Najít tlačítko zpět pomocí test tagu
        val backButton = composeRule.onNodeWithTag("BackButton")
            .assertIsDisplayed()

        // Kliknout na tlačítko zpět
        backButton.performClick()

        // Ověřit, že metoda navitageToInterests byla zavolána
        assertTrue(mockNavigation.navigateToInterestsCalled)
    }
}