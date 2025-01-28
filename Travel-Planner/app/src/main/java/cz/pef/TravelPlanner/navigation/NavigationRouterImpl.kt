package cz.pef.TravelPlanner.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun getNavController(): NavController = navController
    

    override fun returnBack() {
        navController.popBackStack()
    }


    override fun navigateToSettings() {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate(Destination.SettingsScreen.route)
        }
    }

    override fun splashScreen() {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate(Destination.SplashScreen.route)
        }
    }

    override fun navigateToMap() {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate(Destination.MapScreen.route)
        }
    }

    override fun navigateToProfile() {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate(Destination.ProfileScreen.route)
        }
    }

    override fun navigateToSearch() {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate(Destination.SearchScreen.route)
        }
    }

    override fun navitageToInterests() {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate(Destination.InterestsScreen.route)
        }
    }

    override fun navigateToPhotoScanner() {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate(Destination.PhotoScannerScreen.route)
        }
    }

    override fun navigateToItinerary() {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate(Destination.ItineraryScreen.route)
        }
    }

    override fun navigateToAttractions() {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate(Destination.AttractionsScreen.route)
        }
    }

    override fun navigateToLocationSettings() {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate(Destination.LocationScreen.route)
        }
    }

    override fun navigateToAttractionDetailScreen(id: Long) {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate("${Destination.AttractionDetailScreen.route}/${id}")
        }
    }

    override fun navigateToAttractionPlanScreen(id: Long) {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.navigate("${Destination.AttractionPlanScreen.route}/${id}")
        }
    }
}