package cz.pef.TravelPlanner.ui.screens.MapScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.TravelPlanner.BuildConfig.API_KEY
import cz.pef.TravelPlanner.communication.CommunicationResult
import cz.pef.TravelPlanner.communication.Geometry
import cz.pef.TravelPlanner.communication.IPetsRemoteRepository
import cz.pef.TravelPlanner.communication.Location
import cz.pef.TravelPlanner.communication.Photo
import cz.pef.TravelPlanner.communication.PlaceResult
import cz.pef.TravelPlanner.database.ITravelRepository
import cz.pef.TravelPlanner.models.PlaceResultEntity
import cz.pef.TravelPlanner.models.SavedPlaceEntity
import cz.pef.TravelPlanner.models.UserSettings
import cz.pef.TravelPlanner.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: IPetsRemoteRepository,
    private val database : ITravelRepository,
    private val preferencesManager: PreferencesManager

) : ViewModel() {
    private val _selectUIState: MutableStateFlow<MapScreenUIState> =
        MutableStateFlow(MapScreenUIState.Loading())
    val selectUIState = _selectUIState.asStateFlow()
    private val data: MapScreenData = MapScreenData()
    var places : List<PlaceResultEntity> = mutableListOf()

    private val _customMarkerEnabled = MutableStateFlow(false)
    val customMarkerEnabled = _customMarkerEnabled.asStateFlow()


    init {
        viewModelScope.launch {
        val loadData = database.getAllPlaces()
            if (loadData.isEmpty()){
                emptydatabase()
            }else{
                var settings = database.getUserSettings()

                if (settings?.latitude == null || settings.longitude == null) {
                    if (settings == null) {
                        settings = UserSettings(latitude = 49.1951, longitude = 16.6068)
                    } else {
                        settings = settings.copy(
                            latitude = settings.latitude ?: 49.1951,
                            longitude = settings.longitude ?: 16.6068
                        )
                    }
                    database.insertOrUpdateUserSettings(settings)
                }
                data.places = loadData.map { place ->
                    PlaceResult(
                        name = place.name,
                        vicinity = place.vicinity,
                        rating = place.rating,
                        geometry = Geometry(Location(lat = place.lat, lng = place.lng)),
                        photos = if (place.photo_reference!=null){ listOf(Photo(place.photo_reference!!))}else{
                            emptyList()
                        } ,
                        icon = place.icon
                    )
                }


                data.userSettings = settings
                // Update the UI state to notify the Composable
                _selectUIState.value = MapScreenUIState.ScreenDataChanged(data)

                preferencesManager.customMarkerEnabledFlow.collectLatest { enabled ->
                    _customMarkerEnabled.value = enabled
                }
            }

        }
    }
    fun emptydatabase() {
        viewModelScope.launch {

            var settings = database.getUserSettings()

            //v případě resetu aplikace se nevkládají latitude a longitude proto je musíme vložit zde
            if (settings?.latitude == null || settings.longitude == null) {
                if (settings == null) {
                    settings = UserSettings(latitude = 49.1951, longitude = 16.6068)
                } else {
                    settings = settings.copy(
                        latitude = settings.latitude ?: 49.1951,
                        longitude = settings.longitude ?: 16.6068
                    )
                }
                database.insertOrUpdateUserSettings(settings)
            }



            val location = "${settings!!.latitude.toString()},${settings.longitude.toString()}"
            val interets = database.getAllInterests()
            val range = settings.maxLocationDistance
            val maximumAttraction = settings.maxAttractionsDisplayed
            //interets.forEach {      Log.d("TravelPlanner", "Interest: $it") }
            val result = withContext(Dispatchers.IO) {
                repository.fetchPlacesNearby(location, range!!*1000, "nature", API_KEY)
            }
            when(result){
                is CommunicationResult.ConnectionError -> {}
                is CommunicationResult.Error -> {}
                is CommunicationResult.Exception -> {

                }
                is CommunicationResult.Success -> {
                    data.places = result.data.results
                    places = result.data.results.map { place ->
                        PlaceResultEntity(
                            name = place.name,
                            vicinity = place.vicinity,
                            rating = place.rating,
                            lat = place.geometry.location.lat,
                            lng = place.geometry.location.lng,
                            photo_reference = place.photos?.firstOrNull()?.photo_reference,
                            icon = place.icon
                        )
                    }
                    database.clearPlaces()
                    database.insertPlaces(places)
                }
            }


            //database.insertPlaces(data.places)
            data.userSettings = settings
            // Update the UI state to notify the Composable
            _selectUIState.value = MapScreenUIState.ScreenDataChanged(data)

            preferencesManager.customMarkerEnabledFlow.collectLatest { enabled ->
                _customMarkerEnabled.value = enabled
            }

        }

    }

    fun refreshResults(){
        viewModelScope.launch {
            var settings = database.getUserSettings()

            // Vložení výchozích hodnot pro latitude a longitude, pokud nejsou nastaveny
            if (settings?.latitude == null || settings.longitude == null) {
                settings = settings?.copy(
                    latitude = settings?.latitude ?: 49.1951,
                    longitude = settings?.longitude ?: 16.6068
                ) ?: UserSettings(latitude = 49.1951, longitude = 16.6068)
                database.insertOrUpdateUserSettings(settings)
            }

            val location = "${settings!!.latitude},${settings.longitude}"
            val interests = database.getAllInterests() // Získání seznamu zájmů
            val range =
                settings.maxLocationDistance ?: 10000 // Výchozí hodnota vzdálenosti (v metrech)
            val maximumAttraction = settings.maxAttractionsDisplayed ?: 10
            val attractionsPerInterest =
                if (interests.isNotEmpty()) maximumAttraction / interests.size else maximumAttraction

            // Uchování všech nalezených míst
            val allPlaces =
                mutableSetOf<PlaceResultEntity>() // Použijeme `mutableSetOf` pro automatické odstranění duplicit
            val currentPlaces =
                mutableSetOf<PlaceResult>() // Uchováme unikátní výsledky na základě klíče

            // Iterace přes každý zájem v seznamu
            for (interest in interests) {
                Log.d("TravelPlanner", "Fetching places for interest: $interest")
                val result = withContext(Dispatchers.IO) {
                    repository.fetchPlacesNearby(location, range * 1000, interest, API_KEY)
                }

                // Zpracování výsledků pro daný zájem
                when (result) {
                    is CommunicationResult.ConnectionError -> {
                        Log.e("TravelPlanner", "Connection error for interest: $interest")
                    }

                    is CommunicationResult.Error -> {
                        Log.e("TravelPlanner", "API error for interest: $interest")
                    }

                    is CommunicationResult.Exception -> {
                        Log.e("TravelPlanner", "Exception for interest: $interest")
                    }

                    is CommunicationResult.Success -> {
                        // Zpracování unikátních výsledků
                        val uniquePlaces = result.data.results
                            .filter { place ->
                                // Přidáme jen místa, která ještě neexistují v `currentPlaces`
                                currentPlaces.none { it.name == place.name && it.geometry.location == place.geometry.location }
                            }
                            .take(attractionsPerInterest)

                        // Přidání do setů
                        uniquePlaces.forEach { place ->
                            currentPlaces.add(place) // Přidáme do aktuálních míst
                            allPlaces.add(
                                PlaceResultEntity(
                                    name = place.name,
                                    vicinity = place.vicinity,
                                    rating = place.rating,
                                    lat = place.geometry.location.lat,
                                    lng = place.geometry.location.lng,
                                    photo_reference = place.photos?.firstOrNull()?.photo_reference,
                                    icon = place.icon
                                )
                            )
                        }
                    }
                }
            }

            // Zkontrolujeme, zda máme dostatek míst, a pokud ne, přidáme náhradní
            /*if (currentPlaces.size < maximumAttraction) {
                val additionalPlaces =
                    generateFallbackPlaces(maximumAttraction - currentPlaces.size)
                currentPlaces.addAll(additionalPlaces)
            }*/

            // Uložení všech míst do databáze
            database.clearPlaces()
            database.insertPlaces(allPlaces.toList())

            // Nastavení dat pro UI
            data.places = currentPlaces.toList()
            data.userSettings = settings
            _selectUIState.value = MapScreenUIState.ScreenDataChanged(data)

            // Collect customMarkerEnabledFlow
            preferencesManager.customMarkerEnabledFlow.collectLatest { enabled ->
                _customMarkerEnabled.value = enabled
            }
        }
    }

    fun refreshResults1() {
        viewModelScope.launch {

            var settings = database.getUserSettings()

            //v případě resetu aplikace se nevkládají latitude a longitude proto je musíme vložit zde
            if (settings?.latitude == null || settings.longitude == null) {
                if (settings == null) {
                    settings = UserSettings(latitude = 49.1951, longitude = 16.6068)
                } else {
                    settings = settings.copy(
                        latitude = settings.latitude ?: 49.1951,
                        longitude = settings.longitude ?: 16.6068
                    )
                }
                database.insertOrUpdateUserSettings(settings)
            }



            val location = "${settings!!.latitude.toString()},${settings.longitude.toString()}"
            val interets = database.getAllInterests()
            val range = settings.maxLocationDistance
            val maximumAttraction = settings.maxAttractionsDisplayed
            //interets.forEach {      Log.d("TravelPlanner", "Interest: $it") }
            val result = withContext(Dispatchers.IO) {
                repository.fetchPlacesNearby(location, range!!*1000, "nature", API_KEY)
            }
            when(result){
                is CommunicationResult.ConnectionError -> {}
                is CommunicationResult.Error -> {}
                is CommunicationResult.Exception -> {

                }
                is CommunicationResult.Success -> {
                    data.places = result.data.results
                    places = result.data.results.map { place ->
                        PlaceResultEntity(
                            name = place.name,
                            vicinity = place.vicinity,
                            rating = place.rating,
                            lat = place.geometry.location.lat,
                            lng = place.geometry.location.lng,
                            photo_reference = place.photos?.firstOrNull()?.photo_reference,
                            icon = place.icon
                        )
                    }
                    database.clearPlaces()
                    database.insertPlaces(places)
                }
            }

            //database.insertPlaces(data.places)
            data.userSettings = settings
            // Update the UI state to notify the Composable
            _selectUIState.value = MapScreenUIState.ScreenDataChanged(data)

            preferencesManager.customMarkerEnabledFlow.collectLatest { enabled ->
                _customMarkerEnabled.value = enabled
            }

        }

    }

    fun initializeCustomMarker(){
        viewModelScope.launch {val enabled = preferencesManager.customMarkerEnabledFlow.first()
            _customMarkerEnabled.value = enabled

            // Poté nastavíme další sledování změn
            preferencesManager.customMarkerEnabledFlow.collectLatest { isEnabled ->
                _customMarkerEnabled.value = isEnabled
            }
        }
    }

    fun savePlace(place: PlaceResultEntity) {
        viewModelScope.launch {
            try {
                database.insertSavedPlace(
                    SavedPlaceEntity(
                        name = place.name,
                        vicinity = place.vicinity,
                        rating = place.rating,
                        lat = place.lat,
                        lng = place.lng,
                        photo_reference = place.photo_reference,
                        icon = place.icon,
                        notes = null
                    )
                )
            } catch (e: Exception) {
                // Můžete přidat chybové hlášení, pokud uložení selže
            }
        }
    }

    fun refresh(){
        viewModelScope.launch {
            val loadData = database.getAllPlaces()
            if (loadData.isEmpty()){
                emptydatabase()
            }else{
                var settings = database.getUserSettings()

                if (settings?.latitude == null || settings.longitude == null) {
                    if (settings == null) {
                        settings = UserSettings(latitude = 49.1951, longitude = 16.6068)
                    } else {
                        settings = settings.copy(
                            latitude = settings.latitude ?: 49.1951,
                            longitude = settings.longitude ?: 16.6068
                        )
                    }
                    database.insertOrUpdateUserSettings(settings)
                }
                data.places = loadData.map { place ->
                    PlaceResult(
                        name = place.name,
                        vicinity = place.vicinity,
                        rating = place.rating,
                        geometry = Geometry(Location(lat = place.lat, lng = place.lng)),
                        photos = if (place.photo_reference!=null){ listOf(Photo(place.photo_reference!!))}else{
                            emptyList()
                        } ,
                        icon = place.icon
                    )
                }


                data.userSettings = settings
                // Update the UI state to notify the Composable
                _selectUIState.value = MapScreenUIState.ScreenDataChanged(data)

                preferencesManager.customMarkerEnabledFlow.collectLatest { enabled ->
                    _customMarkerEnabled.value = enabled
                }
            }

        }
    }
}