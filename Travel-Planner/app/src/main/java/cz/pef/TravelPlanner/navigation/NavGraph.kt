package cz.pef.TravelPlanner.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.pef.TravelPlanner.ui.screens.AttractionPlan.AttractionPlanScreen
import cz.pef.TravelPlanner.ui.screens.Attraction_Detail.AttractionDetailsScreen
import cz.pef.TravelPlanner.ui.screens.MapScreen.MapScreen
import cz.pef.TravelPlanner.ui.screens.SplashScreen.SplashScreen
import cz.pef.TravelPlanner.ui.screens.attractions.AttractionsScreen
import cz.pef.TravelPlanner.ui.screens.itinerary.ItineraryScreen
import cz.pef.TravelPlanner.ui.screens.photo_scanner.PhotoScannerScreen
import cz.pef.TravelPlanner.ui.screens.select_user_location.SelectLocationScreen
import cz.pef.TravelPlanner.ui.screens.settings.SettingsScreen
import cz.pef.TravelPlanner.ui.screens.settings_interests.InterestsScreen
import cz.pef.TravelPlanner.ui.screens.settings_proflle.SettingsProfile
import cz.pef.TravelPlanner.ui.screens.settings_search.SearchScreen

@ExperimentalFoundationApi
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    navigation: INavigationRouter = remember { NavigationRouterImpl(navController) },
    startDestination: String
) {

    NavHost(
        navController = navController,
        startDestination = startDestination) {

        composable(Destination.SettingsScreen.route) {
            SettingsScreen(navigation = navigation)
        }

        composable(Destination.SplashScreen.route) {
            SplashScreen(navigation = navigation)
        }

        composable(Destination.MapScreen.route) {
            MapScreen(navigation = navigation)
        }

        composable(Destination.ProfileScreen.route) {
            SettingsProfile(navigation = navigation)
        }

        composable(Destination.SearchScreen.route) {
            SearchScreen(navigation = navigation)
        }

        composable(Destination.InterestsScreen.route) {
            InterestsScreen(navigation = navigation)
        }

        composable(Destination.PhotoScannerScreen.route) {
            PhotoScannerScreen(navigation = navigation)
        }

        composable(Destination.ItineraryScreen.route) {
            ItineraryScreen(navigation = navigation)
        }

        composable(Destination.AttractionsScreen.route) {
            AttractionsScreen(navigation = navigation)
        }

        composable(Destination.LocationScreen.route) {
            SelectLocationScreen(navigation = navigation)
        }

        composable(
            route = Destination.AttractionDetailScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val id = it.arguments?.getLong("id")
            id?.let { attractionId ->
                AttractionDetailsScreen(
                    placeId = attractionId,
                    navigation = navigation,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = Destination.AttractionPlanScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val id = it.arguments?.getLong("id")
            id?.let { attractionId ->
                AttractionPlanScreen(
                    placeId = attractionId,
                    navigation = navigation,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }


    }
}
