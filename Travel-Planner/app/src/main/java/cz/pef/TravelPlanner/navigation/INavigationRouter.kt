package cz.pef.TravelPlanner.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun returnBack()
    fun navigateToSettings()
    fun splashScreen()
    fun navigateToMap()
    fun navigateToProfile()
    fun navigateToSearch()
    fun navitageToInterests()
    fun navigateToPhotoScanner()
    fun navigateToItinerary()
    fun navigateToAttractions()
    fun navigateToLocationSettings()
    fun navigateToAttractionDetailScreen(id: Long)
    fun navigateToAttractionPlanScreen(id: Long)
}