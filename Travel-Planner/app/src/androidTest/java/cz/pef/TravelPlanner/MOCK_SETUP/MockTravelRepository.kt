package cz.pef.TravelPlanner.MOCK_SETUP
import cz.pef.TravelPlanner.database.ITravelRepository
import cz.pef.TravelPlanner.models.PlaceResultEntity
import cz.pef.TravelPlanner.models.SavedPlaceEntity
import cz.pef.TravelPlanner.models.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow

class MockTravelRepository : ITravelRepository {
    private val _userSettings = MutableStateFlow<UserSettings?>(null)
    private val _interests = MutableStateFlow<List<String>>(emptyList())
    private val _places = MutableStateFlow<List<PlaceResultEntity>>(emptyList())
    private val _savedPlaces = MutableStateFlow<List<SavedPlaceEntity>>(
        listOf(
            SavedPlaceEntity(
                id = 1,
                name = "Eiffel Tower",
                photo_reference = "photo1",
                rating = 4.7,
                vicinity = "Paris",
                lat = 48.8584,
                lng = 2.2945,
                icon = "eiffel_icon",
                notes = "Must visit at night for the lights."
            ),
            SavedPlaceEntity(
                id = 2,
                name = "Statue of Liberty",
                photo_reference = "photo2",
                rating = 4.6,
                vicinity = "New York",
                lat = 40.6892,
                lng = -74.0445,
                icon = "liberty_icon",
                notes = null
            ),
            SavedPlaceEntity(
                id = 3,
                name = "Great Wall",
                photo_reference = "photo3",
                rating = 4.5,
                vicinity = "China",
                lat = 40.4319,
                lng = 116.5704,
                icon = "greatwall_icon",
                notes = "Bring comfortable shoes."
            )
        )
    )

    override suspend fun insertOrUpdateUserSettings(userSettings: UserSettings) {
        _userSettings.value = userSettings
    }

    override suspend fun getUserSettings(): UserSettings? {
        return _userSettings.value
    }

    override suspend fun deleteUserSettings(userSettings: UserSettings) {
        if (_userSettings.value == userSettings) {
            _userSettings.value = null
        }
    }

    override suspend fun addInterest(name: String) {
        _interests.value = _interests.value + name
    }

    override suspend fun deleteInterest(name: String) {
        _interests.value = _interests.value - name
    }

    override suspend fun getAllInterests(): List<String> {
        return _interests.value
    }

    override suspend fun getAllPlaces(): List<PlaceResultEntity> {
        return _places.value
    }

    override suspend fun insertPlaces(places: List<PlaceResultEntity>) {
        _places.value = _places.value + places
    }

    override suspend fun clearPlaces() {
        _places.value = emptyList()
    }

    override suspend fun getSavedPlaces(): List<SavedPlaceEntity> {
        return _savedPlaces.value
    }

    override suspend fun insertSavedPlace(place: SavedPlaceEntity) {
        _savedPlaces.value = _savedPlaces.value + place
    }

    override suspend fun deleteSavedPlace(place: SavedPlaceEntity) {
        _savedPlaces.value = _savedPlaces.value - place
    }

    override suspend fun deleteSavedPlaceById(id: Int) {
        _savedPlaces.value = _savedPlaces.value.filterNot { it.id == id }
    }

    override suspend fun getSavedPlaceById(id: Long): SavedPlaceEntity? {
        return _savedPlaces.value.find { it.id.toLong() == id }
    }
}