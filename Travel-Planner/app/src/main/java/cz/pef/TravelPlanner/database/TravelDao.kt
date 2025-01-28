package cz.pef.TravelPlanner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.pef.TravelPlanner.models.InterestEntity
import cz.pef.TravelPlanner.models.PlaceResultEntity
import cz.pef.TravelPlanner.models.SavedPlaceEntity
import cz.pef.TravelPlanner.models.UserSettings

@Dao
interface TravelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(userSettings: UserSettings)

    @Query("SELECT * FROM user_settings WHERE id = :id")
    suspend fun getUserSettingsById(id: Long): UserSettings?

    @Query("SELECT * FROM user_settings LIMIT 1")
    suspend fun getDefaultUserSettings(): UserSettings?

    @Delete
    suspend fun deleteUserSettings(userSettings: UserSettings)

    @Insert
    suspend fun addInterest(interest: InterestEntity)

    @Delete
    suspend fun deleteInterest(interest: InterestEntity)

    @Query("SELECT * FROM interests")
    suspend fun getAllInterests(): List<InterestEntity>



    @Query("SELECT * FROM place_result")
    suspend fun getAllPlaces(): List<PlaceResultEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<PlaceResultEntity>)

    @Query("DELETE FROM place_result")
    suspend fun clearPlaces()



    @Query("SELECT * FROM saved_places")
    suspend fun getSavedPlaces(): List<SavedPlaceEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE) // IGNORE zajistí, že se duplicita nepřepíše
    suspend fun insertSavedPlace(place: SavedPlaceEntity)

    @Delete
    suspend fun deleteSavedPlace(place: SavedPlaceEntity)

    @Query("DELETE FROM saved_places WHERE id = :id") // Smazání podle ID
    suspend fun deleteSavedPlaceById(id: Int)

    @Query("SELECT * FROM saved_places WHERE id = :id")
    suspend fun getSavedPlaceById(id: Long): SavedPlaceEntity?
}