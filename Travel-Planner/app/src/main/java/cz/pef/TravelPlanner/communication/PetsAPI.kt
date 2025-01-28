package cz.pef.TravelPlanner.communication

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PetsAPI {
@GET("place/nearbysearch/json")
suspend fun fetchPlacesNearby(
    @Query("location") location: String,
    @Query("radius") radius: Int,
    @Query("type") type: String,
    @Query("key") apiKey: String
): Response<PlaceResponse>

@GET("directions/json")
suspend fun fetchWalkingTime(
    @Query("origin") origin: String,
    @Query("destination") destination: String,
    @Query("mode") mode: String,
    @Query("key") apiKey: String
): Response<DirectionResponse>

@GET("place/nearbysearch/json")
suspend fun fetchBalancedPlacesByInterest(
    @Query("location") location: String,
    @Query("radius") radius: Int,
    @Query("keyword") keyword: String,
    @Query("key") apiKey: String
): Response<PlaceResponse>
}

