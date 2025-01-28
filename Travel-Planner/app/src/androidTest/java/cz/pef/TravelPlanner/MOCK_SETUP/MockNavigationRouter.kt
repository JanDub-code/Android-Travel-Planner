package cz.pef.TravelPlanner.MOCK_SETUP

import androidx.navigation.NavController
import cz.pef.TravelPlanner.navigation.INavigationRouter

class MockNavigationRouter : INavigationRouter {

    // Flagy pro sledování volání metod
    var navigateToInterestsCalled = false
    var navigateToProfileCalled = false
    var navigateToSearchCalled = false
    var navigateToPhotoScannerCalled = false
    var navigateToItineraryCalled = false
    var navigateToAttractionsCalled = false
    var navigateToLocationSettingsCalled = false
    var navigateToListOfPetsScreenCalled = false
    var navigateToPetCreateCalled = false
    var navigateToPetDetailScreenCalled = false
    var returnBackCalled = false
    var navigateToLoginCalled = false
    var navigateToSettingsCalled = false
    var splashScreenCalled = false
    var navigateToMapCalled = false
    var navigateToAttractionPlanCalled = false
    var navigateToAttractionDetailCalled = false


    // Mockované metody navigace
    override fun navitageToInterests() {
        navigateToInterestsCalled = true
    }

    override fun navigateToProfile() {
        navigateToProfileCalled = true
    }

    override fun navigateToSearch() {
        navigateToSearchCalled = true
    }

    override fun navigateToPhotoScanner() {
        navigateToPhotoScannerCalled = true
    }

    override fun navigateToItinerary() {
        navigateToItineraryCalled = true
    }

    override fun navigateToAttractions() {
        navigateToAttractionsCalled = true
    }

    override fun navigateToLocationSettings() {
        navigateToLocationSettingsCalled = true
    }

    override fun navigateToAttractionDetailScreen(id: Long) {
        navigateToAttractionDetailCalled = true
    }

    override fun navigateToAttractionPlanScreen(id: Long) {
        navigateToAttractionPlanCalled = true
    }



    override fun returnBack() {
        returnBackCalled = true
    }


    override fun navigateToSettings() {
        navigateToSettingsCalled = true
    }

    override fun splashScreen() {
        splashScreenCalled = true
    }

    override fun navigateToMap() {
        navigateToMapCalled = true
    }

    override fun getNavController(): NavController {
        throw NotImplementedError("MockNavigationRouter does not provide a real NavController")
    }
}
