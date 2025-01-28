package cz.pef.TravelPlanner.ui.screens.AttractionPlan

import cz.pef.TravelPlanner.models.UserSettings

sealed class AttractionPlanUIStates {
    class ScreenDataChanged(val data: AttractionPlanData): AttractionPlanUIStates()
    class Loading : AttractionPlanUIStates()
    class Success(val settings: List<UserSettings>) : AttractionPlanUIStates()
    class Error(val errorMessage: String) : AttractionPlanUIStates()
}


