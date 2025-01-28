package cz.pef.TravelPlanner.ui.screens.AttractionPlan
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import cz.pef.TravelPlanner.BuildConfig.API_KEY
import cz.pef.TravelPlanner.communication.CommunicationResult
import cz.pef.TravelPlanner.communication.DirectionResponse
import cz.pef.TravelPlanner.communication.IPetsRemoteRepository
import cz.pef.TravelPlanner.database.ITravelRepository
import cz.pef.TravelPlanner.models.PlaceResultEntity
import cz.pef.TravelPlanner.models.SavedPlaceEntity
import cz.pef.TravelPlanner.models.UserSettings
import cz.pef.TravelPlanner.ui.screens.MapScreen.MapScreenData
import cz.pef.TravelPlanner.ui.screens.MapScreen.MapScreenUIState
import cz.pef.TravelPlanner.utils.TravelStyleManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AttractionPlanViewModel @Inject constructor(
    private val repository: ITravelRepository,
    private val api: IPetsRemoteRepository,
    private val preferencesManager: TravelStyleManager


) : ViewModel() {

    private val _place = MutableStateFlow<SavedPlaceEntity?>(null)
    val place: StateFlow<SavedPlaceEntity?> = _place.asStateFlow()

    private val _directions = MutableStateFlow<DirectionResponse?>(null)
    val directions: StateFlow<DirectionResponse?> = _directions.asStateFlow()

    private val _customTravelStyle = MutableStateFlow("")
    val customTravelStyle = _customTravelStyle.asStateFlow()
    private val dataStore = preferencesManager.dataStore
    var coordinates = mutableListOf<Polyline>()
    private val _selectUIState: MutableStateFlow<AttractionPlanUIStates> =
        MutableStateFlow(AttractionPlanUIStates.Loading())
    val selectUIState = _selectUIState.asStateFlow()
    private val data: AttractionPlanData = AttractionPlanData()

    init {
        viewModelScope.launch {
            var user = repository.getUserSettings()
            if (user != null) {
                data.userSettings = user
            }
            _selectUIState.value = AttractionPlanUIStates.ScreenDataChanged(data)

            }
        initializeCustomMarker()
    }
    fun loadPlaceById(id: Long) {
        viewModelScope.launch {
            _place.value = repository.getSavedPlaceById(id)
            var user = repository.getUserSettings()
            val origin = "${user!!.latitude.toString()},${user!!.longitude.toString()}"//"49.1951,16.6068"
            data.userSettings = user
            _selectUIState.value = AttractionPlanUIStates.ScreenDataChanged(data)

            val destination = "${_place.value?.lat.toString()},${_place.value?.lng.toString()}"//"49.1946198,16.5644835"
            val mode = customTravelStyle.value//_customTravelStyle.toString()//"walking"
            //api.fetchWalkingTime(origin = origin,destination=destination,mode=mode,API_KEY)
            Log.d("origin",origin)
            Log.d("destination",destination)
            Log.d("mode", mode)



            val result = withContext(Dispatchers.IO) {
                api.fetchWalkingTime(
                    origin = origin,
                    destination = destination,
                    mode = mode,
                    apiKey = API_KEY
                )
            }
            when(result){
                is CommunicationResult.ConnectionError -> {}
                is CommunicationResult.Error -> {}
                is CommunicationResult.Exception -> {

                }
                is CommunicationResult.Success -> {
                    val directions = result.data
                    _directions.value = directions

// Správné získání overview_polyline
                    val routes = directions.routes
                    if (!routes.isNullOrEmpty()) {
                        // Použijeme pouze první trasu
                        val overviewPolyline = routes[0].overview_polyline.points
                        if (!overviewPolyline.isNullOrEmpty()) {
                            // Dekódování polyline
                            val decodedPolyline = decodePolyline(overviewPolyline)

                            Log.d("Polyline", "Decoded polyline: $decodedPolyline")

                            // Uložení dekódovaných bodů do dat
                            coordinates = listOf(Polyline(decodedPolyline)).toMutableList()

                            // Nastavíme hodnotu directions pouze pro první trasu
                            _directions.value = directions.copy(routes = listOf(routes[0]))
                        } else {
                            Log.e("DirectionsAPI", "No overview polyline found.")
                        }
                    } else {
                        Log.e("DirectionsAPI", "No routes found.")
                    }

                }
            }


        }
    }

    fun initializeCustomMarker(){
        viewModelScope.launch {val enabled = preferencesManager.customTravelStyleFlow.first()
            _customTravelStyle.value = enabled

            // Poté nastavíme další sledování změn
            preferencesManager.customTravelStyleFlow.collectLatest { isEnabled ->
                _customTravelStyle.value = isEnabled
            }
        }
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = mutableListOf<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat / 1E5, lng / 1E5)
            poly.add(p)
        }

        return poly
    }

    data class Polyline(val points: List<LatLng>) {
        fun getLatLngList(): List<LatLng> = points
    }


}