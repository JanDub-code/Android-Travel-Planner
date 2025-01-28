package cz.pef.TravelPlanner

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import cz.pef.TravelPlanner.AttractionDetails_MOCK_DATA.MockAttractionDetailsScreen
import cz.pef.TravelPlanner.AttractionDetails_MOCK_DATA.MockAttractionDetailsViewModel
import org.junit.Rule
import org.junit.Test

class AttractionDetailsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testTravelStyleSelectionAndConfirm() {
        val mockViewModel = MockAttractionDetailsViewModel()
        var confirmedStyle: String? = null

        composeRule.setContent {
            MockAttractionDetailsScreen(
                viewModel = mockViewModel,
                onConfirmClick = { style -> confirmedStyle = style }
            )
        }

        // Check initial state
        composeRule.onNodeWithTag("RadioButton_DRIVING").assertIsSelected()

        // Select WALKING
        composeRule.onNodeWithTag("RadioButton_WALKING").performClick()
        composeRule.onNodeWithTag("RadioButton_WALKING").assertIsSelected()

        // Select TRANSIT
        composeRule.onNodeWithTag("RadioButton_TRANSIT").performClick()
        composeRule.onNodeWithTag("RadioButton_TRANSIT").assertIsSelected()

        // Confirm selection
        composeRule.onNodeWithTag("ConfirmButton").performClick()
        assert(confirmedStyle == "TRANSIT")
    }
    @Test
    fun testConfirmNavigationToAttractionPlan() {
        val mockViewModel = MockAttractionDetailsViewModel()
        val mockNavigation = MockNavigationRouter() // Mockovaná navigace

        composeRule.setContent {
            MockAttractionDetailsScreen(
                viewModel = mockViewModel,
                onConfirmClick = { selectedTravelStyle ->
                    mockNavigation.navigateToAttractionPlanScreen(1L) // Simulace navigace
                }
            )
        }

        // Ověření výchozího stavu
        composeRule.onNodeWithTag("RadioButton_DRIVING").assertIsSelected()

        // Kliknutí na tlačítko Confirm
        composeRule.onNodeWithTag("ConfirmButton").performClick()

        // Ověření, že navigace byla provedena
        assert(mockNavigation.lastNavigatedScreen == "AttractionPlanScreen")
        assert(mockNavigation.lastNavigatedPlaceId == 1L)
    }
}



class MockNavigationRouter {
    var lastNavigatedScreen: String? = null
    var lastNavigatedPlaceId: Long? = null

    fun navigateToAttractionPlanScreen(placeId: Long) {
        lastNavigatedScreen = "AttractionPlanScreen"
        lastNavigatedPlaceId = placeId
    }
}
