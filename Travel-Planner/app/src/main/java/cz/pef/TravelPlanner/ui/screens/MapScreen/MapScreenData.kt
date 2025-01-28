package cz.pef.TravelPlanner.ui.screens.MapScreen

import cz.pef.TravelPlanner.communication.PlaceResult
import cz.pef.TravelPlanner.models.UserSettings

class MapScreenData {
    var userSettings : UserSettings = UserSettings(0,"","",10,10)
    var places : List<PlaceResult> = mutableListOf()

}