package cz.pef.TravelPlanner.navigation

sealed class Destination(
    val route: String
){
    object  SettingsScreen : Destination(route = "settings_screen")
    object  SplashScreen : Destination(route = "splash_screen")
    object  MapScreen : Destination(route = "map_screen")
    object  ProfileScreen : Destination(route = "profile_screen")
    object  SearchScreen : Destination(route = "search_screen")
    object  InterestsScreen : Destination(route = "interests_screen")
    object  PhotoScannerScreen : Destination(route = "scanner_screen")
    object  ItineraryScreen : Destination(route = "itinerary_screen")
    object  AttractionsScreen : Destination(route = "attractions_screen")
    object  LocationScreen : Destination(route = "location_screen")
    object  AttractionDetailScreen : Destination(route = "attractiondetail_screen")
    object  AttractionPlanScreen : Destination(route = "attractionplan_screen")
}
