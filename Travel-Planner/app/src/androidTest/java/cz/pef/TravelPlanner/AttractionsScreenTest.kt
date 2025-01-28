package cz.pef.TravelPlanner

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import cz.pef.TravelPlanner.Attractions_MOCK_DATA.MockAttractionsScreen
import cz.pef.TravelPlanner.MOCK_SETUP.MockNavigationRouter
import cz.pef.TravelPlanner.models.PlaceResultEntity
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class AttractionsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun attractionsScreen_displaysAttractions() {
        val mockNavigation = MockNavigationRouter()

        composeRule.setContent {
            MockAttractionsScreen(
                navigation = mockNavigation
            )
        }

        val mockPlaces = listOf(
            PlaceResultEntity(
                id = 1,
                name = "Eiffel Tower",
                photo_reference = "photo1",
                rating = 4.7,
                vicinity = "Paris",
                lat = 48.8584,
                lng = 2.2945,
                icon = "eiffel_icon"
            ),
            PlaceResultEntity(
                id = 2,
                name = "Statue of Liberty",
                photo_reference = "photo2",
                rating = 4.6,
                vicinity = "New York",
                lat = 40.6892,
                lng = -74.0445,
                icon = "liberty_icon"
            ),
            PlaceResultEntity(
                id = 3,
                name = "Great Wall",
                photo_reference = "photo3",
                rating = 4.5,
                vicinity = "China",
                lat = 40.4319,
                lng = 116.5704,
                icon = "greatwall_icon"
            )
        )

        mockPlaces.forEach { place ->
            // Check if the place name is displayed
            composeRule.onNodeWithTag("AttractionCard_${place.id}")
                .assertIsDisplayed()

            // Check if the place rating is displayed
            composeRule.onNodeWithTag("AttractionCard_${place.id}")
                .assertIsDisplayed()

            // Check if the place vicinity is displayed
            composeRule.onNodeWithTag("AttractionCard_${place.id}")
                .assertIsDisplayed()
        }
    }

    @Test
    fun attractionsScreen_saveButton_clickSavesPlace() {
        val mockNavigation = MockNavigationRouter()

        composeRule.setContent {
            MockAttractionsScreen(
                navigation = mockNavigation
            )
        }

        val firstPlaceId = 1
        composeRule.onNodeWithTag("SaveButton_${firstPlaceId}")
            .assertIsDisplayed()
            .performClick()

        // You may add further assertions to check the repository or UI state
    }

    @Test
    fun attractionsScreen_clickNavigateButton_navigatesToDetail() {
        val mockNavigation = MockNavigationRouter()

        composeRule.setContent {
            MockAttractionsScreen(
                navigation = mockNavigation
            )
        }

        // Click navigate button for the second place
        val secondPlaceId = 2
        composeRule.onNodeWithTag("AttractionCard_${secondPlaceId}")
            .assertIsDisplayed()
            .performClick()

    }

}
