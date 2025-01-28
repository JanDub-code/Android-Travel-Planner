package cz.pef.TravelPlanner.ui.screens.Attraction_Detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pef.TravelPlanner.BuildConfig.API_KEY
import cz.pef.TravelPlanner.communication.CommunicationResult
import cz.pef.TravelPlanner.communication.DirectionResponse
import cz.pef.TravelPlanner.communication.IPetsRemoteRepository
import cz.pef.TravelPlanner.database.ITravelRepository
import cz.pef.TravelPlanner.models.PlaceResultEntity
import cz.pef.TravelPlanner.models.SavedPlaceEntity
import cz.pef.TravelPlanner.utils.PreferencesManager
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
class AttractionDetailsViewModel @Inject constructor(
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

    fun loadPlaceById(id: Long) {
        viewModelScope.launch {
            _place.value = repository.getSavedPlaceById(id)
            var user = repository.getUserSettings()
            val origin = "49.1951,16.6068"
            val destination = "49.1946198,16.5644835"
            val mode = "walking"
            //api.fetchWalkingTime(origin = origin,destination=destination,mode=mode,API_KEY)


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
                    _directions.value = result.data

                }
            }
        }


    }
    fun setTravelStyle(text : String){
        viewModelScope.launch {
            preferencesManager.setCustomTravelStyle(text)
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
}