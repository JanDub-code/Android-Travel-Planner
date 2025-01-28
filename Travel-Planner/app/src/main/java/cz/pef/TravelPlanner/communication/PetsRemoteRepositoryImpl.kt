package cz.pef.TravelPlanner.communication


import javax.inject.Inject

class PetsRemoteRepositoryImpl @Inject constructor(private val petsAPI: PetsAPI) : IPetsRemoteRepository {

    override suspend fun fetchPlacesNearby(location: String, radius: Int, type: String, apiKey: String): CommunicationResult<PlaceResponse> {
        return makeApiCall { petsAPI.fetchPlacesNearby(location, radius, type, apiKey) }
    }

    override suspend fun fetchWalkingTime(origin: String, destination: String, mode: String, apiKey: String): CommunicationResult<DirectionResponse> {
        return makeApiCall { petsAPI.fetchWalkingTime(origin, destination, mode, apiKey) }
    }

    override suspend fun fetchBalancedPlacesByInterest(location: String, radius: Int, keyword: String, apiKey: String): CommunicationResult<PlaceResponse> {
        return makeApiCall { petsAPI.fetchBalancedPlacesByInterest(location, radius, keyword, apiKey) }
    }
}