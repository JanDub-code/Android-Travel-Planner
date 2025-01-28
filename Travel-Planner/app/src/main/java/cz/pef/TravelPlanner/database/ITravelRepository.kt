package cz.pef.TravelPlanner.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.pef.TravelPlanner.models.InterestEntity
import cz.pef.TravelPlanner.models.PlaceResultEntity
import cz.pef.TravelPlanner.models.SavedPlaceEntity
import cz.pef.TravelPlanner.models.UserSettings

interface ITravelRepository {
    suspend fun insertOrUpdateUserSettings(userSettings: UserSettings)
    suspend fun getUserSettings(): UserSettings?
    suspend fun deleteUserSettings(userSettings: UserSettings)


    suspend fun addInterest(name: String)
    suspend fun deleteInterest(name: String)
    suspend fun getAllInterests(): List<String>

    suspend fun getAllPlaces(): List<PlaceResultEntity>
    suspend fun insertPlaces(places: List<PlaceResultEntity>)
    suspend fun clearPlaces()

    suspend fun getSavedPlaces(): List<SavedPlaceEntity>
    suspend fun insertSavedPlace(place: SavedPlaceEntity)
    suspend fun deleteSavedPlace(place: SavedPlaceEntity)
    suspend fun deleteSavedPlaceById(id: Int)
    suspend fun getSavedPlaceById(id: Long): SavedPlaceEntity?

}