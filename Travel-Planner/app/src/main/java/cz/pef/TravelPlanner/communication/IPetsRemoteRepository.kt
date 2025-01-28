package cz.pef.TravelPlanner.communication

interface IPetsRemoteRepository : IBaseRemoteRepository {
    suspend fun fetchPlacesNearby(location: String, radius: Int, type: String, apiKey: String): CommunicationResult<PlaceResponse>
    suspend fun fetchWalkingTime(origin: String, destination: String, mode: String, apiKey: String): CommunicationResult<DirectionResponse>
    suspend fun fetchBalancedPlacesByInterest(location: String, radius: Int, keyword: String, apiKey: String): CommunicationResult<PlaceResponse>
}