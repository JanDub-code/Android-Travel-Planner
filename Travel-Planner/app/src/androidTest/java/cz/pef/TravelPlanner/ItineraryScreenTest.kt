package cz.pef.TravelPlanner

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import cz.pef.TravelPlanner.Itinerary_MOCK_DATA.MockItineraryScreen
import cz.pef.TravelPlanner.MOCK_SETUP.MockNavigationRouter
import cz.pef.TravelPlanner.models.SavedPlaceEntity
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class ItineraryScreenTest {

    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun itineraryScreen_displaysSavedPlaces() {
        val mockNavigation = MockNavigationRouter()

        composeRule.setContent {
            MockItineraryScreen(
                navigation = mockNavigation
            )
        }
        // Verify that the LazyColumn is displayed
        composeRule.onNodeWithTag("ItineraryLazyColumn")
            .assertIsDisplayed()

        // Define expected mock places
        val mockPlaces = listOf(
            SavedPlaceEntity(
                id = 1,
                name = "Eiffel Tower",
                photo_reference = "photo1",
                rating = 4.7,
                vicinity = "Paris",
                lat = 48.8584,
                lng = 2.2945,
                icon = "eiffel_icon",
                notes = "Must visit at night for the lights."
            ),
            SavedPlaceEntity(
                id = 2,
                name = "Statue of Liberty",
                photo_reference = "photo2",
                rating = 4.6,
                vicinity = "New York",
                lat = 40.6892,
                lng = -74.0445,
                icon = "liberty_icon",
                notes = null
            ),
            SavedPlaceEntity(
                id = 3,
                name = "Great Wall",
                photo_reference = "photo3",
                rating = 4.5,
                vicinity = "China",
                lat = 40.4319,
                lng = 116.5704,
                icon = "greatwall_icon",
                notes = "Bring comfortable shoes."
            )
        )

        mockPlaces.forEach { place ->
            // Check if the place name is displayed
            composeRule.onNodeWithTag("PlaceName_${place.id}")
                .assertIsDisplayed()

            // Check if the place rating is displayed
            composeRule.onNodeWithTag("PlaceRating_${place.id}")
                .assertIsDisplayed()

            // Check if the place vicinity is displayed
            composeRule.onNodeWithTag("PlaceVicinity_${place.id}")
                .assertIsDisplayed()


        }
    }

    @Test
    fun itineraryScreen_navigateButton_clickNavigates() {
        val mockNavigation = MockNavigationRouter()

        composeRule.setContent {
            MockItineraryScreen(
                navigation = mockNavigation
            )
        }

        // Assume we're testing the first place's navigate button
        val firstPlaceId = 1L
        composeRule.onNodeWithTag("NavigateButton_${firstPlaceId}")
            .assertIsDisplayed()
            .performClick()

        // Verify that navigation was called
        assertTrue(mockNavigation.navigateToAttractionDetailCalled)
    }

    @Test
    fun itineraryScreen_deleteButton_clickDeletesPlace() {
        val mockNavigation = MockNavigationRouter()

        composeRule.setContent {
            MockItineraryScreen(
                navigation = mockNavigation
            )
        }

        val firstPlaceId = 1
        composeRule.onNodeWithTag("DeleteButton_${firstPlaceId}")
            .assertIsDisplayed()
            .performClick()

        // Verify that the place is no longer displayed
        composeRule.onNodeWithTag("ItineraryCard_$firstPlaceId")
            .assertDoesNotExist()
    }

   @Test
    fun itineraryScreen_clickNavigateButton_navigatesToDetail() {
       val mockNavigation = MockNavigationRouter()

       composeRule.setContent {
           MockItineraryScreen(
               navigation = mockNavigation
           )
       }

        // Click navigate button for the second place
        val secondPlaceId = 2L
        composeRule.onNodeWithTag("NavigateButton_${secondPlaceId}")
            .assertIsDisplayed()
            .performClick()

        // Verify that the navigation method was called
        assertTrue(mockNavigation.navigateToAttractionDetailCalled)
    }

    @Test
    fun itineraryScreen_notesDisplayedCorrectly() {
        val mockNavigation = MockNavigationRouter()

        composeRule.setContent {
            MockItineraryScreen(
                navigation = mockNavigation
            )
        }

        // Check notes for the first place (has notes)
        composeRule.onNodeWithTag("PlaceNotes_1")
            .assertIsDisplayed()
            .assertTextContains("Notes: Must visit at night for the lights.")

        // Check notes for the second place (no notes)
        composeRule.onNodeWithTag("PlaceNotes_2")
            .assertDoesNotExist()

        // Check notes for the third place (has notes)
        composeRule.onNodeWithTag("PlaceNotes_3")
            .assertIsDisplayed()
            .assertTextContains("Notes: Bring comfortable shoes.")
    }
}