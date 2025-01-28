package cz.pef.TravelPlanner.database

import cz.pef.TravelPlanner.models.InterestEntity
import cz.pef.TravelPlanner.models.PlaceResultEntity
import cz.pef.TravelPlanner.models.SavedPlaceEntity
import cz.pef.TravelPlanner.models.UserSettings
import javax.inject.Inject



class TravelDatabaseImpl @Inject constructor(private val dao: TravelDao) : ITravelRepository {
    override suspend fun insertOrUpdateUserSettings(userSettings: UserSettings) {
        return dao.insertOrUpdate(userSettings)
    }

    override suspend fun getUserSettings(): UserSettings? {
        return dao.getDefaultUserSettings()
    }

    override suspend fun deleteUserSettings(userSettings: UserSettings) {
        return dao.deleteUserSettings(userSettings)
    }

    override suspend fun addInterest(name: String) {
        return dao.addInterest(InterestEntity(name = name))
    }

    override suspend fun deleteInterest(name: String) {
        val interest = dao.getAllInterests().find { it.name == name }
        if (interest != null) {
            return dao.deleteInterest(interest)
        }
    }

    override suspend fun getAllInterests(): List<String> {
        return dao.getAllInterests().map { it.name }
    }

    override suspend fun getAllPlaces(): List<PlaceResultEntity> {
        return  dao.getAllPlaces()
    }

    override suspend fun insertPlaces(places: List<PlaceResultEntity>) {
        return dao.insertPlaces(places)
    }

    override suspend fun clearPlaces() {
       return dao.clearPlaces()
    }

    override suspend fun getSavedPlaces(): List<SavedPlaceEntity> {
        return dao.getSavedPlaces()
    }

    override suspend fun insertSavedPlace(place: SavedPlaceEntity) {
        return dao.insertSavedPlace(place)
    }

    override suspend fun deleteSavedPlace(place: SavedPlaceEntity) {
        return dao.deleteSavedPlace(place)
    }

    override suspend fun deleteSavedPlaceById(id: Int) {
        return dao.deleteSavedPlaceById(id)
    }

    override suspend fun getSavedPlaceById(id: Long): SavedPlaceEntity? {
        return dao.getSavedPlaceById(id)
    }

}