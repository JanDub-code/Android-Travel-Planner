package cz.pef.TravelPlanner.communication

data class CommunicationError(
    val code: Int,
    val message: String? = null

)